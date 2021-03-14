package com.epam.tishkin;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket socket;
    BufferedReader in;
    BufferedWriter out;

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
                    break;
                }
                for (ServerThread currentServerThread : Server.serverThreadList) {
                    currentServerThread.out.write(clientMessage + "\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Проблема в методе ран ServerThread");
        }
    }
}
