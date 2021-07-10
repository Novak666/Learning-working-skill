package com.itheima.service;

import com.itheima.dao.WordsDao;
import com.itheima.pojo.Words;

import java.util.List;

/**
 * 包名:com.itheima.service
 *
 * @author Leevi
 * 日期2020-10-22  11:58
 */
public class WordsService {
    private WordsDao wordsDao = new WordsDao();
    public List<Words> search(String keyword) throws Exception {
        return wordsDao.search(keyword);
    }
}
