package com.itheima.dao;

import com.itheima.pojo.LinkMan;
import com.itheima.utils.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.List;

/**
 * 包名:com.itheima.dao
 *
 * @author Leevi
 * 日期2020-10-19  08:56
 */
public class LinkManDao {
    private QueryRunner queryRunner = new QueryRunner(DruidUtil.getDataSource());
    public List<LinkMan> findAll() throws Exception {
        String sql = "select * from linkman";
        List<LinkMan> linkManList = queryRunner.query(sql, new BeanListHandler<>(LinkMan.class));
        return linkManList;
    }

    public void add(LinkMan linkMan) throws Exception {
        String sql = "insert into linkman values (null,?,?,?,?,?,?)";
        queryRunner.update(sql,linkMan.getName(),linkMan.getSex(),linkMan.getAge(),linkMan.getAddress(),linkMan.getQq(),linkMan.getEmail());
    }

    public void deleteById(Integer id) throws Exception {
        String sql = "delete from linkman where id=?";
        queryRunner.update(sql,id);
    }

    public LinkMan findById(Integer id) throws Exception {
        String sql = "select * from linkman where id=?";
        LinkMan linkMan = queryRunner.query(sql, new BeanHandler<>(LinkMan.class), id);
        return linkMan;
    }

    public void update(LinkMan linkMan) throws Exception {
        String sql = "update linkman set name=?,sex=?,age=?,address=?,qq=?,email=? where id=?";
        queryRunner.update(sql,linkMan.getName(),linkMan.getSex(),linkMan.getAge(),linkMan.getAddress(),linkMan.getQq(),linkMan.getEmail(),linkMan.getId());
    }

    public Long findTotalCount() throws Exception {
        String sql = "select count(*) from linkman";
        long totalSize = (long) queryRunner.query(sql,new ScalarHandler());
        return totalSize;
    }

    public List<LinkMan> findPageList(Long currentPage, Integer pageSize) throws Exception {
        String sql = "select * from linkman limit ?,?";
        List<LinkMan> linkManList = queryRunner.query(sql, new BeanListHandler<>(LinkMan.class), (currentPage - 1) * pageSize, pageSize);
        return linkManList;
    }
}
