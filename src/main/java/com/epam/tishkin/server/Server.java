package com.epam.tishkin.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final ServerSocket serverSocket;
    private static final List<ClientSession> CLIENT_SESSION_LIST = new CopyOnWriteArrayList<>();
    private static final Properties properties = new Properties();
    final static Logger logger = LogManager.getLogger(Server.class);

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public static void main(String[] args) {
        try (FileReader readerForProperties = new FileReader("src/main/resources/config.properties")) {
            properties.load(readerForProperties);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        int port = Integer.parseInt(properties.getProperty("PORT"));
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void startServer() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientSession client = new ClientSession(clientSocket);
            CLIENT_SESSION_LIST.add(client);
            client.start();
        }
    }

     public static List<ClientSession> getClientSessionList() {
        return CLIENT_SESSION_LIST;
     }
}
