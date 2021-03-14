package com.epam.tishkin;

import java.io.*;
import java.net.Socket;

public class Writer extends Thread {
    Socket socket;

    public Writer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String message;
        try (BufferedWriter write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                message = consoleReader.readLine();
                if ("exit".equals(message)) {
                    break;
                }
                write.write(message + "\n");
                write.flush();
            }
        } catch (IOException e) {
            System.out.println("Проблема в Writer");
        }
    }

}
