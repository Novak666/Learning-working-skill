package com.itheima;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Article;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/17 11:29
 * @description 标题
 * @package com.itheima
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {

    @Autowired
    private TransportClient transportClient;//操作链接到es服务端的客户端API
    @Autowired
    private ObjectMapper objectMapper;//json的转换器

    //insert  //update

    //创建索引  创建类型  创建文档  能自动的进行映射  (创建博客系统 blog01,article类型，docuemnt数据（JSON）)

    /**
     * {
     * "id":1,
     * "title":"震惊！最近航母开到美国了"
     * "content":"标题党数据"
     * <p>
     * }
     */
    @Test
    public void create() throws Exception {

        //restTemplate

        Article article = new Article(2L, "震惊一点不震惊！最近航母开到美国了", "标题党数据");

        String jsonstr = objectMapper.writeValueAsString(article);
        //参数1 指定索引名
        //参数2 指定类型名称
        //参数3 指定文档的唯一标识
        IndexResponse indexResponse = transportClient.prepareIndex("blog02", "article", "1")
                //参数1 指定文档的数据是一个JSON字符串
                //参数2 指定数据类型 JSON数据类型
                .setSource(jsonstr, XContentType.JSON)
                //指定动作 将数据存储到es服务器中
                .get();
        System.out.println(indexResponse.getIndex() + ":" + indexResponse.getVersion() + ":" + indexResponse.getType());
    }

    //update


    //delete
    @Test
    public void delete() {
        //删除文档ID为1的数据
        transportClient.prepareDelete("blog02", "article", "1").get();
    }


    //select 数据

    @Test
    public void select() {
        GetResponse docuemntResponse = transportClient.prepareGet("blog02", "article", "1").get();
        Map<String, Object> sourceAsMap = docuemntResponse.getSourceAsMap();
        String sourceAsString = docuemntResponse.getSourceAsString();
        System.out.println(sourceAsMap);
        System.out.println(sourceAsString);
    }

}
