package com.glodon.demo.service.impl;

import com.glodon.demo.dao.RecordDao;

import com.glodon.demo.domain.Record;
import com.glodon.demo.utils.JDKSerializetionUtil;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目启动时该方法执行一次
 */

@Service
public class MyListener  implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private RecordDao recordDao;
    @Resource
    JedisPool jedisPool = new JedisPool();

    @SneakyThrows
    @Override

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {//保证只执行一次
            //项目启动建立redis的record表

            //连接redis服务器

            Jedis jedis = jedisPool.getResource();
            Map<byte[], byte[]> map = new HashMap<>();
            List<Record> list = recordDao.findAll();

            for (Record e: list) {
                //序列化record对象操作
                byte[] item = JDKSerializetionUtil.serialize(e);
                map.put(String.valueOf(System.currentTimeMillis()).getBytes(),item);

            }
            if (map!=null&&map.size() > 0){
                jedis.hmset("record".getBytes(),map);
                jedis.close();
            }
        }
    }
}
