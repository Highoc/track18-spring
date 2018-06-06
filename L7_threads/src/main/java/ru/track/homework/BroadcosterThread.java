package ru.track.homework;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BroadcosterThread extends Thread {
    private static List<Message> messages;
    private static List<ClientThread> clients;

    private static long lastUpdateTS = System.currentTimeMillis();

    public BroadcosterThread(@NotNull List<ClientThread> clients, @NotNull List<Message> messages) {
        this.messages = messages;
        this.clients = clients;
    }

    public void run()
    {
        try {
            while (!isInterrupted()) {
                List<Message> newMessages = getNewMessages();
                for (Message newMessage: newMessages) {
                    synchronized (clients) {
                        for (ClientThread client: clients) {

                            if (client.isInterrupted()) {
                                clients.remove(client);
                                continue;
                            }

                            if (!client.getUsername().equals(newMessage.getUsername())) {
                                try {
                                    client.send(newMessage);
                                } catch (IOException e) {
                                    client.interrupt();
                                    clients.remove(client);
                                }
                            }
                        }
                    }
                }

                Thread.sleep(800);
            }
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        }
    }

    private List<Message> getNewMessages() {
        long nowTS = System.currentTimeMillis();
        List<Message> result = new ArrayList<>();

        synchronized (messages) {
            for (Message messsage: messages) {
                if (messsage.getTimeStand() > lastUpdateTS) {
                    result.add(messsage);
                }
            }
        }
        lastUpdateTS = nowTS;

        return result;
    }
}
