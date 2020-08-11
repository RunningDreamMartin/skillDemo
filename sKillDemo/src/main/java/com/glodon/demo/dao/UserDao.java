package com.glodon.demo.dao;

import com.glodon.demo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    User selectUser(@Param("name") String name);
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(String name, String password);
}