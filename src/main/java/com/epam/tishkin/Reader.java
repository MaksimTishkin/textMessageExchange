package com.epam.tishkin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader extends Thread {
    Socket socket;

    public Reader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String message;
        try (BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            do {
                message = read.readLine();
                System.out.println(message);
            } while (!"exit".equals(message));
        } catch (IOException e) {
            System.out.println("Проблема в Reader");
        }
    }
}
