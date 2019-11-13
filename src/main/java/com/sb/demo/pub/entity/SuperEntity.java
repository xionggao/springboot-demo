package com.sb.demo.pub.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * JPA实体基类
 * 
 * @author xg
 * @date 2019-10-21
 */
@MappedSuperclass
public abstract class SuperEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -408295561272928194L;

	public static final int IS_DELETE_NO = 0;
	public static final int IS_DELETE_YES = 1;

	/**
	 * 实体主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@CreationTimestamp
	private LocalDateTime createTime;;

	/**
	 * 逻辑删除标记
	 */
	@Column(name = "dr", length = 1)
	private Integer dr = 0;

	/**
	 * 时间戳(用于同步锁)
	 */
	@Column(name = "ts")
	@UpdateTimestamp
	private LocalDateTime ts;

	public static final String ATT_ID = "id";
	public static final String ATT_DR = "dr";
	public static final String ATT_TS = "ts";
	public static final String ATT_CREATE_TIME = "createTime";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getTs() {
		return ts;
	}

	public void setTs(LocalDateTime ts) {
		this.ts = ts;
	}

	/**
	 * 取属性值
	 * 
	 * @param attributeName
	 * @return
	 */
	public Object getAttributeValue(String attributeName) {
		return BeanHelper.getAttributeValue(this, attributeName);
	}

	/**
	 * 设置属性值
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void setAttributeValue(String attributeName, Object value) {
		BeanHelper.setAttributeValue(this, attributeName, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		return this.toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		// 31可以拆解为2<<5 -1,虚拟机做了相关优化,本身计算机乘除移位效率很高,这可能是用31的原因
		final int prime = 31;
		int result = 1;
		result = prime * result + (BeanHelper.getAttributeValue(this, ATT_ID) == null ? 0
				: BeanHelper.getAttributeValue(this, ATT_ID).hashCode());
		result = prime * result + (BeanHelper.getAttributeValue(this, ATT_DR) == null ? 0
				: BeanHelper.getAttributeValue(this, ATT_DR).hashCode());
		result = prime * result + (BeanHelper.getAttributeValue(this, ATT_TS) == null ? 0
				: BeanHelper.getAttributeValue(this, ATT_TS).hashCode());
		result = prime * result + (BeanHelper.getAttributeValue(this, ATT_CREATE_TIME) == null ? 0
				: BeanHelper.getAttributeValue(this, ATT_CREATE_TIME).hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder(this.getClass().getName() + "{");
		buff.append(ATT_ID).append(":").append(BeanHelper.getAttributeValue(this, ATT_ID)).append(",");
		buff.append(ATT_DR).append(":").append(BeanHelper.getAttributeValue(this, ATT_DR)).append(",");
		buff.append(ATT_TS).append(":").append(BeanHelper.getAttributeValue(this, ATT_TS)).append(",");
		buff.append(ATT_CREATE_TIME).append(":").append(BeanHelper.getAttributeValue(this, ATT_CREATE_TIME))
				.append(",");
		return buff.deleteCharAt(buff.lastIndexOf(",")).append("}").toString();
	}
}
