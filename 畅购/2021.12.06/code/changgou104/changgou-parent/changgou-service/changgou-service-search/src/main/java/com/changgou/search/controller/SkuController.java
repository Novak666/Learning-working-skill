package com.changgou.search.controller;

import com.changgou.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/29
 **/
@RestController
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuService skuService;

    //从数据库查询出来再导入数据到ES中
    @GetMapping("/import")
    public Result importToES() {

        skuService.importSku();

        return new Result(true, StatusCode.OK, "导入数据至ES成功");

    }

    /**
     * 根据条件查询数据
     * @param stringMap 搜索的条件封装对象 包含要搜索的关键字，品牌名，规格选项，分类.....
     * @return 封装的数据对象 map 里面包含（当前页的记录 总页数，总记录数......）
     */
    @PostMapping
    public Map<String, Object> search(@RequestBody(required = false) Map<String, String> stringMap) {
        if (stringMap == null) {
            stringMap = new HashMap<>();
        }
        return skuService.search(stringMap);
    }

}
