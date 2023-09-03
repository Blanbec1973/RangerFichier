package controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Connexion {
    private final String url;
    private Connection conn = null;
    private Statement statement=null;
    private ResultSet resultSet=null;
    private static final Logger logger = LogManager.getLogger(Connexion.class);
    public Connexion(String url) {
        this.url = url;
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connection to SQLite has been established.");
            statement= conn.createStatement();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    public ResultSet getResultSet() {
        return resultSet;
    }
    public void close() {
        try {
            if (conn != null) {
                conn.close();
                logger.info("Connection closed.");
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void query(String sql) {
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Erreur dans la requête : {}", sql);
            Thread.currentThread().interrupt();
        }
    }

}

