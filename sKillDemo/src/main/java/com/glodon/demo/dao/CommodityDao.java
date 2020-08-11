package com.glodon.demo.dao;

import com.glodon.demo.domain.Commodity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangzy-t
 */
@Mapper
public interface CommodityDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Commodity record);

    int insertSelective(Commodity record);

    Commodity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commodity record);
    Integer getIdByCode(String code);
    int updateByPrimaryKey(Commodity record);

    int getStockById(Integer id);
    int getStatusById(Integer id);
    List<Integer> getStatusByIdList(@Param("idList") List<Integer> idList);

    int updateStatusById(@Param("id")Integer id, @Param("status") Integer status);
    int updateStatusByIdList(@Param("idList") List<Integer> idList,@Param("status") Integer status);
    int updateStockById(@Param("id")Integer id, @Param("stock") Integer stock);
    List<Commodity> selectExists(@Param("status") Integer status);
    //自定义方法

    /**
     * 查询所有商品
     * @return
     */
    List<Commodity> findAll();

    /**
     * 分页查询商品
     * @return
     */
    List<Commodity> selectPage();

    /**
     * 根据三个条件，组合查询符合条件的商品记录
     * @param code
     * @param name
     * @param status
     * @return
     */
    List<Commodity> queryCommodity(@Param("code") String code, @Param("name") String name, @Param("status") Integer status);

    List<String> queryAllCodes();
}