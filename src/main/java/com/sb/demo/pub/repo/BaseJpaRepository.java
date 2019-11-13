package com.sb.demo.pub.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * JpaRepository基类
 * 
 * @param <T>
 *            实体对象
 * @param <Serializable>
 *            实体主键类型
 * 
 * @see RelationEntityJpaDao
 * 
 * @author xg
 * @date 2019-10-22
 */
@NoRepositoryBean
public interface BaseJpaRepository<T> extends JpaRepository<T, Serializable>, JpaSpecificationExecutor<T> {

	/**
	 * 根据主键批量删除
	 * 
	 * @param ids
	 */
	void deleteByIds(Serializable... ids);
}
