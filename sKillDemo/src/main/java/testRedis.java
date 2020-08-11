import com.glodon.demo.doMain.Record;
import com.glodon.demo.utils.JDKSerializetionUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Explain:Java操作Redis测试
 */
public class testRedis {

    private Jedis jedis;
    private JedisPool jedisPool;
    private JDKSerializetionUtil jdkSerializetionUtil;

    RedisTemplate redisTemplate;
    @Before
    public void setJedis() {
        //连接redis服务器(在这里是连接本地的)
        jedis = new Jedis("127.0.0.1", 6379);
        //权限认证
//        jedis.auth("chenhaoxiang");
        System.out.println("连接服务成功");
    }

    /**
     * Redis操作字符串
     */
    @Test
    public void testString() {
        //添加数据
        jedis.set("name", "chx"); //key为name放入value值为chx
        System.out.println("拼接前:" + jedis.get("name"));//读取key为name的值

//        //向key为name的值后面加上数据 ---拼接
//        jedis.append("name", " is my name;");
//        System.out.println("拼接后:" + jedis.get("name"));
//
//        //删除某个键值对
//        jedis.del("name");
//        System.out.println("删除后:" + jedis.get("name"));
//
//        //s设置多个键值对
//        jedis.mset("name", "chenhaoxiang", "age", "20", "email", "chxpostbox@outlook.com");
//        jedis.incr("age");//用于将键的整数值递增1。如果键不存在，则在执行操作之前将其设置为0。 如果键包含错误类型的值或包含无法表示为整数的字符串，则会返回错误。此操作限于64位有符号整数。
//        System.out.println(jedis.get("name") + " " + jedis.get("age") + " " + jedis.get("email"));
    }
    @Test
    public void test1(){
        System.out.println(System.currentTimeMillis());
    }
    @Test
    public void testMap() throws IOException {
//        Jedis jedis1 = new Jedis("127.0.0.1", 6379);
//        System.out.println(jedis1.get("name"));
//        redisTemplate = new RedisTemplate();
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Record.class));
//        HashOperations<String, String, Object> miaoshamap=redisTemplate.opsForHash();
        jdkSerializetionUtil = new JDKSerializetionUtil();
        //添加数据
        List<Record> list = new ArrayList<>();
        list.add(new Record(1,2));
        list.add(new Record(2,2));
        Map<byte[], byte[]> map1 = new HashMap<>();
        for(Record record:list){
            //序列化对象操作
            byte[] item = jdkSerializetionUtil.serialize(record);
            System.out.println(record.getId());
            map1.put(String.valueOf(record.getId()).getBytes(),item);
        }
        jedis.hmset("record".getBytes(),map1);
//        jedis.hdel("record".getBytes());
        System.out.println("done");
        System.out.println("record".getBytes().toString());
        Map<byte[], byte[]> map = new HashMap<>();
        //取一条
        byte[] item = jedis.hget("record".getBytes(), String.valueOf(2).getBytes());
        Object record = jdkSerializetionUtil.unserizlize(item);
        Record record1 = (Record) record;
        System.out.println(record1);
        //把redis所有record数据来出来
//       map = jedis.hgetAll("record".getBytes());
//        for (Map.Entry<byte[], byte[]> m : map.entrySet()){
//            System.out.println("key:" + m.getKey() + " value:" + m.getValue());
//           // byte[]item = jedis.hget("record".getBytes(),m.getKey());
//            Object record = jdkSerializetionUtil.unserizlize(m.getValue());
//            Record record1 = (Record)record;
//            System.out.println(record1);
//        }


//        byte[]item = jedis.hget("record".getBytes(),String.valueOf(1).getBytes());
//        Object record = jdkSerializetionUtil.unserizlize(item);
//        Record record1 = (Record)record;
//        System.out.println(record1);

//




        //取出user中的name，结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key是可变参数
//        List<String> list = jedis.hmget("martin", "name", "age", "email");
//        System.out.println("list: "+list);
//        System.out.println("******************************");
//        Map<Object, Object> map1 = redisUtil.hmget("martin");
//         System.out.println(map1);

//        //删除map中的某个键值
//        jedis.hdel("martin", "age");
//        System.out.println("age:" + jedis.hmget("martin", "age")); //因为删除了，所以返回的是null
//        System.out.println("user的键中存放的值的个数:" + jedis.hlen("martin")); //返回key为user的键中存放的值的个数2
//        System.out.println("是否存在key为user的记录:" + jedis.exists("martin"));//是否存在key为user的记录 返回true
//        System.out.println("user对象中的所有key:" + jedis.hkeys("martin"));//返回user对象中的所有key
//        System.out.println("user对象中的所有value:" + jedis.hvals("martin"));//返回map对象中的所有value
//
//        //拿到key，再通过迭代器得到值
//        Iterator<String> iterator = jedis.hkeys("martin").iterator();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            System.out.println(key + ":" + jedis.hmget("martin", key));
//        }
//        jedis.del("martin");
//        System.out.println("删除后是否存在key为user的记录:" + jedis.exists("martin"));//是否存在key为user的记录

    }

    /**
     * jedis操作List
     */
    @Test
    public void testList(){
        //移除javaFramwork所所有内容
        jedis.del("javaFramwork");
        //存放数据
        jedis.lpush("javaFramework","spring");
        jedis.lpush("javaFramework","springMVC");
        jedis.lpush("javaFramework","mybatis");
        //取出所有数据,jedis.lrange是按范围取出
        //第一个是key，第二个是起始位置，第三个是结束位置
        System.out.println("长度:"+jedis.llen("javaFramework"));
        //jedis.llen获取长度，-1表示取得所有
        System.out.println("javaFramework:"+jedis.lrange("javaFramework",0,-1));

        jedis.del("javaFramework");
        System.out.println("删除后长度:"+jedis.llen("javaFramework"));
        System.out.println(jedis.lrange("javaFramework",0,-1));
    }

    /**
     * jedis操作Set
     */
    @Test
    public void testSet(){
        //添加
        jedis.sadd("user","chenhaoxiang");
        jedis.sadd("user","hu");
        jedis.sadd("user","chen");
        jedis.sadd("user","xiyu");
        jedis.sadd("user","chx");
        jedis.sadd("user","are");
        //移除user集合中的元素are
        jedis.srem("user","are");
        System.out.println("user中的value:"+jedis.smembers("user"));//获取所有加入user的value
        System.out.println("chx是否是user中的元素:"+jedis.sismember("user","chx"));//判断chx是否是user集合中的元素
        System.out.println("集合中的一个随机元素:"+jedis.srandmember("user"));//返回集合中的一个随机元素
        System.out.println("user中元素的个数:"+jedis.scard("user"));
    }

    /**
     * 排序
     */
    @Test
    public void test(){
        jedis.del("number");//先删除数据，再进行测试
        jedis.rpush("number","4");//将一个或多个值插入到列表的尾部(最右边)
        jedis.rpush("number","5");
        jedis.rpush("number","3");

        jedis.lpush("number","9");//将一个或多个值插入到列表头部
        jedis.lpush("number","1");
        jedis.lpush("number","2");
        System.out.println(jedis.lrange("number",0,jedis.llen("number")));
        System.out.println("排序:"+jedis.sort("number"));
        System.out.println(jedis.lrange("number",0,-1));//不改变原来的排序
        jedis.del("number");//测试完删除数据
    }


}
