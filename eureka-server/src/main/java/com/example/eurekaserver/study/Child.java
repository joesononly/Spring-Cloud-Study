package com.example.eurekaserver.study;

public class Child extends Parent {
    static {
        System.out.println("Child static block");
    }

    {
        System.out.println("Child not static block");
    }

    Child(){
        System.out.println("Child constructor");
    }

    public static void main(String[] args) {
        Child child = new Child();
    }
}
