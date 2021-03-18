package com.epam.tishkin.clients;

import com.epam.tishkin.clients.ioTools.Reader;
import com.epam.tishkin.clients.ioTools.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public abstract class Client {
    final static Logger logger = LogManager.getLogger(Client.class);
    protected Reader reader;
    protected Writer writer;

    public Client() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/main/resources/config.properties"));
            String localHost = properties.getProperty("localHost");
            int port = Integer.parseInt(properties.getProperty("PORT"));
            Socket socket = new Socket(localHost, port);
            reader = new Reader(socket);
            writer = new Writer(socket);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
