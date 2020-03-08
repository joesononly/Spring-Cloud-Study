package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class JConsoleClientApplication {

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
