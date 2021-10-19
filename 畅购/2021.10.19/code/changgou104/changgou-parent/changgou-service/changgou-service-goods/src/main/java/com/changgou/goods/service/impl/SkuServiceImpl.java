package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/****
 * @Author:admin
 * @Description:Sku业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SkuServiceImpl extends CoreServiceImpl<Sku> implements SkuService {

    private SkuMapper skuMapper;

    @Autowired
    public SkuServiceImpl(SkuMapper skuMapper) {
        super(skuMapper, Sku.class);
        this.skuMapper = skuMapper;
    }
}
