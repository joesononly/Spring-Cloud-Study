package com.example.jpaclient.controller;

import com.example.jpaclient.entity.User;
import com.example.jpaclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/create/wrong1/{name}")
    public void createUserWrong1(@PathVariable(name = "name") String name){
        User user = new User(getCurrentTime(),name);
        userService.createUserWrong1(user);
    }

    @RequestMapping("/create/wrong2/{name}")
    public void createUserWrong2(@PathVariable(name = "name") String name){
        User user = new User(getCurrentTime(),name);
        userService.createUserWrong2(user);
    }

    @RequestMapping("/create/wrong3/{name}")
    public void createUserWrong3(@PathVariable(name = "name") String name) throws Exception{
        User user = new User(getCurrentTime(),name);
        userService.createUserExcludeRunExc(user);
    }

    @RequestMapping("/create/wrong4/{name}")
    public void createUserWrong4(@PathVariable(name = "name") String name){
        User user = new User(getCurrentTime(),name);
        userService.createSubUser(user);
    }

    @RequestMapping("/create/right/{name}")
    public void createUserRight(@PathVariable(name = "name") String name){
        User user = new User(getCurrentTime(),name);
        userService.createdUserRight(user);
    }

    @RequestMapping("/create/right2/{name}")
    public void createUserRight2(@PathVariable(name = "name") String name){
        User user = new User(getCurrentTime(),name);
        userService.createUserByPublic2(user);
    }


    public String getCurrentTime(){
        Date currentTime = new Date();
        //改变输出格式（自己想要的格式）
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到字符串时间
        String s8 = formatter.format(currentTime);
        return s8;
    }

}
