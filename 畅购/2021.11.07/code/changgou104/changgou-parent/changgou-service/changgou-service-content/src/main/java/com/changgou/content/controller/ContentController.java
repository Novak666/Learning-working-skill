package com.changgou.content.controller;

import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import com.changgou.core.AbstractCoreController;
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
@RequestMapping("/content")
@CrossOrigin
public class ContentController extends AbstractCoreController<Content>{

    private ContentService  contentService;

    @Autowired
    public ContentController(ContentService  contentService) {
        super(contentService, Content.class);
        this.contentService = contentService;
    }

    //实现接口 根据广告分类的ID 获取广告的列表
    // select * from tb_content where category_id = ?
    @GetMapping("/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable(name="id") Long id){
        Content conditon = new Content();
        conditon.setCategoryId(id);
        List<Content> contentList = contentService.select(conditon);
        return new Result<List<Content>>(true, StatusCode.OK,"查询成功",contentList);
    }

}
