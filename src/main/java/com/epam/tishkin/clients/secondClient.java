package com.epam.tishkin.clients;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class secondClient extends Client {

    public secondClient(Socket socket) {
        super(socket);
    }

    public static void main(String[] args) {
        try (FileReader readerForProperties = new FileReader("src/main/resources/config.properties")) {
            properties.load(readerForProperties);
            String localHost = properties.getProperty("localHost");
            int port = Integer.parseInt(properties.getProperty("PORT"));
            Client client = new secondClient(new Socket(localHost, port));
            client.reader.start();
            client.writer.start();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
