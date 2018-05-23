package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import dao.UsersDAO;
import dataSets.UsersDataSet;

public class RemoteDbExample {

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection(1); // FIXME
        if (connection == null) {
            return;
        }


        Statement statement = connection.createStatement();
        statement.execute("create table if not exists users  (id bigint auto_increment, user_name varchar(256), primary key (id))");

        UsersDAO usersDAO = new UsersDAO(connection);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            UsersDataSet user = new UsersDataSet(line);
            boolean created = usersDAO.create(user);
            System.out.println("Created: " + user);
        }

    }


    public static Connection getConnection(int domain) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" +        //db type
                    "tdb-" + domain + ".trail5.net:" +    //host name
                    "3306/" +                //port
                    "track17?" +             //db name
                    "user=track_student&" +  //login
                    "password=7EsH.H6x";
            return DriverManager.getConnection(url    //password
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
