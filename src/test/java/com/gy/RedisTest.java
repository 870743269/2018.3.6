package com.gy;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.gy.domain.News;
import com.gy.utils.ByUtils;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;

public class RedisTest {
	Jedis jedis;

	@Before
	public void Before(){
		//链接redis 数据库
		jedis=new Jedis("192.168.45.13", 6379);
		jedis.auth("redis");
	}
	@Test
	public void testString(){
		//选择数据库(默认选择0号数据库)
		jedis.select(0);

		//添加一个字符串
		jedis.set("name", "zhangsan");
		System.out.println(jedis.get("name"));

		//追加字符串
		jedis.append("name","123");
		System.out.println(jedis.get("name"));

		//一次 设置--获取  多个字符串
		jedis.mset("a","1","b","2","c","3");
		System.out.println(jedis.mget("a","b","c"));

		//获得字符串长度
		System.out.println(jedis.strlen("name"));

		//获得原来的值并重新设置
		String set = jedis.getSet("name", "lishi");
		System.out.println(set);
		System.out.println(jedis.get("name"));
		System.out.println("-------------------String");
	}

	@Test
	public void testList(){
		jedis.select(1);

		//添加list集合数据
		jedis.lpush("1", "zhangsan", "lisi","wangwu");//压栈

		System.out.println(jedis.lpop("1"));//弹栈(删除下标为0的值)

		//获取list集合中,指定下标处的元素
		System.out.println(jedis.lindex("1", 2));

		//返回集合长度
		System.out.println(jedis.llen("1"));

		//在指定的元素前或后插入新元素
		jedis.linsert("1", LIST_POSITION.BEFORE, "wangwu", "1");

		//修改指定下标区间内的元素
		jedis.lset("1", 0, "guaner");

		//保留指定的元素
		jedis.ltrim("1", 0,0);

		System.out.println(jedis.lrange("1", 0,-1));
		System.out.println("---------------list");
	}

	@Test
	public void testHash(){
		jedis.select(2);
		//添加hash类型数据
		//key值唯一, 存在key 则修改
		jedis.hset("1", "name", "手机");
		jedis.hset("1", "color", "红色");
		jedis.hset("1", "id", "12");

		//创建Map集合
		Map<String, String> hash =new HashMap();
		hash.put("price", "1200");
		hash.put("网段", "CDMA");
		jedis.hmset("1", hash);

		//获取map集合中的值
		System.out.println(jedis.hget("1", "name"));
		//获取根据index 获取map集合
		System.out.println(jedis.hgetAll("1"));
		//获取map集合所有key
		System.out.println(jedis.hkeys("1"));
		//获取map集合所有值
		System.out.println(jedis.hvals("1"));

		System.out.println("-----------------hash");
	}

	@Test
	public void testSet(){
		jedis.select(3);
		//值唯一
		//添加set集合元素
		jedis.sadd("a", "zhangsan","lishi","wangwu");
		jedis.sadd("b", "zhangsan","lishi","wangwu","zhaoliu");
		jedis.sadd("a", "2","1","3");

		//获得set集合
		System.out.println(jedis.smembers("a"));
		//获得set集合的长度
		System.out.println(jedis.scard("a"));
		//判断set集合是否存在某个元素
		Boolean sismember = jedis.sismember("a", "2");
		System.out.println(sismember);

		//获得指定set集合的差集-交集-并集
		System.out.println(jedis.sdiff("a","b"));
		System.out.println(jedis.sinter("a","b"));
		System.out.println(jedis.sunion("a","b"));

		//删除一个随机元素  并返回
		System.out.println(jedis.spop("a"));

		//移动元素
		System.out.println(jedis.smove("a", "b", "lishi"));

		//删除元素
		System.out.println(jedis.srem("a", "zhangsan"));

		System.out.println("-----------------set");
	}

	@Test
	public void testSortedSet(){
		jedis.select(4);
		//
		System.out.println(jedis.zadd("a",1,"马云"));

		System.out.println("-----------------SortedSet");
	}

	/**
	 * 将javabean对象保存redis数据库
	 * 将javabean对象转化为json串
	 * **/
	@Test
	public void testJavabeanToJson(){
		jedis.select(5);
		//创建news对象
		News news=new News();
		news.setId(1);
		news.setTitle("尚学堂");
		news.setContent("尚学堂上市.....");
		//转化成 json 储存
		jedis.set("a", new Gson().toJson(news));
		//取出后转化成对象
		News fromJson = new Gson().fromJson(jedis.get("a"), News.class);
		System.out.println(fromJson);
	}

	/**
	 * 将javabean对象转化为by数组
	 * **/
	@Test
	public void testJavabeanToByte(){
		jedis.select(6);
		//创建news对象
		News news=new News();
		news.setId(1);
		news.setTitle("尚学堂");
		news.setContent("尚学堂上市.....");
		
		byte[] byb = ByUtils.objToByte("b");
		
		jedis.set(byb,ByUtils.objToByte(news));
		
		News  byteToObj= ByUtils.byteToObj(jedis.get(byb), News.class);
		

		System.out.println(byteToObj);
	}
}


