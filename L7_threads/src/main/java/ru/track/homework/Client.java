package ru.track.homework;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final int port;
    private final String host;

    private Thread writer;
    private Thread reader;

    public Client(@NotNull String host, int port) {
        this.port = port;
        this.host = host;
    }

    private void connect()
    {
        try {
            Socket socket = new Socket(host, port);
            final Socket finalSocket = socket;

            writer = new Thread(() -> { System.out.println("writer > started"); writeToSocket(finalSocket); });
            writer.setName("writer");
            writer.start();

            reader = new Thread(() -> { System.out.println("reader > started"); readFromSocket(finalSocket); });
            reader.setName("reader");
            reader.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromSocket(@NotNull Socket socket) {
        try {
            final InputStream in = socket.getInputStream();
            final byte[] buffer = new byte[4096];

            for (int nRead = 0; nRead != -1; nRead = in.read(buffer)) {
                if (nRead == 0) continue;

                System.out.println(new String(buffer, 0, nRead));
            }
            socket.shutdownInput();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }

    private void writeToSocket(@NotNull Socket socket) {
        try{
            final OutputStream out = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String line = scanner.nextLine();
                if ("exit".equals(line)) {
                    System.out.println("bye!");
                    break;
                }

                out.write(line.getBytes());
                out.flush();
            }
            socket.shutdownOutput();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }


    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost",9000);
        client.connect();
    }
}
