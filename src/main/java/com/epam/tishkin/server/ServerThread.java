package com.epam.tishkin.server;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerThread extends Thread {
    String clientName;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    FileWriter fileWriter;
    final File fileWithHistory = new File("history.txt");
    private final static Queue<String> lastFiveMessage = new ConcurrentLinkedQueue<>();
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    Lock readLock = readWriteLock.readLock();
    Lock writeLock = readWriteLock.writeLock();

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            fileWriter = new FileWriter(fileWithHistory, true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Enter your name" + "\n");
            out.flush();
            clientName = in.readLine();
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
            System.out.println("Что то не так в конструкторе ServerThread");
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
                for (ServerThread currentServerThread : Server.getServerThreadList()) {
                    if (currentServerThread != this) {
                        currentServerThread.out.write(clientName + ": " + clientMessage + "\n");
                        currentServerThread.out.flush();
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
            System.out.println("Проблема в методе ран ServerThread");
        }
    }
}
