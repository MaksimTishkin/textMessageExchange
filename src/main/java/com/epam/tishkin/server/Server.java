package com.epam.tishkin.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static ServerSocket serverSocket;
    private static final List<ServerThread> serverThreadList = new CopyOnWriteArrayList<>();
    private static final Properties properties = new Properties();
    public static int port;

    public static void main(String[] args) {
        try {
            properties.load(new FileReader("src/main/resources/config.properties"));
            port = Integer.parseInt(properties.getProperty("PORT"));
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serverThreadList.add(new ServerThread(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Инициализация properties");
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Закрытие сервер сокет");
            }
        }
    }

     public static List<ServerThread> getServerThreadList() {
        return serverThreadList;
     }
}
