package com.itheima.service;

import com.itheima.dao.LinkManDao;
import com.itheima.pojo.LinkMan;
import com.itheima.pojo.PageBean;

import java.util.List;

/**
 * 包名:com.itheima.service
 *
 * @author Leevi
 * 日期2020-10-19  08:52
 */
public class LinkManService {
    private LinkManDao linkManDao = new LinkManDao();
    public List<LinkMan> findAll() throws Exception {
        return linkManDao.findAll();
    }

    public void add(LinkMan linkMan) throws Exception {
        //调用dao层的方法添加联系人
        linkManDao.add(linkMan);
    }

    public void deleteById(Integer id) throws Exception {
        linkManDao.deleteById(id);
    }

    public LinkMan findById(Integer id) throws Exception {
        return linkManDao.findById(id);
    }

    public void update(LinkMan linkMan) throws Exception {
        linkManDao.update(linkMan);
    }

    public PageBean findByPage(Long currentPage, Integer pageSize) throws Exception {
        //目标: 创建一个PageBean对象，设置好5个属性
        PageBean<LinkMan> pageBean = new PageBean();
        //1. 设置当前页数
        pageBean.setCurrentPage(currentPage);
        //2. 设置每页条数
        pageBean.setPageSize(pageSize);
        //3. 调用dao层的方法查询总条数
        Long totalSize = linkManDao.findTotalCount();
        //将总条数设置到pageBean中
        pageBean.setTotalSize(totalSize);

        //4. 计算出总页数
        Long totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        //将总页数设置到pageBean
        pageBean.setTotalPage(totalPage);

        //5. 调用dao层的方法查询到当前页的联系人集合
        List<LinkMan> linkManList = linkManDao.findPageList(currentPage,pageSize);
        //将linkManList设置到PageBean对象中
        pageBean.setList(linkManList);

        return pageBean;
    }
}
