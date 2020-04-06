package com.example.jpaclient.dao;

import com.example.jpaclient.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByName(String name);
}
