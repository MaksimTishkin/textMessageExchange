package com.epam.tishkin.clients;

import com.epam.tishkin.server.Server;
import com.epam.tishkin.clients.ioTools.Reader;
import com.epam.tishkin.clients.ioTools.Writer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

public class Client2 {
    Properties properties = new Properties();
    Reader reader;
    Writer writer;

    public Client2() {
        try {
            properties.load(new FileReader("src/main/resources/config.properties"));
            String localHost = properties.getProperty("localHost");
            int port = Integer.parseInt(properties.getProperty("PORT"));
            Socket socket = new Socket(localHost, port);
            reader = new Reader(socket);
            writer = new Writer(socket);
            reader.start();
            writer.start();
        } catch (IOException e) {
            System.out.println("Проблема в конструкторе Client2");
        }
    }

    public static void main(String[] args) {
        Client2 client = new Client2();
    }
}
