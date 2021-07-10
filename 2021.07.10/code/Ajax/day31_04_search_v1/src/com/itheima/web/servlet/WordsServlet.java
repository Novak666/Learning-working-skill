package com.itheima.web.servlet;

import com.alibaba.fastjson.JSON;
import com.itheima.pojo.ResultBean;
import com.itheima.pojo.Words;
import com.itheima.service.WordsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Leevi
 * 日期2020-10-22  11:58
 */
@WebServlet("/words")
public class WordsServlet extends BaseServlet {
    private WordsService wordsService = new WordsService();
    /**
     * 搜索提示
     * @param request
     * @param response
     */
    public void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 获取请求参数keyword的值
            String keyword = request.getParameter("keyword");
            //2. 调用业务层的方法，根据搜索关键字进行搜索
            List<Words> wordsList = wordsService.search(keyword);
            //3. 将搜索到的内容封装到ResultBean对象中
            resultBean.setData(wordsList);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器出现异常
            resultBean.setFlag(false);
            resultBean.setErrorMsg("查询失败");
        }

        //将resultBean转换成json字符串响应给客户端
        String jsonStr = JSON.toJSONString(resultBean);

        response.getWriter().write(jsonStr);
    }
}
