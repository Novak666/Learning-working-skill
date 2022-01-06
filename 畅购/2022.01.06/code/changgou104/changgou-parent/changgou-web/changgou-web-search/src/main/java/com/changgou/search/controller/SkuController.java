package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * controller 接收用户传递的请求 执行搜索获取数据，设置数据到model中返回给页面渲染给用户
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/05 15:32
 **/
@Controller
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    @GetMapping("/list")
    public String search(@RequestParam Map<String,String> searchMap, Model model) {
        //1.接收页面传递过来的搜索的条件
        //2.调用feign 获取search微服务中的搜索到的数据
        //2.1 在changgou-service-search-api中创建一个feign接口
        //2.2 编写方法 （搜索数据）
        //2.3 在changgou-service-search微服务中'实现' 业务接口
        //2.4 加入依赖 启用feigclients
        //2.5 注入接口 使用
        Map<String, Object> resultMap = skuFeign.search(searchMap);
        //3.将数据 设置到model中
        model.addAttribute("result",resultMap);

        //4.回显数据  searchMap={"keywords":"小米"}
        model.addAttribute("searchMap",searchMap);

        //6.拼接url 设置到model (最新的（上一次的）请求路径)
        // /search/list?keywords=华为&brand=huawei&category=笔记本
        String url = url(searchMap);
        model.addAttribute("url",url);

        //分页数据封装
        Page page = new Page(
                //总记录数
                Long.valueOf(resultMap.get("total").toString()),
                //当前页码
                Integer.valueOf(resultMap.get("pageNum").toString()),
                //每页显示的行
                Integer.valueOf(resultMap.get("pageSize").toString())
        );
        model.addAttribute("page",page);
        return "search";
    }

    private String url(Map<String, String> searchMap) {
        String url = "/search/list";
        if(searchMap!=null){
            //   /search/list?keywords=华为&brand=huawei&category=笔记本&
            url+="?";
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                String key = stringStringEntry.getKey();//keywords,brand,category
                String value = stringStringEntry.getValue();// 华为,huawei,笔记本
                //特殊的key 不需要拼接
                if(key.equals("sortField") || key.equals("sortRule") || key.equals("pageNum")){
                    continue;
                }
                url+=key+"="+value+"&";
            }
            url = url.substring(0,url.length()-1);

        }
        return url;
    }
}
