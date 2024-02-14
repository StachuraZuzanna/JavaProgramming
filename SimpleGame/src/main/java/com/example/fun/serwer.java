package com.example.fun;

import javafx.application.Application;
import javafx.stage.Stage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.*;
import java.net.*;

public class serwer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //receiver
        DatagramSocket server = new DatagramSocket(3000);
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf,1024);
        server.receive(packet);
        String str2=new String(packet.getData(),0,packet.getLength());
        System.out.println(str2);
        server.close();
    }
}
