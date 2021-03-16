package com.epam.tishkin.clients.ioTools;

import java.io.*;
import java.net.Socket;

public class Writer extends Thread {
    private final Socket socket;

    public Writer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String message;
        try (BufferedWriter write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            do {
                message = consoleReader.readLine();
                write.write(message + "\n");
                write.flush();
            } while (!"exit".equals(message));
        } catch (IOException e) {
            System.out.println("Проблема в Writer");
        }
    }
}