package com.epam.tishkin;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client2 {
    Reader reader;
    Writer writer;

    public Client2() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), Server.PORT);
            reader = new Reader(socket);
            writer = new Writer(socket);
        } catch (IOException e) {
            System.out.println("Проблема в конструкторе Client2");
        }
    }

    public static void main(String[] args) {
        Client client2 = new Client();
        client2.reader.start();
        client2.writer.start();
    }
}
