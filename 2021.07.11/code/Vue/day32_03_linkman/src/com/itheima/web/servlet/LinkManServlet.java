package com.itheima.web.servlet;

import com.itheima.pojo.LinkMan;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.ResultBean;
import com.itheima.service.LinkManService;
import com.itheima.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Leevi
 * 日期2020-10-19  08:50
 */
@WebServlet("/linkman")
public class LinkManServlet extends BaseServlet {
    private LinkManService linkManService = new LinkManService();
    /**
     * 分页查询
     * @param request
     * @param response
     */
    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 获取请求参数
            Long currentPage = Long.valueOf(request.getParameter("currentPage"));
            Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));

            //2. 调用业务层的方法进行分页查询
            PageBean<LinkMan> pageBean = linkManService.findByPage(currentPage,pageSize);
            resultBean.setData(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            resultBean.setErrorMsg("分页查询失败");
        }

        //将ResultBean对象转换成json字符串输出给浏览器
        JsonUtils.printResult(response,resultBean);
    }

    /**
     * 修改联系人
     * @param request
     * @param response
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 获取客户端携带过来的联系人信息
            LinkMan linkMan = JsonUtils.parseJSON2Object(request, LinkMan.class);

            //3. 调用业务层的方法，修改联系人
            linkManService.update(linkMan);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            resultBean.setErrorMsg("修改失败");
        }
        //将ResultBean对象转换成json字符串输出给浏览器
        JsonUtils.printResult(response,resultBean);
    }

    /**
     * 根据id删除联系人
     * @param request
     * @param response
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1.获取到请求参数id
            Integer id = Integer.valueOf(request.getParameter("id"));
            //2. 调用业务层的方法根据id删除联系人
            linkManService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            resultBean.setErrorMsg("删除失败");
        }

        //将ResultBean对象转换成json字符串输出给浏览器
        JsonUtils.printResult(response,resultBean);
    }

    /**
     * 添加联系人
     * @param request
     * @param response
     */
    public void add(HttpServletRequest request,HttpServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 获取所有联系人信息，封装到LinkMan对象中
            LinkMan linkMan = JsonUtils.parseJSON2Object(request, LinkMan.class);

            //2. 调用业务层的方法，添加联系人
            linkManService.add(linkMan);

        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            resultBean.setErrorMsg("添加联系人失败");
        }

        //将ResultBean对象转换成json字符串输出给浏览器
        JsonUtils.printResult(response,resultBean);
    }

    /**
     * 查询单个联系人信息
     * @param request
     * @param response
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 获取到请求参数id的值
            Integer id = Integer.valueOf(request.getParameter("id"));

            //2. 调用业务层的方法根据id查询联系人
            LinkMan linkMan = linkManService.findById(id);

            resultBean.setData(linkMan);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            resultBean.setErrorMsg("数据回显失败");
        }

        //将ResultBean对象转换成json字符串，并且输出到浏览器
        JsonUtils.printResult(response,resultBean);
    }

    /**
     * 查询所有联系人
     * @param request
     * @param response
     */
    public void findAll(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 调用业务层方法查询所有联系人信息
            List<LinkMan> linkManList = linkManService.findAll();
            //2. 将linkManList存储到ResultBean对象
            resultBean.setData(linkManList);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            resultBean.setErrorMsg("查询所有联系人失败");
        }

        //将ResultBean对象转换成json字符串，并且输出到浏览器
        JsonUtils.printResult(response,resultBean);
    }
}
