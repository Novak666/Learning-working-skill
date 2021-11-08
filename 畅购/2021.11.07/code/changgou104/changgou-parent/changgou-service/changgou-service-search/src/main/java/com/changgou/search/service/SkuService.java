package com.changgou.search.service;

import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/29
 **/
public interface SkuService {
    void importSku();

    Map<String, Object> search(Map<String, String> stringMap);
}
