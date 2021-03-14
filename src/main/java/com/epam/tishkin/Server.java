package com.epam.tishkin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static List<ServerThread> serverThreadList = new ArrayList<>();
    public static final int PORT= 3345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключение " + clientSocket);
                serverThreadList.add(new ServerThread(clientSocket));
                System.out.println("В листе " + serverThreadList.size());
            }
        } catch (IOException e) {
            System.out.println("Проблема в Сервере");
        }
    }
}
