package com.example.jpaclient;

import com.example.jpaclient.dao.OrderRepository;
import com.example.jpaclient.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class JpaController {

    @Autowired
    OrderRepository orderRepository;

    @RequestMapping("/order/{id}")
    public Optional<Order> queryOrderById(@PathVariable String id){
        return orderRepository.findById(id);
    }
}
