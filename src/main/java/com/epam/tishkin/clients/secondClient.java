package com.epam.tishkin.clients;

public class secondClient extends Client {

    public static void main(String[] args) {
        Client client = new secondClient();
        client.reader.start();
        client.writer.start();
    }
}
