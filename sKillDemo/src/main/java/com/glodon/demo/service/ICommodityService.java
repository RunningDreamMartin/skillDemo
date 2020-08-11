package com.glodon.demo.service;

import com.glodon.demo.domain.Commodity;
import com.glodon.demo.domain.CommodityFile;
import com.glodon.demo.page.PageRequest;
import com.glodon.demo.page.PageResult;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangzy-t
 */
public interface ICommodityService {

    /**
     * 查询所有商品
     * @return
     */
    List<Commodity> findAll();

    /**
     * 分页查询接口
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPage(PageRequest pageRequest) throws IOException;

    /**
     * 新增商品
     */
    String addCommodity(Commodity commodity);

    /**
     * 根据id查找某一商品记录
     * @param id
     * @return
     */
    Commodity findById(Integer id);

    /**
     * 根据id更新某一商品记录
     * @param
     * @return
     */
    String updateCommodity(CommodityFile commodityFile);
    /**
     * 根据三个条件，组合查询符合条件的商品记录
     * @param code
     * @param name
     * @param status
     * @return
     */
    List<Commodity> queryCommodity(String code, String name, Integer status);

}
