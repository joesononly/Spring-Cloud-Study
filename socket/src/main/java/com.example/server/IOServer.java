package com.example.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

class IOServer{

    public static void main(String[] args) throws Exception{

        //TODO 服务器连接客户端
        final ServerSocket server = new ServerSocket(3333);

        while (true){
            final Socket socket = server.accept();

            new Thread(new Runnable() {
                public void run() {
                    int len;
                    byte[] data = new byte[1024];

                    try{
                        InputStream in = socket.getInputStream();

                        while((len = in.read(data)) != -1){
                            System.out.println(new String(data,0,len));
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }

    }
}