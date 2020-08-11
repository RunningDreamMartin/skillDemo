package com.glodon.demo.utils;

import com.glodon.demo.dao.CommodityDao;
import com.glodon.demo.dao.RecordDao;
import com.glodon.demo.domain.Commodity;
import com.glodon.demo.domain.Record;
import com.glodon.demo.service.ShelfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author huangs-e
 * @Date 2020/8/7 9:33
 * @Version 1.0
 */
@Component
public class TimeJob implements ApplicationContextAware {
    public static final int STOCK =0;
    public static final int ONSHELF = 1;
    public static final int DOWN=2;
   /* 对应在库存
    上架
    下架*/

    @Resource
    private JDKSerializetionUtil jdkSerializetionUtil;
    private final Log logger = LogFactory.getLog(TimeJob.class);
    Log log = LogFactory.getLog( this .getClass());
    @Resource
    CommodityDao commodityDao;

    @Resource
    private RecordDao recordDao;

    @Resource
    JedisPool jedisPool = new JedisPool();
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }


    /**
     * 每隔1分钟查询一次数据库
     *1分钟内有要上架的商品就将商品提前存入redis中
     */


    //@Scheduled(cron = "*/5 * * * * *")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void AutoUpShelf() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        List<Commodity>commodities=commodityDao.selectExists(0);
        //查询所有待上架商品
        Jedis jedis = jedisPool.getResource();
        for (Commodity commodity : commodities) {
            long curren = System.currentTimeMillis();
            Date real = new Date(curren);
            curren += 1 * 60 * 1000;
            Date date = new Date(curren);
            Date startData= commodity.getStartTime();
            Date endTime=commodity.getEndTime();
            if (startData == null||(startData!=null&&date.after(startData)&&commodity.getStatus()==0&&startData.after(real))){
                logger.info("存在可上架的商品");
                int stock=commodityDao.getStockById(commodity.getId());
                jedis.set(String.valueOf(commodity.getId()), String.valueOf(stock));
                commodityDao.updateStatusById(commodity.getId(),ONSHELF);
            }else {
                logger.info("没有上架的商品");
            }
        }
        jedis.close();
        logger.info("可上架的商品监测");
    }
    /**
     * 每隔5分钟查询一次数据库
     *15分钟内有已经下架的商品就将商品提前取出redis
     */

    //@Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void AutodownShelf() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        List<Commodity>commodities=commodityDao.selectExists(1);
        Jedis jedis = jedisPool.getResource();

        long curren = System.currentTimeMillis();

        Date date = dateFormat.parse(dateFormat.format(new Date(curren)));
        ShelfService shelfService;
        for (Commodity commodity : commodities) {
            Integer temp=commodity.getId();
            String tempStr=jedis.get(String.valueOf(temp));

            int stocks= Integer.parseInt(jedis.get(String.valueOf(temp)));
            commodityDao.updateStockById(commodity.getId(),stocks);
            if (tempStr==null||"0".equals(tempStr)){
                commodityDao.updateStockById(commodity.getId(),0);
                commodityDao.updateStatusById(commodity.getId(),DOWN);
                jedis.del(String.valueOf(commodity.getId()));
                log.info("商品没了释放redis ");
            }else {
                Date timeTemp=commodity.getEndTime();
                Date endTime=new Date();
                if(timeTemp!=null){
                    endTime=dateFormat.parse(dateFormat.format(timeTemp));
                }
                if (endTime !=null&&endTime.before(date)){

                    log.info("存在符合要求的下架商品");
                    int stock= Integer.parseInt(jedis.get(String.valueOf(temp)));
                    commodityDao.updateStockById(commodity.getId(),stock);
                    commodityDao.updateStatusById(commodity.getId(),DOWN);
                    log.info("修改mysql库存");
                    jedis.del(String.valueOf(commodity.getId()));
                    //清空redis缓存
                }else {
                    logger.info("没有上架的商品");
                }
            }
        }
        jedis.close();
        logger.info("可下架的商品监测");
    }



    /**
     * 定时刷新record，把redis中record表status为1，3的数据传给数据库，然后删除，避免下一次再次读到数据库里
     */
    //@Scheduled(cron = "0 0/10 * * * ?")
    @Scheduled(cron = "*/30 * * * * *")
    public void flashRecord() {
        log.info("持久化记录");

        Jedis jedis = jedisPool.getResource();
        Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();

        List<Record> list = new ArrayList<Record>();//所有record数据
        //把redis所有record数据来出来
        map = jedis.hgetAll("record".getBytes());
        for (Map.Entry<byte[], byte[]> m : map.entrySet()){
            Object record = JDKSerializetionUtil.unserizlize(m.getValue(),Record.class);
            Record record1 = (Record)record;
            list.add(record1);
        }

        //删除redis 数据
        jedis.del("record");

        //提取正在秒杀记录
        Map<byte[], byte[]> map1 = new HashMap<byte[], byte[]>();
        if (list.size() > 0){
            for (Record record:list) {
                if(record.getStatus()!=null&&record.getStatus() == 2){
                    byte[] item1 = jdkSerializetionUtil.serialize(record);
                    map1.put(String.valueOf(System.currentTimeMillis()).getBytes(),item1);
                }
            }
            if (map1!=null&&map1.size() != 0){
                jedis.hmset("record".getBytes(),map1);
            }

            //1，3存入数据库,这样一条一条插入会不会影响效率
            for (Record record:list) {
                if(record.getStatus() != 2){
                    recordDao.insert(record);
                }
            }
        }

        jedis.close();
    }

}