package com.itheima;

import redis.clients.jedis.Jedis;

public class App {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.40.130",6378);
        jedis.set("name","itheima");
        jedis.close();
    }
}
