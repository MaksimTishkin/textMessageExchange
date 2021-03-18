package com.epam.tishkin.clients;

public class firstClient extends Client {

    public static void main(String[] args) {
        Client client = new firstClient();
        client.reader.start();
        client.writer.start();
    }
}
