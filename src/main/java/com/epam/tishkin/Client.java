package com.epam.tishkin;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    Reader reader;
    Writer writer;

    public Client() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), Server.PORT);
            reader = new Reader(socket);
            writer = new Writer(socket);
            reader.start();
            writer.start();
        } catch (IOException e) {
            System.out.println("Проблема в конструкторе Client");
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
