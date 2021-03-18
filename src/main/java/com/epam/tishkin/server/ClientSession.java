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

    public ClientSession(Socket socket) {
        try {
            fileWriter = new FileWriter("history.txt", true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientName = getClientName();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getClientName() throws IOException {
        out.write("Enter your name" + "\n");
        out.flush();
        return in.readLine();
    }

    private void sendLastFiveMessage() throws IOException {
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
    }

    private void addMessageInQueue(String message) throws IOException {
        writeLock.lock();
        try {
            lastFiveMessage.add(clientName + ": " + message + "\n");
            if (lastFiveMessage.size() > 5) {
                lastFiveMessage.remove();
            }
        } finally {
            writeLock.unlock();
        }
    }

    private synchronized void writeMessageInHistoryFile(String message) throws IOException {
        fileWriter.write(clientName + ": " + message + "\n");
        fileWriter.flush();
    }

    private void sendMessageToClients(String message) throws IOException {
        for (ClientSession currentClientSession : Server.getClientSessionList()) {
            if (currentClientSession != this) {
                currentClientSession.out.write(clientName + ": " + message + "\n");
                currentClientSession.out.flush();
            }
        }
    }

    @Override
    public void run() {
        try {
            sendLastFiveMessage();
            while (true) {
                String clientMessage = in.readLine();
                if ("exit".equals(clientMessage)) {
                    out.write(clientMessage + "\n");
                    out.flush();
                    break;
                }
                sendMessageToClients(clientMessage);
                addMessageInQueue(clientMessage);
                writeMessageInHistoryFile(clientMessage);
            }
        } catch(IOException e){
            logger.error(e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                fileWriter.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
