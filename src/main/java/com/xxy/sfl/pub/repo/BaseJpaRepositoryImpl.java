package com.xxy.sfl.pub.repo;

import com.xxy.sfl.pub.entity.BeanHelper;
import com.xxy.sfl.pub.entity.EntityMetaInfo;
import com.xxy.sfl.pub.entity.EntityMetaUtil;
import com.xxy.sfl.pub.entity.SuperEntity;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.*;

/**
 * 重写SimpleJpaRepository部分方法
 * <ul>
 * <li>1.实现逻辑删除</li>
 * <li>2.优化批量删除效率</li>
 * <li>3.批量保存的效率优化后续处理</li>
 * </ul>
 *
 * @author xg
 */
public class BaseJpaRepositoryImpl<T extends SuperEntity> extends SimpleJpaRepository<T, Serializable>
        implements BaseJpaRepository<T> {

    private final EntityManager em;

    public BaseJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Serializable id) {
        if (id == null) {
            return;
        }
        this.deleteByIds(new Serializable[]{id});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Serializable... ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder("(");
        for (Serializable id : ids) {
            sb.append("'").append(id).append("',");
        }
        sb.deleteCharAt(sb.lastIndexOf(",")).append(")");
        Class<?> clazz = this.getDomainClass();
        EntityMetaInfo metaInfo = EntityMetaUtil.getEntityMetaInfo(clazz);
        String tableName = metaInfo.getTableName();
        String pkColumn = metaInfo.getPkColumn();
        Assert.hasText(tableName, "can not get table name on bean " + clazz.getName());
        Assert.hasText(pkColumn, "can not get id column on bean " + clazz.getName());
        Map<String, Class<?>> children = metaInfo.getChildren();
        if (children != null && !children.isEmpty()) {
            Set<String> keys = children.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                Class<?> subClass = children.get(key);
                EntityMetaInfo subMetaInfo = EntityMetaUtil.getEntityMetaInfo(subClass);
                Map<String, String> joinColumns = metaInfo.getJoinColumns();
                String joinColumn = joinColumns == null || !joinColumns.containsKey(key) ? null : joinColumns.get(key);
                String subTableName = subMetaInfo.getTableName();
                Assert.hasText(subTableName, "can not get table name on bean " + clazz.getName());
                Assert.hasText(joinColumn, "can not get join column on bean " + clazz.getName());
                String subSql = "update " + subTableName + " set " + SuperEntity.ATT_DR + " = "
                        + SuperEntity.IS_DELETE_YES + " where " + joinColumn + " in " + sb.toString();
                em.createNativeQuery(subSql).executeUpdate();
            }
        }
        String mainSql = "update " + tableName + " set " + SuperEntity.ATT_DR + " = " + SuperEntity.IS_DELETE_YES
                + " where " + pkColumn + " in " + sb.toString();
        em.createNativeQuery(mainSql).executeUpdate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(T entity) {
        if (entity == null) {
            return;
        }
        Object id = BeanHelper.getAttributeValue(entity, SuperEntity.ATT_ID);
        if (id != null) {
            this.deleteById(String.valueOf(id));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Iterable<? extends T> entities) {
        if (entities == null) {
            return;
        }
        Iterator<? extends T> it = entities.iterator();
        List<Serializable> idList = new LinkedList<Serializable>();
        while (it.hasNext()) {
            T t = it.next();
            Object id = BeanHelper.getAttributeValue(t, SuperEntity.ATT_ID);
            if (id == null) {
                continue;
            }
            idList.add(String.valueOf(id));
        }
        if (!idList.isEmpty()) {
            this.deleteByIds(idList.toArray(new Serializable[idList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInBatch(Iterable<T> entities) {
        this.deleteAll(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        this.deleteAllInBatch();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllInBatch() {
        Class<?> clazz = this.getDomainClass();
        EntityMetaInfo metaInfo = EntityMetaUtil.getEntityMetaInfo(clazz);
        String tableName = metaInfo.getTableName();
        Assert.hasText(tableName, "can not get table name on bean " + clazz.getName());
        Map<String, Class<?>> children = metaInfo.getChildren();
        if (children != null && !children.isEmpty()) {
            Set<String> keys = children.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                Class<?> subClass = children.get(key);
                String subTableName = EntityMetaUtil.getTableName(subClass);
                Assert.hasText(subTableName, "can not get table name on bean " + subClass.getName());
                String subSql = "update " + subTableName + " set " + SuperEntity.ATT_DR + " = "
                        + SuperEntity.IS_DELETE_YES;
                em.createNativeQuery(subSql).executeUpdate();
            }
        }
        String mainSql = "update " + tableName + " set " + SuperEntity.ATT_DR + " = " + SuperEntity.IS_DELETE_YES;
        em.createNativeQuery(mainSql).executeUpdate();
    }

}
