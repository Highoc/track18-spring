package ru.track.homework;

import org.jetbrains.annotations.NotNull;

class Message {
    private String senderName;
    private String data;
    private long ts;

    public Message(@NotNull String senderName, @NotNull String data, long ts)
    {
        this.senderName = senderName;
        this.ts = ts;
        this.data = data;
    }

    public String getData() { return data; }
    public String getSenderName() { return senderName; }
    public void setSenderName(@NotNull String senderName) { this.senderName = senderName; }
    public long getTimeStand() { return ts; }
}