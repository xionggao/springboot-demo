package com.xxy.sfl.pub.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 实体元数据信息
 * 
 * @author xg
 * @date 2019-10-24
 */
public class EntityMetaInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6182654923575963252L;

	private String pkField; // 主键字段属性名
	private String pkColumn; // 主键字段对应数据库列名
	private String tableName; // 实体对应的表名
	private Map<String, String> joinColumns; // 外键关联关系(key为子实体在主实体中的属性,value为子实体外键字段名)
	private Map<String, String> fieldColumnMap; // 实体字段与列的对应关系
	private List<String> columns; // 实体所有数据库列
	private Map<String, Class<?>> children; // 子实体(key为子实体在主实体中的属性,value为子实体类型)

	public String getPkField() {
		return pkField;
	}

	public void setPkField(String pkField) {
		this.pkField = pkField;
	}

	public String getPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, String> getJoinColumns() {
		return joinColumns;
	}

	public void setJoinColumns(Map<String, String> joinColumns) {
		this.joinColumns = joinColumns;
	}

	public Map<String, String> getFieldColumnMap() {
		return fieldColumnMap;
	}

	public void setFieldColumnMap(Map<String, String> fieldColumnMap) {
		this.fieldColumnMap = fieldColumnMap;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public Map<String, Class<?>> getChildren() {
		return children;
	}

	public void setChildren(Map<String, Class<?>> children) {
		this.children = children;
	}
}
