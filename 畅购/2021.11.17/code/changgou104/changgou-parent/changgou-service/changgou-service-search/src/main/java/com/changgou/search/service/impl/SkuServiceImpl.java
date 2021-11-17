package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchResultMapperImpl;
import com.changgou.search.service.SkuService;
import entity.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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

        //2.1 设置分组查询条件   商品分类的分组
        //AggregationBuilders.terms("skuCategorygroup") 表示设置分组条件 设置分组的别名skuCategorygroup
        // .field("categoryName") 表示分组的字段 设置为 categoryName
        //.size(10000) 表示桶的容量
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("categoryName").size(10000));

        //2.2 设置分组查询条件   商品的品牌的分组
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brandName").size(10000));

        //2.3 设置分组查询条件   规格的分组
        // 字段设置为：spec.keyword   当搜索的时候实现分组查询的时候是不需要进行分词的，
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(100000));

        //设置高亮 1. 设置高亮的字段  2. 设置前缀 和 后缀

        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<span style=\"color:red\">").postTags("</span>"));

        //3.设置条件  设置查询条件  匹配查询 match 从多个字段中搜索 比如：从brandName 上或者从categoryName 上或者从name上
        //参数1 指定 要搜索的字段
        //参数2 指定 要搜索的内容
        //nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name",keywords));//相当于select * from tb_sku where name like '%?%'

        //参数1 指定要搜索的内容
        //参数2 指定要从哪一些字段上进行搜索  关系 OR
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"name","categoryName","brandName"));


        //参数1 指定要搜索的内容
        //参数2 指定要从哪一些字段上进行搜索  关系 OR
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"name","categoryName","brandName"));

        //3.1 设置过滤查询(多个条件组合)
        // bool查询 添加商品分类的过滤查询 1.创建bool查询对象 2.创建xxx过滤查询对象（商品分类） 3.将条件设置bool查询对象（Must should,must_not,filter）
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //3.1.1 商品分类过滤
        String category = searchMap.get("category");
        if(!StringUtils.isEmpty(category)) {
            //must
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", category));
        }

        //3.1.2 品牌过滤
        String brand = searchMap.get("brand");
        if(!StringUtils.isEmpty(brand)) {
            //must
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", brand));
        }

        //3.1.3 规格过滤  1获取 参数 2.循环遍历 3.判断以spec_开头的就将这些数据进行 拼接 过滤查询
        //searchMap:{"keywords":"华为","category":"你点击的分类的值","brand":"你点击到的品牌的名称","spec_网络制式":"电信2G"}
        for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
            String key = stringStringEntry.getKey();//spec_网络制式
            String value = stringStringEntry.getValue();//电信2G
            if(key.startsWith("spec_")){
                boolQueryBuilder.filter(QueryBuilders.termQuery("specMap."+key.substring(5)+".keyword", value));
            }
        }

        //3.1.4 价格区间的过滤查询（范围）
        String price = searchMap.get("price");
        if(!StringUtils.isEmpty(price)) {
            String[] split = price.split("-");
            if(split[1].equals("*")){//大于等3000
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0],true).to(split[1],true));
            }
        }

        //设置
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

        //3.2 接收页面传递过来的页码值，设置分页查询 设置分页对象 指定当前的页码和每页显示的行

        String pageNumString = searchMap.get("pageNum");
        Integer pageNum=1;
        if(!StringUtils.isEmpty(pageNumString)){
            pageNum = Integer.parseInt(pageNumString);
        }
        Integer pageSize = 40;
        //参数1 指定当前的页码值 0 标识第一页
        //参数2 指定每页显示的行
        Pageable pageble = PageRequest.of(pageNum-1,pageSize);

        nativeSearchQueryBuilder.withPageable(pageble);

        //3.3 排序 排序的类型 和排序的字段 类似于order by column desc/asc
        //接收页面传递参数 获取字段和类型
        String sortField = searchMap.get("sortField");
        String sortRule = searchMap.get("sortRule");//大写的 DESC /ASC
        if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
            //SortBuilders.fieldSort("price") 设置排序的字段
            // order(SortOrder.DESC) 设置排序的类型
            //nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equals("DESC")?SortOrder.DESC:SortOrder.ASC));
            //sortRule.tolo
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }

        //4.构建查询对象
        SearchQuery query = nativeSearchQueryBuilder.build();
        //5.执行查询

        //参数1 指定查询对象（所有的查询的条件封装在这）
        //参数2 指定返回的数据类型（字节码对象）
        //参数3 指定searchresultmapp(自定义的)
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class, new SearchResultMapperImpl());

        //6.获取结果 封装返回
        //6.1 获取当前页的记录
        List<SkuInfo> content = skuPage.getContent();
        //6.2 获取根据条件命中的总记录数
        long totalElements = skuPage.getTotalElements();
        //6.3 获取总页数
        int totalPages = skuPage.getTotalPages();

        //6.4 获取分组的结果 商品分类的分组结果
        StringTerms stringTerms  = (StringTerms) skuPage.getAggregation("skuCategorygroup");

        List<String> categoryList =getStringsCategoryList(stringTerms);
        //6.5 获取分组的结果 商品品牌的分组结果
        StringTerms brandstringTerms  = (StringTerms) skuPage.getAggregation("skuBrandgroup");
        List<String> brandList = getStringsBrandList(brandstringTerms);

        //6.6 获取分组的结果 商品的规格的分组结果
        StringTerms stringTermsSpec  = (StringTerms) skuPage.getAggregation("skuSpecgroup");
        //解析规格的数据 返回map对象
        Map<String, Set<String>> specMap = getStringSetMap(stringTermsSpec);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rows",content);
        resultMap.put("total",totalElements);
        resultMap.put("totalPages",totalPages);
        //商品分类的列表
        resultMap.put("categoryList",categoryList);
        //商品的品牌的列表
        resultMap.put("brandList",brandList);
        //返回map 规格数据
        resultMap.put("specMap",specMap);

        resultMap.put("pageNum",pageNum);
        resultMap.put("pageSize",pageSize);

        return resultMap;
    }

    private List<String> getStringsBrandList(StringTerms brandstringTerms) {
        List<String> brandList = new ArrayList<>();
        if (brandstringTerms != null) {
            for (StringTerms.Bucket bucket : brandstringTerms.getBuckets()) {
                brandList.add(bucket.getKeyAsString());
            }
        }
        return brandList;
    }

    private List<String> getStringsCategoryList(StringTerms stringTerms) {
        List<String> categoryList = new ArrayList<>();
        if (stringTerms != null) {
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();//分组的值
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }

    //一顿操作
    private Map<String, Set<String>> getStringSetMap(StringTerms stringTermsSpec) {
        Map<String,Set<String>> specMap = new HashMap<>();
        if(stringTermsSpec!=null){

            Set<String> values = new HashSet<String>();
            for (StringTerms.Bucket bucket : stringTermsSpec.getBuckets()) {
                //{"手机屏幕尺寸":"5.5寸","网络":"电信4G","颜色":"白","测试":"s11","机身内存":"128G","存储":"16G","像素":"300万像素"}
                //{"手机屏幕尺寸":"5.0寸","网络":"电信4G","颜色":"白","测试":"s11","机身内存":"128G","存储":"16G","像素":"300万像素"}
                String keyAsString = bucket.getKeyAsString();
                Map<String,String> map = JSON.parseObject(keyAsString, Map.class);
                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                    String key = stringStringEntry.getKey();//手机屏幕尺寸
                    String value = stringStringEntry.getValue();//5.5寸
                    values = specMap.get(key);
                    if(values==null){
                        values = new HashSet<>();
                    }
                    values.add(value);
                    specMap.put(key,values);
                }
            }
        }
        return specMap;
    }
}
