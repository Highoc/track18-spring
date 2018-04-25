package ru.track.homework;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {

    private final int port;
    private final String host;

    private Thread writer;
    private Thread reader;

    private static Gson gson = new Gson();

    public Client(@NotNull String host, int port) {
        this.port = port;
        this.host = host;
    }

    private void connect()
    {
        try {
            Socket socket = new Socket(host, port);
            final Socket finalSocket = socket;

            writer = new Thread(() -> { writeToSocket(finalSocket); });
            writer.setName("writer");
            writer.start();

            reader = new Thread(() -> { readFromSocket(finalSocket); });
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

                Message message = gson.fromJson(new String(buffer, 0, nRead), Message.class);
                System.out.println(String.format("%s> %s", message.getSenderName(), message.getData()));
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

                Date date = new Date();
                Message message = new Message("", line, date.getTime());
                String result = gson.toJson(message);

                out.write(result.getBytes());
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
