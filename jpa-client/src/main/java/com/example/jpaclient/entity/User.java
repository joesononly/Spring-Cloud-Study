package com.example.jpaclient.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user_test")
@Getter
@Setter
@ToString
public class User implements Serializable {

    public User(){}

    public User(String id,String name){
        this.id = id;
        this.name = name;
    }

    @Id
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
