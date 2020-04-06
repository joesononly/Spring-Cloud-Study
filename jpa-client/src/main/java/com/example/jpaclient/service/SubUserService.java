package com.example.jpaclient.service;

import com.example.jpaclient.dao.UserRepository;
import com.example.jpaclient.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubUserService {

    Logger logger = LoggerFactory.getLogger(SubUserService.class);

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void createSubUser(User user){
        userRepository.save(user);
        throw new RuntimeException("创建子用户错误");
    }
}
