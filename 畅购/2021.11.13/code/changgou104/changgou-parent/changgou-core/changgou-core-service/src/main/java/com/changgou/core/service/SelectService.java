package com.changgou.core.service;

import java.util.List;

/**
 * 描述
 *
 * @author www.itheima.com
 * @version 1.0
 * @package com.changgou.core *
 * @since 1.0
 */
public interface SelectService<T> {

    /**
     * 查询所有
     *
     * @return
     */
    public List<T> selectAll();

    /**
     * 查询一个对象
     *
     * @param id
     * @return
     */
    public T selectByPrimaryKey(Object id);

    /**
     * 根据条件查询
     *
     * @param record
     * @return
     */
    public List<T> select(T record);


}
