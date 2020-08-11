package com.glodon.demo.service;

import com.glodon.demo.domain.User;

import java.util.Map;

public interface UserService {

    public Boolean insert(User user);

    public Map<String, Object> login(String name, String pass);
}
