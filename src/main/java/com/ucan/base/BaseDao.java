package com.ucan.base;

import java.util.List;

/**
 * DAO层通用接口
 * 
 * @author liming.cen
 * @date 2022年12月23日 下午7:51:33
 */
public interface BaseDao<T> {
    /**
     * 新增一条记录
     * 
     * @param obj
     * @return
     */
    int insert(T obj);

    /**
     * 批量新增记录
     * 
     * @param list
     * @return
     */
    int insertBatch(List<T> list);

    /**
     * 批量删除记录
     * 
     * @param list
     * @return
     */
    int deleteBatch(List<T> list);

    /**
     * 删除记录
     * 
     * @param obj
     * @return
     */
    int delete(T obj);

    /**
     * 删除记录
     * 
     * @param id
     * @return
     */
    int deleteById(String id);

    /**
     * 批量删除记录
     * 
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);

    /**
     * 更新记录
     * 
     * @param obj
     * @return
     */
    int update(T obj);

    /**
     * 按id查询记录
     * 
     * @param id
     * @return
     */
    T queryById(String id);

    /**
     * 按名称查询记录
     * 
     * @param name
     * @return
     */
    T queryByName(String name);

}
