package com.changgou.content.feign;

import com.changgou.content.pojo.Content;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/19
 **/
@FeignClient(name="content",path = "/content")
// path = "/content"相当于@requestMapping
//@RequestMapping("/content")
public interface ContentFeign {

    //根据广告分类的ID 获取广告分类下的所有的广告列表数据
    @GetMapping("/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable(name="id") Long id);

}
