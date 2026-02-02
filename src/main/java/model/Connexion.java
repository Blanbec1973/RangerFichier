package model;

import exceptions.DatabaseAccessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Connexion implements AutoCloseable {
    private final String url;

    private Connection conn = null;
    private static final Logger logger = LogManager.getLogger(Connexion.class);

    public Connexion(String url) {
        this.url = url;
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connection to SQLite has been established.");

        } catch (SQLException e) {
            logger.error("Unable to connect to SGBD : {}", e.getMessage());
            throw new DatabaseAccessException("Unable to connect to SGBD", e);

        }
    }
    public Connection getConn() {
        return conn;
    }

    @Override
    public void close() {
        try {
            if (conn != null) {
                conn.close();
                logger.info("Connection closed.");
            }
        } catch (SQLException ex) {
            logger.error("Error while closing connexion : {}", ex.getMessage());
            throw new DatabaseAccessException("Error while closing connexion", ex);
        }
    }
}

