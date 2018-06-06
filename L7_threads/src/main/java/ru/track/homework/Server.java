package ru.track.homework;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private final int port;
    private final int backlog;

    private ConversationService messages = new Database();
    private List<ClientThread> clients = Collections.synchronizedList(new ArrayList<>());

    private BroadcosterThread broadcaster;
    private AdminThread admin;


    public Server(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;

        admin = new AdminThread(clients);
        broadcaster = new BroadcosterThread(clients, messages);
    }

    public void serve() throws IOException {
        ServerSocket serverSocket = null;
        try {
            final ServerSocket serverSocketFinal = new ServerSocket(port, backlog, InetAddress.getByName("localhost"));
            serverSocket = serverSocketFinal;

            broadcaster.start();
            admin.start();

            while (true){
                System.out.println("main> wait to accept new client...");
                handle(serverSocketFinal);
            }

        } finally {
            IOUtils.closeQuietly(serverSocket);
        }
    }

    private void handle(@NotNull ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();

            ClientThread client = new ClientThread(socket, clients, messages);

            clients.add(client);
            client.start();

            System.out.println(String.format("main> new client connected: %s", client.getName()));
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9000, 10);
        server.serve();
    }
}
