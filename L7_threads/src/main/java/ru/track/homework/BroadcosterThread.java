package ru.track.homework;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class BroadcosterThread extends Thread {
    private static ConversationService messages;
    private static List<ClientThread> clients;

    private static long lastUpdateTS = System.currentTimeMillis();

    BroadcosterThread(@NotNull List<ClientThread> clients, @NotNull ConversationService messages) {
        BroadcosterThread.messages = messages;
        BroadcosterThread.clients = clients;
    }

    public void run()
    {
        try {
            while (!isInterrupted()) {
                long currentTS = System.currentTimeMillis();
                List<Message> newMessages = messages.getHistory(lastUpdateTS, currentTS, 20);
                lastUpdateTS = currentTS;

                for (Message newMessage: newMessages) {
                    synchronized (clients) {
                        for (ClientThread client: clients) {
                            if (!client.isInterrupted() && !client.getUsername().equals(newMessage.getUsername())) {
                                try {
                                    client.send(newMessage);
                                } catch (IOException e) {
                                    client.interrupt();
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
}
