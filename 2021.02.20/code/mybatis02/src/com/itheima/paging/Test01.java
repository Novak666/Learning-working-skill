package com.itheima.paging;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.bean.Student;
import com.itheima.mapper.StudentMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class Test01 {
    @Test
    public void selectPaging() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取StudentMapper接口的实现类对象
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

        //通过分页助手来实现分页功能
        // 第一页：显示3条数据
        //PageHelper.startPage(1,3);
        // 第二页：显示3条数据
        //PageHelper.startPage(2,3);
        // 第三页：显示3条数据
        PageHelper.startPage(3,3);

        //5.调用实现类的方法，接收结果
        List<Student> list = mapper.selectAll();

        //6.处理结果
        for (Student student : list) {
            System.out.println(student);
        }

        //获取分页相关参数
        PageInfo<Student> info = new PageInfo<>(list);
        System.out.println("总条数：" + info.getTotal());
        System.out.println("总页数：" + info.getPages());
        System.out.println("当前页：" + info.getPageNum());
        System.out.println("每页显示条数：" + info.getPageSize());
        System.out.println("上一页：" + info.getPrePage());
        System.out.println("下一页：" + info.getNextPage());
        System.out.println("是否是第一页：" + info.isIsFirstPage());
        System.out.println("是否是最后一页：" + info.isIsLastPage());

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
