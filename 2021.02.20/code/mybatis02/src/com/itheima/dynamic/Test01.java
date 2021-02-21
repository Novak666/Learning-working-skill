package com.itheima.dynamic;

import com.itheima.bean.Student;
import com.itheima.mapper.StudentMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Test01 {
    @Test
    public void selectByIds() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取StudentMapper接口的实现类对象
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);

        //5.调用实现类的方法，接收结果
        List<Student> list = mapper.selectByIds(ids);

        //6.处理结果
        for (Student student : list) {
            System.out.println(student);
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }

    @Test
    public void selectCondition() throws Exception{
        //1.加载核心配置文件
        InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");

        //2.获取SqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        //3.通过工厂对象获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //4.获取StudentMapper接口的实现类对象
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

        Student stu = new Student();
        stu.setId(2);
        stu.setName("李四");
        //stu.setAge(24);

        //5.调用实现类的方法，接收结果
        List<Student> list = mapper.selectCondition(stu);

        //6.处理结果
        for (Student student : list) {
            System.out.println(student);
        }

        //7.释放资源
        sqlSession.close();
        is.close();
    }
}
