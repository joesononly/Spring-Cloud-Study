package com.example.jpaclient.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "s_order")
@Getter
@Setter
@ToString
public class Order {

    @Column(name="ROW_ID",unique = true)
    private String id;
    @Column(name="ORDER_NUM")
    private String orderNum;

}
