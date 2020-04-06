package com.example.jpaclient.service;

import com.example.jpaclient.dao.UserRepository;
import com.example.jpaclient.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SubUserService subUserService;

    /**
     *
     */
    public void createUserWrong1(User user){

        try{
            //调用私有方法保存数据
            createUserByPrivate(user);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        //判断用户数据是否被事务回滚
        List<User> users = userRepository.findByName(user.getName());
        logger.info("用户数为：{}",users.size());

    }

    public void createUserWrong2(User user){
        try{
            //调用私有方法保存数据
            createUserByPublic(user);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        //判断用户数据是否被事务回滚
        List<User> users = userRepository.findByName(user.getName());
        logger.info("用户数为：{}",users.size());
    }

    public void createdUserRight(User user){
        try{
            //调用私有方法保存数据
            userService.createUserByPublic(user);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        //判断用户数据是否被事务回滚
        List<User> users = userRepository.findByName(user.getName());
        logger.info("用户数为：{}",users.size());
    }

    /**
     * @Created by zjh
     * @Created 20200325
     * @Comment @Transactional 注解无法作用于私有方法，因此事务将无效
     * @param user
     */
    @Transactional
    private void createUserByPrivate(User user){
        userRepository.save(user);

        if (!"test".equals(user.getName()))
            throw new RuntimeException("用户名错误");
    }


    /**
     * @Comment @Transactional 由于Spring对service进行了AOP代理，因此只能通过代理类调用公开方法，事务才能有效
     * @param user
     */
    @Transactional
    public void createUserByPublic(User user){
        userRepository.save(user);

        if (!"test".equals(user.getName()))
            throw new RuntimeException("用户名错误");
    }

    /**
     * @Comment 异常在事务的方法中被抛出，事务无法被执行
     * @param user
     */
    @Transactional
    public void createUserByPublic2(User user){
        try{
            userRepository.save(user);

            if (!"test".equals(user.getName()))
                throw new RuntimeException("用户名错误");
        }catch (Exception e){
            logger.error("函数内捕获异常，使得事务无法被执行，异常:{}",e.getMessage());
        }

    }

    /**
     * @Comment 在Spring源码中描述了，只有RunntimeException和Error抛出时才会触发事务，其他的异常将会被判定为业务异常，将不会触发事务
     * @param user
     * @throws IOException
     */
    @Transactional
    public void createUserExcludeRunExc(User user) throws IOException{
        userRepository.save(user);
        otherTask();
    }

    /**
     * @Comment 用于伪造IO异常
     * @throws IOException
     */
    private void otherTask() throws IOException { Files.readAllLines(Paths.get("file-that-not-exist")); }

    @Transactional
    public void createSubUser(User user){
        userRepository.save(user);
        subUserService.createSubUser(user);
    }
}
