package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController extends AbstractCoreController<Sku>{

    private SkuService  skuService;

    @Autowired
    public SkuController(SkuService  skuService) {
        super(skuService, Sku.class);
        this.skuService = skuService;
    }

    /**
     * feign专用 根据状态获取SKU的列表数据
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable(name = "status")String status) {
        //select * from tb_sku where status=?
        List<Sku> skuList =  skuService.findByStatus(status);
        return new Result<List<Sku>>(true, StatusCode.OK,"查询条件成功",skuList);
    };

    @GetMapping("/decCount")
    public Result decCount(@RequestParam(name="id") Long id, @RequestParam(name="num") Integer num){
        //受影响的行
        Integer count = skuService.decCount(id,num);
        if(count>0){
            return new Result(true,StatusCode.OK,"扣减成功");
        }else{
            return new Result(false,StatusCode.ERROR,"扣减失败");
        }
    }
}
