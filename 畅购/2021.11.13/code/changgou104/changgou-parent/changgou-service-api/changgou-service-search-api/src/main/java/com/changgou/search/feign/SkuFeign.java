package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/05 16:01
 **/
@FeignClient(name="search",path = "/search")
public interface SkuFeign {
    //根据搜索条件 获取搜索的结果
    @PostMapping
    public Map<String,Object> search(@RequestBody(required = false) Map<String,String> searchMap);
}
