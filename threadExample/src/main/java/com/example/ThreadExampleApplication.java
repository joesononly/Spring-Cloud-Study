package com.example;

import com.example.controller.ThreadExampleController;
import org.openjdk.jmh.runner.Runner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class ThreadExampleApplication {

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
		SpringApplication.run(ThreadExampleApplication.class, args);
		//fillHeap(1000);
	}

}
