package com.epam.tishkin;

import com.epam.tishkin.server.ClientSession;
import com.epam.tishkin.server.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TextMessageExchangeTest {
    static Socket socket;
    ByteArrayOutputStream out;
    ByteArrayInputStream in;
    ClientSession server;

    @BeforeAll
    static void initAll() throws IOException {
        socket = Mockito.mock(Socket.class);
    }

    @Test
    public void testSetClientName() throws IOException {
        String expectedClientName = "Maxim";
        in = new ByteArrayInputStream(expectedClientName.getBytes());
        out = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream()).thenReturn(in);
        Mockito.when(socket.getOutputStream()).thenReturn(out);
        server = new ClientSession(socket);
        server.setClientName();
        Assertions.assertEquals(expectedClientName, server.getClientName());
    }

    @Test
    public void testSendLastFiveMessage() throws IOException {
        String expectedFirstMessage = "Hello" + "\n";
        String expectedSecondMessage = "world" + "\n";
        in = new ByteArrayInputStream("".getBytes());
        out = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream()).thenReturn(in);
        Mockito.when(socket.getOutputStream()).thenReturn(out);
        server = new ClientSession(socket);
        server.sendLastFiveMessage();
        Assertions.assertEquals("", out.toString());
        server.getLastFiveMessage().add(expectedFirstMessage);
        server.getLastFiveMessage().add(expectedSecondMessage);
        server.sendLastFiveMessage();
        Assertions.assertEquals(expectedFirstMessage + expectedSecondMessage, out.toString());
    }

    @Test
    public void testSendMessageToClients() throws IOException {
        String message = "Hello";
        in = new ByteArrayInputStream("".getBytes());
        out = new ByteArrayOutputStream();
        Mockito.when(socket.getInputStream()).thenReturn(in);
        Mockito.when(socket.getOutputStream()).thenReturn(out);
        server = new ClientSession(socket);
        Server.getClientSessionList().add(server);
        server.sendMessageToClients(message);
        String expectedMessage = "";
        Assertions.assertEquals(expectedMessage, out.toString());

        Socket otherSocket = Mockito.mock(Socket.class);
        ByteArrayInputStream otherIn = new ByteArrayInputStream("".getBytes());
        ByteArrayOutputStream otherOut = new ByteArrayOutputStream();
        Mockito.when(otherSocket.getInputStream()).thenReturn(otherIn);
        Mockito.when(otherSocket.getOutputStream()).thenReturn(otherOut);
        ClientSession clientSession = new ClientSession(otherSocket);
        Server.getClientSessionList().add(clientSession);
        server.sendMessageToClients(message);
        expectedMessage = clientSession.getClientName() + ": " + message + "\n";
        Assertions.assertEquals(expectedMessage, otherOut.toString());
    }
}
