package db.database;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTools {
    /**
     * Подключение к БД.
     * @return builded Connection
     */
    public static Connection configureDBConnection() {
        try {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setServerName("localhost");
            ds.setDatabaseName("vk_data_analysis");
            ds.setUser("postgres");
            ds.setPassword("postgres");
            ds.setPortNumber(5432);
            System.out.println("Connection is opened!");
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
