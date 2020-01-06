package com.xxy.sfl.pub.service;

import com.xxy.sfl.pub.entity.BeanHelper;
import com.xxy.sfl.pub.entity.EntityMetaUtil;
import com.xxy.sfl.pub.entity.SuperEntity;
import com.xxy.sfl.pub.exception.BusinessException;
import com.xxy.sfl.pub.repo.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 通用服务接口实现类
 *
 * @author xg
 */
public class CommonServiceImpl<R extends BaseJpaRepository<T>, T extends SuperEntity> implements ICommonService<T> {

    @Autowired
    private R repo;

    @Override
    public T findById(Serializable id) throws BusinessException {
        if (id == null) {
            return null;
        }
        Optional<T> result = repo.findById(id.toString());
        if (result == null || !result.isPresent()) {
            return null;
        }
        return result.get();
    }

    @Override
    @SuppressWarnings("serial")
    public List<T> findAll(Map<String, Object> condition) throws BusinessException {
        if (condition == null || condition.isEmpty()) {
            return repo.findAll();
        }
        return repo.findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Set<String> keys = condition.keySet();
                Iterator<String> it = keys.iterator();
                List<Predicate> predicates = new ArrayList<Predicate>();
                while (it.hasNext()) {
                    String key = it.next();
                    Object value = condition.get(key);
                    if (value != null) {
                        predicates.add(cb.equal(root.get(key), value));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

    @Override
    @SuppressWarnings("serial")
    public Page<T> findPage(Pageable page, Map<String, Object> condition) throws BusinessException {
        if (page == null) {
            page = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        }
        if (condition == null || condition.isEmpty()) {
            return repo.findAll(page);
        }
        return repo.findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Set<String> keys = condition.keySet();
                Iterator<String> it = keys.iterator();
                List<Predicate> predicates = new ArrayList<Predicate>();
                while (it.hasNext()) {
                    String key = it.next();
                    Object value = condition.get(key);
                    if (value != null) {
                        predicates.add(cb.equal(root.get(key), value));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, page);
    }

    @Override
    public T saveOrUpdate(T t) throws BusinessException {
        if (t == null) {
            return null;
        }
        t.setTs(LocalDateTime.now());
        return repo.save(t);
    }

    @Override
    public void deleteByIds(Serializable... ids) throws BusinessException {
        if (ids == null || ids.length == 0) {
            return;
        }
        repo.deleteByIds(ids);
        throw new BusinessException("未知异常");
    }

    @Override
    public void deleteAll(Collection<T> ts) throws BusinessException {
        if (ts == null || ts.isEmpty()) {
            return;
        }
        List<Serializable> idList = new ArrayList<Serializable>(ts.size());
        for (T t : ts) {
            String idField = EntityMetaUtil.getPrimaryField(t.getClass());
            Object id = BeanHelper.getAttributeValue(t, idField);
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
    public void deleteAll() throws BusinessException {
        repo.deleteAll();
    }
}
