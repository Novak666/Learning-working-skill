package com.changgou.search.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/31
 **/
public class LongToStringSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //自定义的序列化将long类型的数据 转成STRING 设置到JSON序列化器中
        gen.writeString(value.toString());
    }
}
