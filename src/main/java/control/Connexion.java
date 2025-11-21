package control;

import exceptions.DatabaseAccessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Connexion implements AutoCloseable {
    private final String url;
    private Connection conn = null;
     private Statement statement=null;
    private ResultSet resultSet=null;
    private static final Logger logger = LogManager.getLogger(Connexion.class);
    public Connexion(String url) {
        this.url = url;
    }
    public Connection getConn() {return conn;}
    public Statement getStatement() {return statement;}
    public void connect() {
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connection to SQLite has been established.");
            statement= conn.createStatement();

        } catch (SQLException e) {
            logger.error("Erreur de connexion à la base : {}", e.getMessage());
            throw new DatabaseAccessException("Impossible de se connecter à la base", e);

        }
    }
    public ResultSet getResultSet() {return resultSet;}

    @Override
    public void close() {
        try {
            if (conn != null) {
                conn.close();
                logger.info("Connection closed.");
            }
        } catch (SQLException ex) {
            logger.error("Erreur lors de la fermeture de la connexion : {}", ex.getMessage());
            throw new DatabaseAccessException("Erreur lors de la fermeture de la connexion", ex);
        }
    }
    public void query(String sql) {
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("Erreur dans la requête : {}", sql);
            throw new DatabaseAccessException("Erreur lors de l'exécution de la requête", e);

        }
    }

}

