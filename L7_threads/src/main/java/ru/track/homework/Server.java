package ru.track.homework;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    private final int port;
    private final int backlog;

    private List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    private List<ClientThread> clients = new ArrayList<>();
    private Thread broadcaster = new Thread(() -> { broadcast(); });;

    public Server(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;

        broadcaster.start();
    }

    public void serve() throws IOException {
        ServerSocket serverSocket = null;
        try {
            final ServerSocket serverSocketFinal = new ServerSocket(port, backlog, InetAddress.getByName("localhost"));
            serverSocket = serverSocketFinal;

            while (true){
                System.out.println("main > wait to accept new client...");
                handle(serverSocketFinal);
            }

        } finally {
            IOUtils.closeQuietly(serverSocket);
        }
    }

    private void handle(@NotNull ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();

            ClientThread client = new ClientThread(socket, messages);
            clients.add(client);
            client.start();

            System.out.println(String.format("main > new client connected: %s", client.getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcast()
    {
        try {
            Date date = new Date();
            long lastUpdateTime = date.getTime();

            while (true) {
                synchronized (messages) {
                    for (Message messsage: messages) {
                        if (messsage.getTimeStand() > lastUpdateTime)
                        {
                            for (ClientThread client: clients) {
                                if (!client.getName().equals(messsage.getSenderName()))
                                {
                                    client.send(messsage);
                                }
                            }
                        }
                    }
                    lastUpdateTime = date.getTime();
                }
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9000, 10);
        server.serve();
    }
}
