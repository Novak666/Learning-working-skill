package com.itheima.dao;

import com.itheima.pojo.Words;
import com.itheima.utils.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

/**
 * 包名:com.itheima.dao
 *
 * @author Leevi
 * 日期2020-10-22  11:58
 */
public class WordsDao {
    private QueryRunner queryRunner = new QueryRunner(DruidUtil.getDataSource());
    public List<Words> search(String keyword) throws Exception {
        String sql = "select * from words where word like ?";
        List<Words> wordsList = queryRunner.query(sql, new BeanListHandler<>(Words.class), "%" + keyword + "%");
        return wordsList;
    }
}
