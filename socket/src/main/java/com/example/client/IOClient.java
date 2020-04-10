package com.example.client;

import java.net.Socket;
import java.util.Date;

public class IOClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1",3333);

        while(true){
            socket.getOutputStream().write((new Date()+":Hello World").getBytes());
            Thread.sleep(2000);
        }
    }
}
