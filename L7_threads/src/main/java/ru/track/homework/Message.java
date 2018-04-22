package ru.track.homework;

class Message {
    private long ts;
    private String data;

    public Message(long ts, String data)
    {
        this.ts = ts;
        this.data = data;
    }
}