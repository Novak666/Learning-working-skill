package com.changgou.canal.listenser;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.xpand.starter.canal.annotation.*;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/14
 **/
@CanalEventListener
public class CanalDataEventListener {
    /***
     * 增加数据监听
     * @param eventType
     * @param rowData
     */
//    @InsertListenPoint
//    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        rowData.getAfterColumnsList().forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
//    }

    /***
     * 修改数据监听
     * @param rowData
     */
//    @UpdateListenPoint
//    public void onEventUpdate(CanalEntry.RowData rowData) {
//        System.out.println("UpdateListenPoint");
//        rowData.getBeforeColumnsList().forEach((c) -> System.out.println("Before: " + c.getName() + " ::   " + c.getValue()));
//        rowData.getAfterColumnsList().forEach((c) -> System.out.println("After: " + c.getName() + " ::   " + c.getValue()));
//    }

    /***
     * 删除数据监听
     * @param eventType
     */
//    @DeleteListenPoint
//    public void onEventDelete(CanalEntry.EventType eventType) {
//        System.out.println("DeleteListenPoint");
//    }

    /**
     *
     *
     * 当 数据库changgou_content 下的tb_content发生了变化
     * 监听到 被修改 或者被删除 或者 被新增的那行数据对应的category_id列名的值
     * 调用广告微服务 实现查询广告分类ID 对应的广告列表数据
     * 将广告列表数据 存储到redis中
     * @param eventType
     * @param rowData
     */

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // destination 指定的是目的地 和canal-server中的服务器中的example目录一致
    // schema 指定要监听的数据库的库名
    // table 指定要监听的表名
    // eventType 指定事件类型（当发生了insert delete update的时候处理）
    @ListenPoint(destination = "example",
            schema = "changgou_content",
            table = {"tb_content", "tb_content_category"},
            eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE, CanalEntry.EventType.INSERT}
    )
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //1.获取category_id的值
        String categoryId = getColumnValue(eventType, rowData);
        //2.通过feign调用广告微服务（根据分类的ID获取该分类下的所有的广告列表数据）
            //2.1 在 changgou-service-content-api中添加起步依赖 //ctr + f11
            //2.2 创建一个接口 定义方法 添加注解@feignclient注解
            //2.3 在content微服务中实现 业务接口--》controller service dao
            //2.4 在changgou-service-canal 启用Enablefeignclients
            //2.5 注入一个接口即可
        Result<List<Content>> feign = contentFeign.findByCategory(Long.valueOf(categoryId));
        List<Content> contentList = feign.getData();
        //3.将数据存储到redis中
            //3.1 加入redis的起步依赖spring boot data redis starter
            //3.2 配置redis的链接到的服务端的ip和端口
            //3.3 注入StringRedisTemplate
            //3.3 执行set key value
        stringRedisTemplate.boundValueOps("content_"+categoryId).set(JSON.toJSONString(contentList));
    }

    //获取category_id的值
    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = "";
        if (eventType == CanalEntry.EventType.DELETE) {
            //1.如果是delete 获取before的数据
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                if (column.getName().equals("category_id")) {
                    categoryId = column.getValue();
                    break;
                }
            }
        } else {
            //2.判断如果是insert 和 update 那么获取after的数据
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            for (CanalEntry.Column column : afterColumnsList) {
                if (column.getName().equals("category_id")) {
                    categoryId = column.getValue();
                    break;
                }
            }
        }

        //3.获取行中的category_id的值 返回
        return categoryId;
    }
}
