package com.base.wang.service.base;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fuxx on 15/10/9.
 */
@Service
public interface BaseService<T> {

    T selectByKey(Object key);
    
    T selectOne(T entity);

    int save(T entity);

    int delete(Object key);

    int updateAll(T entity);

    int updateNotNull(T entity);

    List<T> selectPage(int pageNum, int pageSize);

    List<T> selectPage(int pageNum, int pageSize, T entity);
    
}