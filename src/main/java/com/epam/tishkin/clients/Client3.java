package com.epam.tishkin.clients;

import com.epam.tishkin.server.Server;
import com.epam.tishkin.clients.ioTools.Reader;
import com.epam.tishkin.clients.ioTools.Writer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client3 {
    Reader reader;
    Writer writer;

    public Client3() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), Server.PORT);
            reader = new Reader(socket);
            writer = new Writer(socket);
            reader.start();
            writer.start();

        } catch (IOException e) {
            System.out.println("Проблема в конструкторе Client2");
        }
    }

    public static void main(String[] args) {
        Client3 client = new Client3();
    }
}
