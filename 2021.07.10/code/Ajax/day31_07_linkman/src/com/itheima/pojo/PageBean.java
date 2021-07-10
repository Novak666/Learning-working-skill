package com.itheima.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 包名:com.itheima.pojo
 * @author Leevi
 * 日期2020-10-19  10:57
 */
public class PageBean<T> implements Serializable {
    private List<T> list;
    /**
     * 总数据条数
     */
    private Long totalSize;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 每页数据条数
     */
    private Integer pageSize;
    /**
     * 当前页数
     */
    private Long currentPage;


    @Override
    public String toString() {
        return "PageBean{" +
                "list=" + list +
                ", totalSize=" + totalSize +
                ", totalPage=" + totalPage +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                '}';
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }
}
