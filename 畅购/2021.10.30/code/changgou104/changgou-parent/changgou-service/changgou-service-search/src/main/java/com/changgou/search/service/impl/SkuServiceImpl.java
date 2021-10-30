package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import entity.Result;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/29
 **/
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ElasticsearchTemplate esTemplate;//核心操作es的类

    @Override
    public void importSku() {
        //1.调用feign 查询商品微服务的符合条件的sku的数据集合
        //1.1在changgou-service-goods-api中创建一个接口  业务接口
        //1.2定义方法 和添加注解  方法：根据状态进行查询符合条件的sku的数据列表
        //1.3在changgou-service-goods微服务实现业务接口（编写controller service dao ）
        //1.4添加changgou-service-goods-pai的依赖，启动类中启用feignclients
        //1.5注入接口 指定调用
        Result<List<Sku>> result = skuFeign.findByStatus("1");
        List<Sku> skuList = result.getData();
        //先将POJO 转成JSON  再讲JSON 转回POJO 类型不同
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuList), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            //获取规格的数据 {"电视音响效果":"小影院","电视屏幕尺寸":"60英寸","尺码":"165"}
            String spec = skuInfo.getSpec();
            //转成MAP
            Map<String, Object> map = JSON.parseObject(spec, Map.class);
            //设置到skuinfo中的specMap属性中
            skuInfo.setSpecMap(map);
        }
        //2.将数据存储到es服务器中
        skuEsMapper.saveAll(skuInfoList);
    }

    //先只做 根据关键字来搜索

    /**
     * @param searchMap {"keywords":"手机"}
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //1.获取关键字的值
        String keywords = searchMap.get("keywords");
        if(StringUtils.isEmpty(keywords)){
            //轮播效果方法 随机产生一个数据
            keywords="华为";
        }
        //2.创建查询对象的 构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //3.设置条件  设置查询条件  匹配查询 match
        //参数1 指定 要搜索的字段
        //参数2 指定 要搜索的内容
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name",keywords));//相当于select * from tb_sku where name like '%?%'

        //4.构建查询对象
        SearchQuery query = nativeSearchQueryBuilder.build();
        //5.执行查询

        //参数1 指定查询对象（所有的查询的条件封装在这）
        //参数2 指定返回的数据类型（字节码对象）
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class);
        //6.获取结果 封装返回
        //6.1 获取当前页的记录
        List<SkuInfo> content = skuPage.getContent();
        //6.2 获取根据条件命中的总记录数
        long totalElements = skuPage.getTotalElements();
        //6.3 获取总页数
        int totalPages = skuPage.getTotalPages();

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rows",content);
        resultMap.put("total",totalElements);
        resultMap.put("totalPages",totalPages);
        return resultMap;
    }
}
