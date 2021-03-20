package com.epam.tishkin.clients;

import com.epam.tishkin.clients.ioTools.Reader;
import com.epam.tishkin.clients.ioTools.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.util.Properties;

public abstract class Client {
    protected static Properties properties = new Properties();
    protected Reader reader;
    protected Writer writer;
    protected Socket socket;
    final static Logger logger = LogManager.getLogger(Client.class);

    public Client(Socket socket) {
        this.socket = socket;
        reader = new Reader(socket);
        writer = new Writer(socket);
        reader.start();
        writer.start();
    }
}
