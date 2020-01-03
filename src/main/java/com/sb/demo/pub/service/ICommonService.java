package com.sb.demo.pub.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.sb.demo.pub.exception.BusinessException;

/**
 * 通用服务接口
 *
 * @author xg
 */
public interface ICommonService<T> {

    int DEFAULT_PAGE_NUMBER = 0; // 默认的分页页码
    int DEFAULT_PAGE_SIZE = 10; // 默认的分页条数

    /**
     * 根据ID返回实体对象
     *
     * @param id 实体主键
     * @return 实体对象
     * @throws BusinessException 业务异常
     */
    public T findById(Serializable id) throws BusinessException;

    /**
     * 返回所有实体对象
     *
     * @return
     * @throws BusinessException
     */
    public List<T> findAll(Map<String, Object> condition) throws BusinessException;

    /**
     * 分页查询
     *
     * @param page
     * @return
     * @throws BusinessException
     */
    public Page<T> findPage(Pageable page, Map<String, Object> condition) throws BusinessException;

    /**
     * 新增或修改实体
     *
     * @param t
     * @return
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    public T saveOrUpdate(T t) throws BusinessException;

    /**
     * 根据ID逻辑删除实体
     *
     * @param id
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    default void deleteById(Serializable id) throws BusinessException {
        Objects.requireNonNull(id);
        this.deleteByIds(new Serializable[]{id});
    }

    /**
     * 根据ID逻辑删除实体(批量)
     *
     * @param ids
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Serializable... ids) throws BusinessException;

    /**
     * 逻辑删除实体
     *
     * @param t
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    default void delete(T t) throws BusinessException {
        Objects.requireNonNull(t);
        List<T> list = new ArrayList<T>(1);
        list.add(t);
        this.deleteAll(list);
    }

    /**
     * 逻辑删除实体(批量)
     *
     * @param ts
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Collection<T> ts) throws BusinessException;

    /**
     * 逻辑删除实体
     *
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() throws BusinessException;
}
