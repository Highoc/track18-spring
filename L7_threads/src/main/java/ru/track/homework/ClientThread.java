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
import java.util.concurrent.atomic.AtomicLong;

public class ClientThread extends Thread {
    private final long clientID;
    private String username;

    private static AtomicCounter counter = new AtomicCounter();

    private static List<Message> messages;
    private static List<ClientThread> clients;

    private final Socket socket;

    private InputStream in;
    private OutputStream out;

    private static Gson gson = new Gson();

    public ClientThread(@NotNull Socket socket, @NotNull List<ClientThread> clients, @NotNull List<Message> messages) {
            this.clientID = counter.inc();
            this.socket = socket;

            this.clients = clients;
            this.messages = messages;

            setName(String.format("Client[%d]@%s:%s", clientID, socket.getInetAddress(), socket.getPort()));
    }

    public void run() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            final byte[] buffer = new byte[4096];

            int nUsername = in.read(buffer);
            username = new String(buffer, 0, nUsername);

            for (int nRead = 0; !isInterrupted() || (nRead != -1); nRead = in.read(buffer)) {
                while (in.available() == 0) { sleep(200); }
                if (nRead == 0) continue;

                Message message = gson.fromJson(new String(buffer, 0, nRead), Message.class);
                messages.add(message);
                System.out.println(String.format("%s[%s]> %s", getName(), message.getUsername(), message.getText()));
            }

            socket.shutdownInput();
            socket.shutdownOutput();

        } catch (IOException | InterruptedException e) {
            //throw new RuntimeException(e);
        } finally {
            clients.remove(this);
            System.out.println(String.format("main> client disconnected: %s", getName()));
            IOUtils.closeQuietly(socket);
        }
    }

    public void send(Message message) throws IOException {
        out.write(gson.toJson(message).getBytes());
        out.flush();
    }

    public String getUsername() { return username; }
    public long getClientID() { return clientID; }
}

class AtomicCounter {
    private AtomicLong val = new AtomicLong(0);
    public long inc() {
        return val.getAndIncrement();
    }
}
