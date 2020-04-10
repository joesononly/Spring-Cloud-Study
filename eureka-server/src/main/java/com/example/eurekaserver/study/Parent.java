package com.example.eurekaserver.study;

public class Parent {
    static {
        System.out.println("Parent static block");
    }

    {
        System.out.println("Parent not static block");
    }

    Parent(){
        System.out.println("Parent Constructor");
    }
}
