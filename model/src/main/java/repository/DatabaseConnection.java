package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {
    private Properties properties;
    private Connection instance = null;
    private static final Logger logger = LogManager.getLogger();

    public DatabaseConnection(Properties properties){
        this.properties = properties;
    }

    private Connection getNewConnection() {
        logger.traceEntry();
        String url  = properties.getProperty("basketball.jdbc.url");
        Connection connection = null;

        logger.info("trying to connect to database ... {}", url);
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }
        return connection;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(instance);
        return instance;
    }
}