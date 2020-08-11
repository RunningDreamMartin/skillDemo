package com.glodon.demo.service.impl;

import com.glodon.demo.dao.CommodityDao;
import com.glodon.demo.service.ShelfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

/**
 * @Author huangs-e
 * @Date 2020/8/6 19:15
 * @Version 1.0
 */
@Service
public class ShelfServiceImpl implements ShelfService {

    public static final int UP_STATUS =1;
    public static final int Down_STATUS =2;


    Log log = LogFactory.getLog( this .getClass());
    @Resource
    @Autowired
    CommodityDao como;
    JedisPool jedisPool = new JedisPool();
    Jedis jedis=jedisPool.getResource();
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    @Override
    public   Boolean upShelf(Integer id){
        if (id!=null){
            int statu = como.getStatusById(id);
            if (statu == 0) {
                log.info(como.updateStatusById(id,UP_STATUS));
                    try {
                        como.updateStatusById(id,UP_STATUS);
                        log.info("上架成功");
                        int stock=como.getStockById(id);
                        jedis.set(String.valueOf(id), String.valueOf(stock));
                        return true;
                    }catch (Exception e) {
                        log.info("上架失败");
                    }
            }else {
                log.info("已经上架了");
            }

        }
        return false;
    }

    @Override
    public Boolean batchUpShelf(List<Integer> idList) {
        HashSet<Integer> hashSet = new HashSet<Integer> ();
        if (idList != null && idList.size() != 0) {
            for (Integer id : idList) {
                 boolean isSuc=upShelf(id);
                 if (isSuc) {
                     hashSet.add(1);
                 }else {
                     hashSet.add(0);
                 }
            }
            if (hashSet.contains(0)){
                log.error("没有全部上架成功");
                return false;
            }else {
                log.error("全部上架成功");
                return true;
            }

        }
        log.error("上架失败");
        return false;
    }


    /*
    *通过id来下架商品
    * 下架商品前先将redis中的数量保存到数据库中
    * redis归0
    * */
    @Override
    public Boolean downShelf(Integer id) {
        if (id!=null){
            int statu = como.getStatusById(id);
            if (statu == UP_STATUS) {
                try {
                    como.updateStatusById(id,Down_STATUS);
                    //更改商品状态
                    int stock= Integer.parseInt(jedis.get(String.valueOf(id)));
                    como.updateStockById(id,stock);
                    //更新商品数量
                    jedis.del(String.valueOf(id));
                    //删除redis缓存key
                    log.info("下架成功");
                    jedis.close();
                    return true;
                }catch (Exception e) {
                    log.info("下架失败");
                }
            }else {
                log.info("无法下架");
                return false;
            }
            log.info("没有此商品");
        }
        return false;
    }



    @Override
    public Boolean batchDownShelf(List<Integer> idList) {
        HashSet<Integer> hashSet = new HashSet<Integer> ();
        if (idList != null || idList.size() != 0) {
            for (Integer id : idList) {
                boolean isSuc=downShelf(id);
                if (isSuc) {
                    hashSet.add(1);
                }else {
                    hashSet.add(0);
                }
            }
            if (hashSet.contains(0)){
                log.error("没有全部下架成功");
                return false;
            }else {
                log.info("全部下架成功");
                return true;
            }
        }
        log.error("下架失败");
        return false;
    }




    @Test
    public void test(){
        Integer id = 3;
        if (id!=null){
            int statu = como.getStatusById(id);
            log.info(statu+"asdasdasd");
            if (statu == 0) {
                log.info(como.updateStatusById(id,UP_STATUS)+" asdas");
                try {
                    como.updateStatusById(id,UP_STATUS);
                    log.info("上架成功");
                    int stock=como.getStockById(id);
                    jedis.set(String.valueOf(id), String.valueOf(stock));
                    jedis.close();
                }catch (Exception e) {
                    log.info("上架失败");
                }
            }else {
                log.info("已经上架了");
            }

        }


    }
    public Boolean downShelf1() {
        Integer id=44;
        if (id!=null){
            try {
                como.updateStatusById(id,Down_STATUS);
                int stock= Integer.parseInt(jedis.get(String.valueOf(id)));
                como.updateStockById(id,stock);
                como.updateStatusById(id,Down_STATUS);
                jedis.set(String.valueOf(id),"0");
                log.info("下架成功");
                jedis.close();
                return true;
            }catch (Exception e) {
                log.info("下架失败");
            }
        }
        return false;
    }



}
