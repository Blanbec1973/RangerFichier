package model;

import exceptions.DatabaseAccessException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConnexionTest {
    private static Connexion connexion;

    @BeforeAll
    static void  beforeAll() {
        connexion = new Connexion("jdbc:sqlite:src/main/resources/RangerFichier.db");
    }
    @AfterAll
    static void afterAll() {
        connexion.close();
    }
    @Test
    @Order(1)
    void connect() {
        connexion.connect();
        Assertions.assertNotNull(connexion.getConn());
        Assertions.assertNotNull(connexion.getStatement());
    }
    @Test
    @Order(2)
    void query() {
        connexion.query("Select count(*) from REGLES;");
        Assertions.assertNotNull(connexion.getResultSet());
    }
    @Test
    @Order(3)
    void getResultSet() throws SQLException {
        connexion.getResultSet().next();
        int nbLignes = connexion.getResultSet().getInt(1);
        System.out.println("nblignes : "+nbLignes);
        assertTrue(nbLignes>10);
    }
    @Test
    void testConnectWithInvalidUrl() {
        Connexion connexion2 = new Connexion("jdbc:sqlite:/invalid/path/to/db");
        DatabaseAccessException exception = assertThrows(DatabaseAccessException.class, connexion2::connect);
        assertTrue(exception.getMessage().contains("Impossible de se connecter"));
    }

    @Test
    void testCloseWithoutConnect() {
        Connexion connexion2 = new Connexion("jdbc:sqlite:src/main/resources/RangerFichier.db");
        // Should not throw exception even if not connected
        assertDoesNotThrow(connexion2::close);
    }

    @Test
    void testQueryInvalidSql() {
        Connexion connexion2 = new Connexion("jdbc:sqlite:src/main/resources/RangerFichier.db");
        connexion2.connect();
        DatabaseAccessException exception = assertThrows(DatabaseAccessException.class,
                () -> connexion2.query("SELECT * FROM table_inexistante"));
        assertTrue(exception.getMessage().contains("Erreur lors de l'exécution"));
        connexion2.close();
    }
    @Test
    void testCloseAfterConnect() {
        Connexion connexion2 = new Connexion("jdbc:sqlite:src/main/resources/RangerFichier.db");
        connexion2.connect();
        assertDoesNotThrow(connexion2::close);
    }

    @Test
    void testInitialState() {
        Connexion connexion2 = new Connexion("jdbc:sqlite:src/main/resources/RangerFichier.db");
        assertNull(connexion2.getConn());
        assertNull(connexion2.getStatement());
    }
    @Test
    void testQueryBeforeConnect() {
        Connexion connexion2 = new Connexion("jdbc:sqlite:src/main/resources/RangerFichier.db");
        assertThrows(DatabaseAccessException.class,
                () -> connexion2.query("SELECT * FROM REGLES"));
    }
    @Test
    void testCloseMultipleTimes() {
        Connexion connexion2 = new Connexion("jdbc:sqlite:src/main/resources/RangerFichier.db");
        connexion2.connect();
        connexion2.close();
        assertDoesNotThrow(connexion2::close);
    }
}


