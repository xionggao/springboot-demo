package com.xxy.sfl.pub.entity;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 实体元数据工具
 * <ul>
 * <li>1.实体需符合JPA规范</li>
 * <li>2.暂时只处理单向一对多关联关系的实体</li>
 * </ul>
 * 
 * @author xg
 * @date 2019-10-24
 */
public final class EntityMetaUtil {

	private static Map<String, EntityMetaInfo> entityMetaCache = new HashMap<String, EntityMetaInfo>();

	/**
	 * 获取实体对应的数据库表名
	 * 
	 * @param clazz 实体类
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		EntityMetaInfo metaInfo = getEntityMetaInfo(clazz);
		return metaInfo == null ? null : metaInfo.getTableName();
	}

	/**
	 * 获取实体的主键字段名
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getPrimaryField(Class<?> clazz) {
		EntityMetaInfo metaInfo = getEntityMetaInfo(clazz);
		return metaInfo == null ? null : metaInfo.getPkField();
	}

	/**
	 * 获取实体主键字段对应的数据库列名
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getPrimaryColumn(Class<?> clazz) {
		EntityMetaInfo metaInfo = getEntityMetaInfo(clazz);
		return metaInfo == null ? null : metaInfo.getPkColumn();
	}

	/**
	 * 获取实体对应的数据库列名集合
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<String> getColumns(Class<?> clazz) {
		EntityMetaInfo metaInfo = getEntityMetaInfo(clazz);
		return metaInfo == null ? null : metaInfo.getColumns();
	}

	/**
	 * 获取外键关联的列
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getJoinColumn(Class<?> clazz, String key) {
		EntityMetaInfo metaInfo = getEntityMetaInfo(clazz);
		if (metaInfo == null) {
			return null;
		}
		Map<String, String> joinColumns = metaInfo.getJoinColumns();
		return joinColumns == null || !joinColumns.containsKey(key) ? null : joinColumns.get(key);
	}

	/**
	 * 获取实体元数据信息
	 * 
	 * @param clazz
	 * @return
	 */
	public static EntityMetaInfo getEntityMetaInfo(Class<?> clazz) {
		Assert.notNull(clazz, "Bean class can not be null!");
		if (!entityMetaCache.containsKey(clazz.getName())) {
			initMetaEntityMap(clazz);
		}
		return entityMetaCache.get(clazz.getName());
	}

	/**
	 * 解析实体元数据
	 * 
	 * @param clazz
	 */
	private static void initMetaEntityMap(Class<?> clazz) {
		try {
			EntityMetaInfo metaInfo = new EntityMetaInfo();
			Entity entity = clazz.getAnnotation(Entity.class);
			Assert.notNull(entity, clazz.getName() + " must be a java persistence entity");
			Table table = clazz.getAnnotation(Table.class);
			String tableName = (table == null || !StringUtils.hasText(table.name()))
					? camelToUnderline(clazz.getSimpleName()) : table.name();
			metaInfo.setTableName(tableName);
			Map<String, String> fieldColumnMap = new HashMap<String, String>();
			List<String> columns = new LinkedList<String>();
			String pkField = null;
			String pkColumn = null;
			Map<String, String> joinColumns = new HashMap<String, String>();
			Map<String, Class<?>> children = new HashMap<String, Class<?>>();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				Field field = getField(clazz, pd.getName());
				if (field == null) {
					throw new IllegalArgumentException(
							"Failed to get field on class " + clazz.getName() + " by name " + pd.getName());
				}
				Method readMethod = pd.getReadMethod();
				// 过滤掉@Transient修饰的属性
				if (field.getAnnotation(Transient.class) != null || readMethod.getAnnotation(Transient.class) != null) {
					continue;
				}
				OneToMany otm = field.getAnnotation(OneToMany.class) == null ? readMethod.getAnnotation(OneToMany.class)
						: field.getAnnotation(OneToMany.class);
				ManyToOne mto = field.getAnnotation(ManyToOne.class) == null ? readMethod.getAnnotation(ManyToOne.class)
						: field.getAnnotation(ManyToOne.class);
				JoinColumn jc = field.getAnnotation(JoinColumn.class) == null
						? readMethod.getAnnotation(JoinColumn.class) : field.getAnnotation(JoinColumn.class);
				if (otm != null) {
					Type returnType = pd.getReadMethod().getGenericReturnType();
					Type paramType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
					children.put(pd.getName(), Class.forName(paramType.getTypeName()));
					if (jc != null) { // 单向一对多
						String joinColumn = StringUtils.hasText(jc.name()) ? jc.name()
								: camelToUnderline(field.getName());
						joinColumns.put(pd.getName(), joinColumn);
						metaInfo.setJoinColumns(joinColumns);
					}
					continue;
				} else if (mto != null && jc != null) { // 双向一对多
					String joinColumn = StringUtils.hasText(jc.name()) ? jc.name() : camelToUnderline(field.getName());
					joinColumns.put(pd.getName(), joinColumn);
					metaInfo.setJoinColumns(joinColumns);
					continue;
				}
				Id id = field.getAnnotation(Id.class) == null ? readMethod.getAnnotation(Id.class)
						: field.getAnnotation(Id.class);
				Column column = field.getAnnotation(Column.class) == null ? readMethod.getAnnotation(Column.class)
						: field.getAnnotation(Column.class);

				if (id != null) {
					pkField = field.getName();
					if (column == null || !StringUtils.hasText(column.name())) {
						pkColumn = camelToUnderline(field.getName());
					} else {
						pkColumn = column.name();
					}
					columns.add(pkColumn);
					fieldColumnMap.put(pkField, pkColumn);
				} else {
					String colName;
					if (column == null || !StringUtils.hasText(column.name())) {
						colName = camelToUnderline(field.getName());
					} else {
						colName = column.name();
					}
					columns.add(colName);
					fieldColumnMap.put(field.getName(), colName);
				}
			}
			metaInfo.setPkField(pkField);
			metaInfo.setPkColumn(pkColumn);
			metaInfo.setColumns(columns);
			metaInfo.setFieldColumnMap(fieldColumnMap);
			metaInfo.setChildren(children);
			entityMetaCache.put(clazz.getName(), metaInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据名称获取Field(递归方式)
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		Field field = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length < 0) {
			return null;
		}
		for (Field f : fields) {
			if (f.getName().equals(fieldName)) {
				field = f;
				break;
			}
		}
		if (field == null && clazz.getSuperclass() != null) {
			field = getField(clazz.getSuperclass(), fieldName);
		}
		return field;
	}

	/**
	 * 获取实体对应的数据库字段名
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<String> getAllColumns(Class<?> clazz) {
		EntityMetaInfo metaInfo = getEntityMetaInfo(clazz);
		return metaInfo == null ? null : metaInfo.getColumns();
	}
	
	/**
	 * 驼峰转下划线
	 * 
	 * @param str
	 * @return
	 */
	public static String camelToUnderline(String str) {
		Assert.hasText(str, "parameter must not be null");
		int len = str.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_");
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}
}
