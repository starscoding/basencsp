package mygroup;

import redis.clients.jedis.Jedis;

public class TestRedis {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		String name = jedis.get("name");
		System.out.println(name);
	}
}
