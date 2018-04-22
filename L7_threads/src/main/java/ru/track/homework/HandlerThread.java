package ru.track.homework;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HandlerThread extends Thread {

    private Socket socket;
    private int clientID;

    private static int counterID = 0;

    HandlerThread(@NotNull Socket socket) {
        this.socket = socket;
        clientID = counterID++;

        setName(String.format("Client[%d]@%s:%s", clientID, socket.getInetAddress(), socket.getPort()));
    }

    public void run() {
        final byte[] buffer = new byte[4096];

        try
        {
            final InputStream in = socket.getInputStream();
            final OutputStream out = socket.getOutputStream();

            for (int nRead = 0; nRead != -1; nRead = in.read(buffer)) {
                if (nRead == 0) continue;
                System.out.println(String.format("\t%s > %s", getName(), new String(buffer, 0, nRead)));
                out.write(buffer, 0, nRead);
                out.flush();
            }

            socket.shutdownInput();
            socket.shutdownOutput();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(String.format("main > client disconnected: %s", getName()));
            IOUtils.closeQuietly(socket);
        }
    }
}
