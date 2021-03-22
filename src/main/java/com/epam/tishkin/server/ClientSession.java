package com.epam.tishkin.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
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
    private final Lock readLock = readWriteLock.readLock();
    final static Logger logger = LogManager.getLogger(ClientSession.class);

    ClientSession(Socket socket) {
        try {
            fileWriter = new FileWriter("history.txt", true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void run() {
        setClientName();
        sendLastFiveMessage();
        while (true) {
            String clientMessage = "";
            try {
                clientMessage = in.readLine();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            if ("exit".equals(clientMessage)) {
                try {
                    out.write(clientMessage + "\n");
                    out.flush();
                    out.close();
                    in.close();
                    fileWriter.close();
                    break;
                } catch (IOException e) {
                    logger.error(e.getMessage());
                } finally {
                    Server.getClientSessionList().remove(this);
                }
            }
            sendMessageToClients(clientMessage);
            addMessageInQueue(clientMessage);
            writeMessageInHistoryFile(clientMessage);
        }
    }

    private void setClientName() {
        try {
            out.write("Enter your name" + "\n");
            out.flush();
            clientName = in.readLine();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendLastFiveMessage() {
        readLock.lock();
        try {
            if (lastFiveMessage.size() > 0) {
                for (String current : lastFiveMessage) {
                    out.write(current);
                    out.flush();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            readLock.unlock();
        }
    }

    private void sendMessageToClients(String message) {
        try {
            for (ClientSession currentClientSession : Server.getClientSessionList()) {
                if (currentClientSession != this) {
                    currentClientSession.out.write(clientName + ": " + message + "\n");
                    currentClientSession.out.flush();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void addMessageInQueue(String message) {
        writeLock.lock();
            lastFiveMessage.add(clientName + ": " + message + "\n");
            if (lastFiveMessage.size() > 5) {
                lastFiveMessage.remove();
            }
        writeLock.unlock();
    }

    private synchronized void writeMessageInHistoryFile(String message) {
        try {
            fileWriter.write(clientName + ": " + message + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}