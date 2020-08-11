package com.glodon.demo.service.impl;

import com.glodon.demo.controller.SKillController;
import com.glodon.demo.domain.Record;
import com.glodon.demo.service.SKillService;
import com.glodon.demo.utils.JDKSerializetionUtil;
import com.glodon.demo.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.*;


@Service
//@Transactional
public class SKillServiceImpl implements SKillService {
    @Resource
    JedisPool jedisPool = new JedisPool();
    Log log = LogFactory.getLog( this .getClass());




    /**
     * 实现秒杀功能
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public boolean secKill(Integer userId, Integer productId) {//看情况加不加ReentrenLock
        Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Record record = new Record(userId,productId,2,date);
        Jedis   jedis =jedisPool.getResource();
        if (jedis != null){
            jedis.watch(String.valueOf(productId));
            //watch 监视一个key，当事务执行之前这个key发生了改变，事务会被打断
            String count = jedis.get(String.valueOf(productId));//商品数量
            if (count == null) {
                log.info("商品不存在");
            }
            if (count!=null){
                Integer num=null;
                if (!StringUtil.isNumber(count)){
                    num=0;
                }else {
                    num= Integer.valueOf(count);
                }
                if(num>=1){
                    Transaction tx = jedis.multi();//开启事务
                    tx.decrBy(String.valueOf(productId), 1);
                    record.setStatus(1);
                    byte[] item = JDKSerializetionUtil.serialize(record);
                    map.put(String.valueOf(System.currentTimeMillis()).getBytes(),item);
                    tx.hmset("record".getBytes(),map);
                    List<Object> list = tx.exec();// 执行事务，得到返回值。如果此时watchkeys被改动了，事务返回，则返回null
                    // list为null或空时，表示并发情况下用户没能抢购到商品，秒杀失败。
                    if(list==null || list.size()==0){
                        //执行到这的事务里面的我认为没执行，测试看对不对
                        //看有没有多线程影响
                        log.info("用户"+userId+"抢购"+"商品"+productId+":失败");
                        record.setStatus(3);
                        byte[] item1 = JDKSerializetionUtil.serialize(record);
                        map.put(String.valueOf(System.currentTimeMillis()).getBytes(),item1);
                        jedis.hmset("record".getBytes(),map);
                        jedis.close();
                        return false;
                    }else{
                        for(Object success : list){
                            log.info("用户"+userId+"抢购"+"商品"+productId+":成功");
                            jedis.close();
                            return true;
                        }
                    }
                }else {
                    record.setStatus(3);
                    byte[] item1 = JDKSerializetionUtil.serialize(record);
                    if (item1!=null){
                        map.put(String.valueOf(System.currentTimeMillis()).getBytes(),item1);
                    }
                    jedis.hmset("record".getBytes(),map);
                    log.info("用户"+userId+"抢购"+"商品"+productId+":商品已经被抢完了");
                }
            }
        }
         jedis.close();
         return false;
    }

}
