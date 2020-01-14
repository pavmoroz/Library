package Database;

//import oracle.jdbc.pool.OracleDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyDataSource {

//    private static OracleDataSource dataSource;

//    static {
//        Properties properties = new Properties();
//
//        try (FileInputStream f = new FileInputStream("dbProperties.txt")) {
//            properties.load(f);
//            dataSource = new OracleDataSource();
//            dataSource.setURL(properties.getProperty("DB_URL"));
//            dataSource.setUser(properties.getProperty("DB_USERNAME"));
//            dataSource.setPassword(properties.getProperty("DB_PASSWORD"));
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private MyDataSource() {
    }

    public static Connection getConnection() throws SQLException {
        String databaseURL = "jdbc:derby:library;create=true";
        return DriverManager.getConnection(databaseURL);
//        return dataSource.getConnection();
    }
}
