package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/29
 **/
@FeignClient(name="goods",path="/sku")
public interface SkuFeign {
    /**
     * 根据状态值查询符合条件的sku的数据列表
     * @param status  0(不正常)  1 (正常)
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable(name = "status")String status);

    //根据sku的ID 获取sku的数据
    @GetMapping(value = "/{id}")
    public Result<Sku> findById(@PathVariable(value = "id") Long id);
}
