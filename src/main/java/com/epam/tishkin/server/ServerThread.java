package com.epam.tishkin.server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    String clientName;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    BufferedWriter fileWriter;

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Enter your name" + "\n");
            out.flush();
            clientName = in.readLine();
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
                        currentServerThread.out.write(clientName + ":" + clientMessage + "\n");
                        currentServerThread.out.flush();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Проблема в методе ран ServerThread");
        }
    }
}
