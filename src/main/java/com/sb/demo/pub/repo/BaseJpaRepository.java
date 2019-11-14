package com.sb.demo.pub.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * JpaRepository基类
 *
 * @param <T> 待持久化的实体对象类型
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
