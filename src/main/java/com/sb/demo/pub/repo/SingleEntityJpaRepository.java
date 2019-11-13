package com.sb.demo.pub.repo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import com.sb.demo.pub.entity.SuperEntity;

/**
 * 单实体JpaRepository基类
 * 
 * <p>
 * 没有关联关系的单实体继承该类,可快速实现增删改查
 * </p>
 * 
 * @author xg
 * @date 2019-10-29
 */
@NoRepositoryBean
public interface SingleEntityJpaRepository<T extends SuperEntity> extends BaseJpaRepository<T> {

	/**
	 * 根据ID查询
	 */
	@Query("SELECT t FROM #{#entityName} t WHERE t.id = ?1 and t.dr = " + SuperEntity.IS_DELETE_NO)
	Optional<T> findById(Serializable id);

	/**
	 * 查询所有记录
	 *
	 * @return
	 */
	@Query("SELECT t FROM #{#entityName} t WHERE t.dr = " + SuperEntity.IS_DELETE_NO)
	List<T> findAll();

	/**
	 * 分页查询记录
	 *
	 * @param pageable
	 * @return
	 */
	@Query("SELECT t FROM #{#entityName} t WHERE t.dr = " + SuperEntity.IS_DELETE_NO)
	Page<T> findPage(Pageable pageable);

	/**
	 * 根据ID进行逻辑删除
	 */
	@Modifying
	@Query("UPDATE #{#entityName} t SET t.dr = " + SuperEntity.IS_DELETE_YES + " WHERE t.id = ?1")
	void deleteById(Serializable id);

	/**
	 * 逻辑删除所有数据
	 */
	@Modifying
	@Query("UPDATE #{#entityName} t SET t.dr = " + SuperEntity.IS_DELETE_YES)
	void deleteAll();
}
