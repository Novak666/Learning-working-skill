package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 操作ES
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/29
 **/
//@Repository//@componse @controller @service @Repository 可以不用加
// SkuInfo,Long  skuinfo 标识要操作到的POJO --》映射到es
// Long 指定POJO的文档的唯一标识数据类型
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
