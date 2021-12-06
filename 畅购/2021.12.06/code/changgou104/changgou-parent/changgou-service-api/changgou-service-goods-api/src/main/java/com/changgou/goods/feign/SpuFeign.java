package com.changgou.goods.feign;

import com.changgou.goods.pojo.Spu;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/19 17:05
 **/
@FeignClient(name="goods",path = "/spu")
public interface SpuFeign {
    /**
     * 根据spu的ID 获取spu的数据
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result<Spu> findById(@PathVariable(value = "id") Long id);
}
