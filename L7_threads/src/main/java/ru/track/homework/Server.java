package ru.track.homework;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

//Не пробрасывать id на клиент
//Корректная работа со списком клиентов
//Админская консоль

public class Server {
    private final int port;
    private final int backlog;

    private List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    private List<ClientThread> clients = Collections.synchronizedList(new ArrayList<>());
    private Thread broadcasterThread = new Thread(() -> { broadcast(); });
    private Thread adminConsoleThread = new Thread(() -> { adminConsole(); });

    public Server(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    public void serve() throws IOException {
        ServerSocket serverSocket = null;
        try {
            final ServerSocket serverSocketFinal = new ServerSocket(port, backlog, InetAddress.getByName("localhost"));
            serverSocket = serverSocketFinal;

            broadcasterThread.start();
            adminConsoleThread.start();

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

            ClientThread client = new ClientThread(socket, messages);
            clients.add(client);
            client.start();

            System.out.println(String.format("main> new client connected: %s", client.getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcast()
    {
        BufferedInputStream
        try {
            Date date = new Date();
            long lastUpdateTime = date.getTime();

            while (true) {
                synchronized (messages) {
                    for (Message messsage: messages) {
                        if (messsage.getTimeStand() >= lastUpdateTime)
                        {
                            for (ClientThread client: clients) {
                                if (!client.isAlive()) {
                                    clients.remove(client);
                                }

                                if (!client.getName().equals(messsage.getSenderName()))
                                {
                                    client.send(messsage);
                                }
                            }
                        }
                    }
                    date = new Date();
                    lastUpdateTime = date.getTime();
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void adminConsole()
    {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();
            if ("exit".equals(line)) {
                System.out.println("bye!");
                break;
            }

            if ("list".equals(line)) {
                synchronized (clients){
                    System.out.println("Client list:");
                    if (clients.isEmpty()) {
                        System.out.println("empty");
                    }

                    for (ClientThread client: clients){
                        if (!client.isAlive()) {
                            System.out.print("-");
                            clients.remove(client);
                        } else {
                            System.out.print("+");
                        }

                        System.out.println(client.getName());

                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9000, 10);
        server.serve();
    }
}
