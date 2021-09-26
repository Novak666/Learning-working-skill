package com.changgou.goods.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/09/26
 **/
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌的数据
     * @return
     */
    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> brandList = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询品牌列表成功", brandList);
    }

    /**
     * 根据Id查询商品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable(value = "id") Integer id) {
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "根据Id查询商品成功", brand);
    }

    /**
     * 添加品牌数据
     * @param brand
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 根据id更新商品
     * @param id
     * @param brand
     * @return
     */
    @PutMapping("/{id}")
    public Result updateById(@PathVariable(value = "id") Integer id, @RequestBody Brand brand) {
        brand.setId(id);
        brandService.updateById(brand);
        return new Result(true, StatusCode.OK, "更新成功");
    }

    /**
     * 根据id删除商品
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable(value = "id") Integer id) {
        brandService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 条件查询商品
     * @param brand
     * @return
     */
    @PostMapping("/search")
    public Result<List<Brand>> search(@RequestBody(required = false) Brand brand) {
        List<Brand> brandList = brandService.search(brand);
        return new Result(true, StatusCode.OK, "条件查询成功", brandList);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findByPage(@PathVariable(value = "page") Integer page,
                                              @PathVariable(value = "size") Integer size) {
        PageInfo<Brand> byPage = brandService.findByPage(page, size);
        return new Result(true,StatusCode.OK,"分页查询成功", byPage);
    }

    /**
     * 条件分页查询
     * @param page
     * @param size
     * @param brand
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findByPage(@PathVariable(value = "page") Integer page,
                                              @PathVariable(value = "size") Integer size,
                                              @RequestBody Brand brand) {
        PageInfo<Brand> byPage = brandService.findByPage(page, size, brand);
        int i = 1/0;
        return new Result(true,StatusCode.OK,"条件分页查询成功", byPage);
    }

}
