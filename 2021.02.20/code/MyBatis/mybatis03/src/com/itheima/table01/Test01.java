package com.itheima.table01;

import com.itheima.bean.Card;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class Test01 {
    @Test
    public void selectAll() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取OneToOneMapper接口的实现类对象
        OneToOneMapper mapper = sqlSession.getMapper(OneToOneMapper.class);

        //5.调用实现类的方法，接收结果
        List<Card> list = mapper.selectAll();

        //6.处理结果
        for (Card c : list) {
            System.out.println(c);
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
