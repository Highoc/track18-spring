package ru.track.homework;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Queue;

public class ClientThread extends Thread {

    private final int clientID;
    private static int counterID = 0;

    private static List<Message> messages;
    private final Socket socket;

    private InputStream in;
    private OutputStream out;

    private static Gson gson = new Gson();

    public ClientThread(@NotNull Socket socket, @NotNull List<Message> messages) {
            this.clientID = counterID++;
            this.socket = socket;
            this.messages = messages;
            setName(String.format("Client[%d]@%s:%s", clientID, socket.getInetAddress(), socket.getPort()));
    }

    public void run()
    {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            final byte[] buffer = new byte[4096];

            for (int nRead = 0; nRead != -1; nRead = in.read(buffer)) {
                if (nRead == 0) continue;

                Message message = gson.fromJson(new String(buffer, 0, nRead), Message.class);
                message.setSenderName(getName());
                messages.add(message);
                System.out.println(String.format("%s> %s", message.getSenderName(), message.getData()));
            }

            socket.shutdownInput();
            socket.shutdownOutput();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(String.format("main> client disconnected: %s", getName()));
            IOUtils.closeQuietly(socket);
        }
    }

    public void send(Message message)
    {
        try {
            String result = gson.toJson(message);
            out.write(result.getBytes());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
