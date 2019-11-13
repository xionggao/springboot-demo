package com.sb.demo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.sb.demo.pub.entity.SuperEntity;

@Entity
@Table(name = "bd_student")
@Where(clause = "dr=0")
public class StudentEntity extends SuperEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7896380907145798742L;

	@Column(length = 100)
	private String code;

	@Column(length = 200)
	private String name;

	@Transient
	@Column(length = 255)
	private String memo;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "student_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private List<CourseEntity> courses = new ArrayList<CourseEntity>();
	
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<CourseEntity> getCourses() {
		return courses;
	}

	public void setCourses(List<CourseEntity> courses) {
		this.courses = courses;
	}
}
