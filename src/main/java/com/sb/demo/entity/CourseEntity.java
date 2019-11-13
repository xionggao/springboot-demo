package com.sb.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.sb.demo.pub.entity.SuperEntity;

@Entity
@Table(name = "bd_course")
@Where(clause = "dr=0")
public class CourseEntity extends SuperEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -672562991131116011L;

	@Column(length = 100)
	private String code;
	@Column
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
