package com.changgou.search.service;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/31
 **/
public class SearchResultMapperImpl implements SearchResultMapper {

    /**
     *          //1.获取非高亮的数据
     *         //2.获取高亮的数据
     *         //3.将高亮的数据中name的值 替换到非高亮POJO中的NAME属性中
     *         //4.再返回
     * @param response
     * @param clazz
     * @param pageable
     * @param <T>
     * @return
     */
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        //1.获取content
        List<T> content = new ArrayList<T>();
        //2.获取分页对象 不用获取了参数中有
        // 获取高亮的数据
        SearchHits hits = response.getHits();
        if(hits==null || hits.getTotalHits()<=0){
            return new AggregatedPageImpl<T>(content);
        }
        for (SearchHit hit : hits) {
            //非高亮的 和高亮的数据 就是hit
            //非高亮的数据
            String sourceAsString = hit.getSourceAsString();
            //转成skuinfo
            SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);
            //获取高亮的数据 将高亮的数据设置到SKUinfo中的name属性中
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null
                    && highlightFields.get("name")!=null
                    && highlightFields.get("name").getFragments()!=null
                    && highlightFields.get("name").getFragments().length>0 ) {
                //获取高亮字段为name的高亮对象
                HighlightField highlightField = highlightFields.get("name");
                //获取高亮碎片
                Text[] fragments = highlightField.getFragments();
                StringBuffer sb = new StringBuffer();
                for (Text fragment : fragments) {
                    //高亮的数据name字段对应的高亮数据
                    String string = fragment.string();
                    sb.append(string);
                }
                //将高亮的数据设置到SKUinfo中的name属性中
                skuInfo.setName(sb.toString());
            }

            //添加到集合content中
            content.add((T)skuInfo);
        }


        //3.获取总记录数
        long totalHits = response.getHits().getTotalHits();//总记录数
        //4.获取聚合结果
        Aggregations aggregations = response.getAggregations();
        //5.获取游标的ID
        String scrollId = response.getScrollId();

        return new AggregatedPageImpl<T>(content,pageable,totalHits,aggregations,scrollId);
    }
}
