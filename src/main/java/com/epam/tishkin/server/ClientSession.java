package com.epam.tishkin.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClientSession extends Thread {
    private String clientName;
    private BufferedReader in;
    private BufferedWriter out;
    private FileWriter fileWriter;
    private final static Queue<String> lastFiveMessage = new ConcurrentLinkedQueue<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();
    final static Logger logger = LogManager.getLogger(ClientSession.class);

    public ClientSession(Socket socket) {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/resources/config.properties"));
            File fileWithHistory = new File(properties.getProperty("fileWithHistory"));
            fileWriter = new FileWriter(fileWithHistory, true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Enter your name" + "\n");
            out.flush();
            clientName = in.readLine();
            Lock readLock = readWriteLock.readLock();
            readLock.lock();
            try {
                if (lastFiveMessage.size() > 0) {
                    for (String current : lastFiveMessage) {
                        out.write(current);
                        out.flush();
                    }
                }
            } finally {
                readLock.unlock();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String clientMessage = in.readLine();
                if ("exit".equals(clientMessage)) {
                    out.write(clientMessage + "\n");
                    out.flush();
                    break;
                }
                for (ClientSession currentClientSession : Server.getClientSessionList()) {
                    if (currentClientSession != this) {
                        currentClientSession.out.write(clientName + ": " + clientMessage + "\n");
                        currentClientSession.out.flush();
                    }
                }
                writeLock.lock();
                try {
                    fileWriter.write(clientName + ": " + clientMessage + "\n");
                    fileWriter.flush();
                    lastFiveMessage.add(clientName + ": " + clientMessage + "\n");
                    if (lastFiveMessage.size() > 5) {
                        lastFiveMessage.remove();
                    }
                } finally {
                    writeLock.unlock();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
