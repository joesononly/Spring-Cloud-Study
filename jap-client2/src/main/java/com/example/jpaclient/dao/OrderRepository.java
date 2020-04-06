package com.example.jpaclient.dao;

import com.example.jpaclient.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {
}
