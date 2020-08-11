package com.glodon.demo.service.impl;

import com.glodon.demo.dao.CommodityDao;
import com.glodon.demo.dao.RecordDao;
import com.glodon.demo.domain.Commodity;
import com.glodon.demo.domain.Record;
import com.glodon.demo.model.RecordModel;
import com.glodon.demo.service.RecordService;
import com.glodon.demo.utils.JDKSerializetionUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author huangs-e
 * @Date 2020/8/8 14:34
 * @Version 1.0
 */
@Service
public class RecordServiceImpl implements RecordService {
    @Resource
    JedisPool jedisPool = new JedisPool();



    @Resource
    RecordDao recordDao;
    @Resource
    CommodityDao commodityDao;

    @Override
    public List<RecordModel> getRecord(Integer id) {

        Jedis jedis = jedisPool.getResource();
        Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
        map = jedis.hgetAll("record".getBytes());
        List<Record> redisList = new ArrayList<Record>();
        List<Record> mysqlList = recordDao.selectByUid(id);
        System.out.println(mysqlList.size());
        List<Record> idList = new ArrayList<Record> ();
        List<RecordModel> returnList=new ArrayList<RecordModel> ();
        idList.addAll(mysqlList);
        //增加mysql的记录

        for (Map.Entry<byte[], byte[]> m : map.entrySet()){
            Object record = JDKSerializetionUtil.unserizlize(m.getValue(),Record.class);
            Record record1 = (Record)record;
            redisList.add(record1);
        }
        Commodity commodity=commodityDao.selectByPrimaryKey(id);

        if (redisList!=null){
            for (Record record : redisList) {
                if (record.getId()==id){
                    idList.add(record);
                }
            }
        }
        //增加redis记录
        for (Record record : idList ){
            RecordModel recordModel = new RecordModel();
            recordModel.setCommodityName(commodity.getName());
            recordModel.setDate(record.getTime());
            recordModel.setPrice(commodity.getSecPrice());
            recordModel.setStatus(record.getStatus());
            returnList.add(recordModel);
        }
        return returnList;
    }
}
