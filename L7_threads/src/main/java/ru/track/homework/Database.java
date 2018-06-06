package ru.track.homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database implements ConversationService {

    private Connection[] connections = new Connection[3];

    Database() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            connections[0] = connect(1);
            connections[1] = connect(2);
            connections[2] = connect(3);
        } catch (SQLException |IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized long store(Message msg) {
        Connection currentConnection = getConnection(msg.getUsername());

        try {
            String insert = "INSERT INTO messages (user_name, text, ts) VALUES (?, ?, ?)";
            PreparedStatement stmt = currentConnection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, msg.getUsername());
            stmt.setString(2, msg.getText());
            stmt.setTimestamp(3, new java.sql.Timestamp(msg.getTimeStand()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Storing message failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Storing message failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized List<Message> getHistory(long from, long to, long limit) {
        List<Message> result = new ArrayList<>();

        try {
            for (Connection currentConnection : connections) {
                String select = "SELECT * FROM messages WHERE ts >= ? AND ts < ? ORDER BY ts LIMIT ?";
                PreparedStatement stmt = currentConnection.prepareStatement(select);
                stmt.setTimestamp(1, new java.sql.Timestamp(from));
                stmt.setTimestamp(2, new java.sql.Timestamp(to));
                stmt.setLong(3, limit);

                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    result.add(new Message(resultSet.getString(2), resultSet.getString(3), resultSet.getLong(4)));
                }
            }

            result.sort((a, b) -> (int) (a.getTimeStand() - b.getTimeStand()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public synchronized List<Message> getByUser(String username, long limit) {
        Connection currentConnection = getConnection(username);
        List<Message> result = new ArrayList<>();

        try {
            String select = "SELECT * FROM messages WHERE user_name = '?' LIMIT ? ORDER BY ts";
            PreparedStatement stmt = currentConnection.prepareStatement(select);
            stmt.setString(1, username);
            stmt.setLong(2, limit);

            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                result.add(new Message(resultSet.getString(2), resultSet.getString(3), resultSet.getLong(4)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static Connection connect(int shardID) throws SQLException {
        String url = String.format("jdbc:mysql://tdb-%d.trail5.net:3306/track17?user=track_student&password=7EsH.H6x", shardID);
        return DriverManager.getConnection(url);
    }

    private Connection getConnection(String username) {
        char firstChar = Character.toUpperCase(username.charAt(0));

        Connection currentConnection;
        if (('A' <= firstChar) && (firstChar <= 'J'))
            currentConnection = connections[0];
        else if (('K' <= firstChar) && (firstChar <= 'T'))
            currentConnection = connections[1];
        else if (('U' <= firstChar) && (firstChar <= 'Z'))
            currentConnection = connections[2];
        else currentConnection = null;

        return currentConnection;
    }
}