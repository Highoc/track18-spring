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

    //private Map<Integer, Client> clients = new HashMap<>();
    private List<Thread> clientThreads = new ArrayList<>();
    private static int counterID = 0;

    class Client {
        private int clientID;
        private Socket socket;

        private Thread reader;
        private Thread writer;

        private Queue<Message> messageQueue;
    }

    public Server(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
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
        Socket socket = null;
        try {
            socket = serverSocket.accept();

            Thread thread = new HandlerThread(socket);
            clientThreads.add(thread);
            System.out.println(String.format("main > new client connected: %s", thread.getName()));
            thread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9000, 10);
        server.serve();
    }
}
