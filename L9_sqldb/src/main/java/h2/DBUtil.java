package h2;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
public class DBUtil {

    public static void printConnection(Connection connection) throws SQLException {
        System.out.println(connection.getMetaData().getURL());
        System.out.append("Autocommit: " + connection.getAutoCommit() + '\n');
        System.out.append("DB name: " + connection.getMetaData().getDatabaseProductName() + '\n');
        System.out.append("DB version: " + connection.getMetaData().getDatabaseProductVersion() + '\n');
        System.out.append("Driver name: " + connection.getMetaData().getDriverName() + '\n');
        System.out.append("Driver version: " + connection.getMetaData().getDriverVersion() + '\n');

    }
}
