package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Para业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class ParaServiceImpl extends CoreServiceImpl<Para> implements ParaService {

    private ParaMapper paraMapper;

    @Autowired
    public ParaServiceImpl(ParaMapper paraMapper) {
        super(paraMapper, Para.class);
        this.paraMapper = paraMapper;
    }

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Para> findByCategoryId(Integer id) {
        //1.根据商品分类获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(id);
        //2.获取分类对象中的模板的ID
        Integer templateId = category.getTemplateId();
        //3.根据模板的ID获取参数列表数据 select * from tb_para where template_id=?
        Para condition = new Para();
        condition.setTemplateId(templateId);//where template_id=?  等号条件只能是等号
        return paraMapper.select(condition);//select * from tb_para
    }
}
