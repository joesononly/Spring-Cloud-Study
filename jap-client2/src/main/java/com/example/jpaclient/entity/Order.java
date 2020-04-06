package com.example.jpaclient.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "s_order@SIEBEL_DEV_LINK")
@Getter
@Setter
@ToString
public class Order {

    @Column(name="ROW_ID",unique = true)
    @Id
    private String id;
    @Column(name="ORDER_NUM")
    private String orderNum;

    @Column(name = "ACCNT_ID")
    private String accountId;


}
