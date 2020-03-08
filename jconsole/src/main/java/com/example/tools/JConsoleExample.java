package com.example.tools;

import com.example.JConsoleClientApplication;

import java.util.ArrayList;
import java.util.List;

/*
* @Created by joeson
* @Created 20200308
* @Comment 用于学习JConsole如何监控应用的健康情况，如线程，内存等信息
*          VM参数：-Xms100m -Xmx100m -XX:+UseSerialGC
* */
public class JConsoleExample {

    static public void fillHeap(int num) throws InterruptedException{
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0;i<num;i++){
            //延缓一点时间
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    static class OOMObject{
        public byte[] placeHolder = new byte[64*1024];
    }

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(ConfigClientApplication.class, args);
        fillHeap(1000);
    }
}
