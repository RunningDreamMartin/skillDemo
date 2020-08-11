package com.glodon.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.glodon.demo.dao.CommodityDao;
import com.glodon.demo.domain.Commodity;
import com.glodon.demo.domain.CommodityFile;
import com.glodon.demo.page.PageRequest;
import com.glodon.demo.page.PageResult;
import com.glodon.demo.page.PageUtils;
import com.glodon.demo.service.ICommodityService;
import com.glodon.demo.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzy-t
 */
@Service
public class CommodityServiceImpl implements ICommodityService {
    @Resource
    JedisPool jedisPool=new JedisPool();
    @Resource
    private CommodityDao commodityDao;
    Log log = LogFactory.getLog( this .getClass());
    @Override
    public List<Commodity> findAll() {
        List<Commodity> list = commodityDao.findAll();
        List<Commodity> listResult = changeList(list);
        return listResult;
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) throws IOException {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
    }

    public PageInfo<Commodity> getPageInfo(PageRequest pageRequest) throws IOException {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Commodity> list = commodityDao.selectPage();
        List<Commodity> listTemp=new ArrayList<> ();
        Jedis jedis=jedisPool.getResource();
        for (Commodity commodity : list) {
            String stock=null;
            try {
                stock= jedis.get(String.valueOf(commodity.getId()));
            }catch (Exception e) {

            }

            if (stock != null) {
                commodity.setStock(Integer.valueOf(stock));
            }
            listTemp.add(commodity);
        }
        List<Commodity> listResult = changeList(listTemp);
        return new PageInfo<Commodity>(listResult);
    }

    @Override
    public String addCommodity(Commodity commodity) {
        String code = commodity.getCode();
        List<String> list = commodityDao.queryAllCodes();
        for(String temp: list){
            if (code.equals(temp)){
                return "Wrong:Duplicate Commodity Code";
            }
        }
        commodityDao.insertSelective(commodity);
        return "Success";
    }

    @Override
    public Commodity findById(Integer id) {
        Commodity result = null;
        try {
            result = Utils.reConvert(commodityDao.selectByPrimaryKey(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String updateCommodity(CommodityFile commodityFile) {
        String code = commodityFile.getCode();
        Integer id = commodityFile.getId();
        List<String> list = commodityDao.queryAllCodes();
        //因为更新商品时，数据库中此商品本身对应的那条记录，code一定会重复，所以使用一个flag标志位，判断唯一的一次相等机会是否已经用完
        log.info(code+","+list);
        for(String temp: list){
            if( code.equals(temp) && id.equals(commodityDao.getIdByCode(temp))){
                continue;
            }
            if (code.equals(temp)){
                return "Wrong:Duplicate Commodity Code";
            }
        }
        try {
            Utils.createImage(commodityFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Commodity commodity = Utils.convert(commodityFile);
        commodityDao.updateByPrimaryKeySelective(commodity);
        return "Success";
    }

    @Override
    public List<Commodity> queryCommodity(String code, String name, Integer status) {
        System.out.println(status+"status");
        return commodityDao.queryCommodity(code, name, status);
    }

    /**
     * 将list中的Commodity元素转换为image字段为base64编码的新Commodity对象
     * @param list
     * @return
     */
    public List<Commodity> changeList(List<Commodity> list){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        List<Commodity> listResult = new ArrayList<>();
        for(Commodity commodity: list){
            try {
                listResult.add(Utils.reConvert(commodity));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listResult;
    }

}
