package ru.track.homework;

import org.jetbrains.annotations.NotNull;

class Message {
    private String username;
    private String text;
    private long ts;

    Message(@NotNull String username, @NotNull String text) {
        this(username, text, System.currentTimeMillis());
    }

    Message(@NotNull String username, @NotNull String text, long ts) {
        this.username = username;
        this.ts = ts;
        this.text = text;
    }

    public String getText() { return text; }
    public String getUsername() { return username; }
    public long getTimeStand() { return ts; }
}