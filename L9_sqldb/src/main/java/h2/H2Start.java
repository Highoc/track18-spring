package h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.h2.tools.Server;

/**
 *
 */
public class H2Start {
    public static void main(String[] args) throws ClassNotFoundException {
        // Запускаем локальный сервер СУБД H2

        try {
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
            webServer.start();
        } catch (SQLException e) {
            System.out.println("Failed to start server");
            return;
        }


        // Загружаем драйвер в JVM
        Class.forName("org.h2.Driver");
        // URL к бд
        // jdbc - префикс протокола jdbc
        // h2 - префикс уникальный для каждой бд (Может быть mysql)
        // ~/test - путь к БД (может быть URl + port)
        String url = "jdbc:h2:~/test";

        Connection connection = null;
        createInsertSelect(url, connection);
//        prepared(url, connection);
    }


    // Нужен primaryKey
    private static void prepared(String url, Connection connection) {
        try {
            connection = DriverManager.getConnection(url, "sa", "");
            DBUtil.printConnection(connection);

            Statement stmt = connection.createStatement();
            stmt.execute("DROP TABLE dbuser");


            // Создание таблицы
            stmt = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS dbuser("
                    + "user_id NUMBER(5) auto_increment NOT NULL, "
                    + "username VARCHAR(20) NOT NULL, "
                    + "PRIMARY KEY (user_id)"
                    + ")";

            boolean executed = stmt.execute(createTableSQL);
            System.out.println("CREATE TABLE: " + executed);


            PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO dbuser (username) VALUES (?);");
            List<String> list = Arrays.asList("A", "B", "C");
            for (String name : list) {
                insertStmt.setString(1, name);
                System.out.println(insertStmt.toString());
                insertStmt.executeUpdate();
            }



            PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM dbuser WHERE user_id > ?;");
            selectStmt.setInt(1, 1);
            System.out.println(selectStmt.toString());
            ResultSet resultSet = selectStmt.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");// or by index =1
                String username = resultSet.getString("username");// or by index =2
                System.out.println("Row: " + userId + ", " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Тут уже ничего не сделать
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createInsertSelect(String url, Connection connection) {
        try {
            // Получение соединения, дальнейшая работа ведется с объектом Connection
            connection = DriverManager.getConnection(url, "sa", "");
            DBUtil.printConnection(connection);


            // Создание таблицы
            Statement stmt = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS dbuser("
                    + "user_id NUMBER(5) NOT NULL, "
                    + "username VARCHAR(20) NOT NULL, "
//                    + "PRIMARY KEY (user_id)"
                    + ")";

            boolean executed = stmt.execute(createTableSQL);
            System.out.println("CREATE TABLE: " + executed);

            // Вставка
            String insertSQL = "INSERT INTO dbuser (user_id, username) VALUES (1, 'test1');";
            stmt.executeUpdate(insertSQL);
            insertSQL = "INSERT INTO dbuser (user_id, username) VALUES (2, 'test2');";
            stmt.executeUpdate(insertSQL);

            // Запрос
            String selectSQL = "SELECT * FROM dbuser";
            ResultSet resultSet = stmt.executeQuery(selectSQL);
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");// or by index =1
                String username = resultSet.getString("username");// or by index =2
                System.out.println("Row: " + userId + ", " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Тут уже ничего не сделать
                    e.printStackTrace();
                }
            }
        }
    }
}