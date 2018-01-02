package com.xlx.utility;

import redis.clients.jedis.Jedis;

public class RedisTest {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Jedis jedis = new Jedis("192.168.37.66");
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.print(value);
	}

}
