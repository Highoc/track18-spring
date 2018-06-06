package ru.track.homework;

import java.util.List;

public class Database implements ConversationService {

    public Database() {

    }

    @Override
    public synchronized long store(Message msg) {
        return 0;
    }

    @Override
    public synchronized List<Message> getHistory(long from, long to, long limit) {
        return null;
    }

    @Override
    public synchronized List<Message> getByUser(String username, long limit) {
        return null;
    }
}
