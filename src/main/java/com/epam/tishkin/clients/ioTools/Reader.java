package com.epam.tishkin.clients.ioTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader extends Thread {
    private final Socket socket;

    public Reader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String message;
        try (BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            while (true) {
                message = read.readLine();
                if ("exit".equals(message)) {
                    break;
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Проблема в Reader");
        }
    }
}
