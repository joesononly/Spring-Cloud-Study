package com.example.dao.mapper;

import com.example.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface UserMapper {

    List<User> query(User user);
}
