package com.epam.tishkin.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final List<ServerThread> serverThreadList = new CopyOnWriteArrayList<>();
    public static final int PORT= 3345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serverThreadList.add(new ServerThread(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Проблема в Сервере");
        }
    }

     public static List<ServerThread> getServerThreadList() {
        return serverThreadList;
     }
}
