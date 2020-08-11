package com.glodon.demo.service.impl;

import com.glodon.demo.dao.UserDao;
import com.glodon.demo.domain.User;
import com.glodon.demo.utils.MD5code;
import com.glodon.demo.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Resource
    private UserDao userMapper;
    Log log = LogFactory.getLog( this .getClass());
    final static int USER_LEVEL =1 ;

    @Override
    public Boolean insert(User user) {
        if(userMapper.selectUser(user.getName())==null){
            user.setLevel(USER_LEVEL);
            userMapper.selectUser(user.getName());
            String pwd=	MD5code.MD5code(user.getPassword());
            user.setPassword(pwd);
            userMapper.insert(user);
            log.info("注册成功");
            return true;
        }else {
            log.info("注册失败");
            return false;
        }
    }

    @Override
    public Map<String, Object> login(String name, String password) {
        Map<String, Object> map = new HashMap();
        User user = userMapper.login(name, MD5code.MD5code(password));
        if (user == null) {
            log.info("登录失败");
            map.put("status", false);
        } else {
            log.info("登录成功");
            map.put("status", true);
            map.put("userId", user.getId());
            map.put("level", user.getLevel());
        }
        return map;
    }

}
