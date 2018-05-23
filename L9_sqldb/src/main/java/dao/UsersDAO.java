package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataSets.UsersDataSet;

public class UsersDAO {
    public static final String SQL_INSERT = "INSERT INTO users (user_name) VALUES (?);";

    private Connection con;

    public UsersDAO(Connection con) {
        this.con = con;
    }

    public boolean create(UsersDataSet user) throws SQLException {
        Statement stmt = null;
        try {

            PreparedStatement preparedStatement = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            int update = preparedStatement.executeUpdate();
            if (update != 1) {
                return false;
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return true;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public UsersDataSet get(long id) throws SQLException {

        UsersDataSet user = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            stmt.execute("select * from users where id=" + id);
            rs = stmt.getResultSet();
            while (rs.next()) {
                long userId = rs.getLong("id");
                String userName = rs.getString("user_name");
                user = new UsersDataSet(userId, userName);
                break;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return user;
    }
}
