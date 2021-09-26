package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.Page;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/09/26
 **/
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    @Override
    public void updateById(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void deleteById(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Brand> search(Brand brand) {
        Example example = createExample(brand);
        //3.执行条件查询
        //select * from tb_brand
        List<Brand> brandList = brandMapper.selectByExample(example);

        //4.返回结果
        return brandList;
    }

    private Example createExample(Brand brand) {
        Example example = new Example(Brand.class);
        //1.判断是否为空
        if(brand!=null){
            //2.根据不同的条件组装条件对象example
            Example.Criteria criteria = example.createCriteria();

            if(!StringUtils.isEmpty(brand.getName())) {
                //参数1 指定要查询的条件的POJO的属性名
                //参数2 指定查询的条件的属性对应的值
                criteria.andLike("name", "%"+brand.getName()+"%");// where  name like '%heima%'
            }
            if(!StringUtils.isEmpty(brand.getLetter())){
                criteria.andEqualTo("letter",brand.getLetter());// and letter='H'
            }
        }
        return example;
    }



    @Override
    public PageInfo<Brand> findByPage(Integer page, Integer size) {
        //1.开始分页
        PageHelper.startPage(page, size);
        //2.执行查询的sql (查询所有)
        List<Brand> brandList = brandMapper.selectAll();
        //3.封装到pageInfo对象中
        return new PageInfo<>(brandList);
    }

    @Override
    public PageInfo<Brand> findByPage(Integer page, Integer size, Brand brand) {
        //1.开始分页
        PageHelper.startPage(page, size);

        Example example = createExample(brand);
        //3.执行条件查询
        //select * from tb_brand
        List<Brand> brandList = brandMapper.selectByExample(example);
        //3.封装到pageInfo对象中
        return new PageInfo<>(brandList);
    }
}
