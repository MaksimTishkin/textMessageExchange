package com.epam.tishkin.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server implements Runnable {
    private static ServerSocket serverSocket;
    private static final List<ClientSession> CLIENT_SESSION_LIST = new CopyOnWriteArrayList<>();
    private static final Properties properties = new Properties();
    final static Logger logger = LogManager.getLogger(Server.class);

    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }

    public void run() {
        try {
            properties.load(new FileReader("src/main/resources/config.properties"));
            int port = Integer.parseInt(properties.getProperty("PORT"));
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                CLIENT_SESSION_LIST.add(new ClientSession(clientSocket));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

     public static List<ClientSession> getClientSessionList() {
        return CLIENT_SESSION_LIST;
     }
}
