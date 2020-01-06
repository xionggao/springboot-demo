package com.xxy.sfl.pub.entity;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解析JavaBean(单例)
 * 
 * @author xg
 */
public class BeanHelper {

	private static BeanHelper helper;

	private static final Object[] NULL_ARGUMENTS = {};

	private static Map<String, Map<String, PropertyDescriptor>> cache = new ConcurrentHashMap<String, Map<String, PropertyDescriptor>>();

	private BeanHelper() {
	}

	private static BeanHelper getInstance() {
		if (helper == null) {
			helper = new BeanHelper();
		}
		return helper;
	}

	/**
	 * 获取JavaBean属性值
	 * 
	 * @param bean 实体对象
	 * @param attributeName 实体属性名
	 * @return
	 */
	public static Object getAttributeValue(Object bean, String attributeName) {
		try {
			Method readMethod = getReadMethod(bean, attributeName);
			if (readMethod != null) {
				return readMethod.invoke(bean, NULL_ARGUMENTS);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Failed to get attribute value: " + attributeName + " at bean " + bean.getClass().getName(), e);
		}
		return null;
	}

	/**
	 * 设置JavaBean属性值
	 * 
	 * @param bean
	 * @param attributeName
	 * @param value
	 */
	public static void setAttributeValue(Object bean, String attributeName, final Object value) {
		try {
			Method writeMethod = getWriteMethod(bean, attributeName);
			if (writeMethod != null) {
				writeMethod.invoke(bean, value);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Failed to set attribute value: " + attributeName + " at bean " + bean.getClass().getName()
							+ " with value " + (value == null ? "null" : value) + "@" + value.getClass().getName(),
					e);
		}
	}

	/**
	 * 获取JavaBean属性的getter方法
	 * 
	 * @param bean
	 * @param attributeName
	 * @return
	 */
	public static Method getReadMethod(Object bean, String attributeName) {
		if (attributeName == null || attributeName.isEmpty()) {
			return null;
		}
		Map<String, PropertyDescriptor> map = getInstance().getPropertyDescriptor(bean);
		if (map == null || map.isEmpty() || !map.containsKey(attributeName)) {
			return null;
		}
		return map.get(attributeName).getReadMethod();
	}

	/**
	 * 获取JavaBean属性的setter方法
	 * 
	 * @param bean
	 * @param attributeName
	 * @return
	 */
	public static Method getWriteMethod(Object bean, String attributeName) {
		if (attributeName == null || attributeName.isEmpty()) {
			return null;
		}
		Map<String, PropertyDescriptor> map = getInstance().getPropertyDescriptor(bean);
		if (map == null || map.isEmpty() || !map.containsKey(attributeName)) {
			return null;
		}
		return map.get(attributeName).getWriteMethod();
	}

	/**
	 * 获取JavaBean的所有属性名
	 * 
	 * @param bean
	 * @return
	 */
	public static Set<String> getAttributeNames(Object bean) {
		Map<String, PropertyDescriptor> map = getInstance().getPropertyDescriptor(bean);
		return map == null ? null : map.keySet();
	}

	/**
	 * 执行JavaBean中定义的方法
	 * 
	 * @param bean
	 * @param method
	 * @param args
	 */
	public static Object invokeMethod(Object bean, Method method, final Object... args) {
		try {
			if (bean != null && method != null) {
				return method.invoke(bean, args == null ? NULL_ARGUMENTS : args);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Failed to invoke method: " + method.getName() + " on bean " + bean.getClass().getName(), e);
		}
		return null;
	}

	public Map<String, PropertyDescriptor> getPropertyDescriptor(Object bean) {
		if (bean == null) {
			return null;
		}
		try {
			String className = bean.getClass().getName();
			if (cache.containsKey(className)) {
				return cache.get(className);
			} else {
				Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
				BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					map.put(pd.getName(), pd);
				}
				cache.put(className, map);
				return map;
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to get attribute info on bean " + bean.getClass().getName(), e);
		}
	}
}
