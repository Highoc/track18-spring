package ru.track.homework;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Scanner;

public class AdminThread extends Thread {
    private List<ClientThread> clients;

    AdminThread(@NotNull List<ClientThread> clients) {
        this.clients = clients;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (!isInterrupted()) {
            String line = scanner.next();

            if (line.equals("list")) {
                synchronized (clients){
                    System.out.println("Client list:");

                    if (clients.isEmpty()) {
                        System.out.println("empty");
                        continue;
                    }

                    for (ClientThread client: clients){
                        if (!client.isInterrupted()) {
                            System.out.println(String.format("%s[%s]", client.getName(), client.getUsername()));
                        }
                    }
                }
            }
            else if (line.equals("drop")) {
                long clientId = scanner.nextLong();

                synchronized (clients){
                    for (ClientThread client: clients){
                        if (!client.isInterrupted() && (client.getClientID() == clientId)) {
                            client.interrupt();
                            break;
                        }
                    }
                }

            }
        }
    }
}