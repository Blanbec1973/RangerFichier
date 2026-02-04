package model;

import exceptions.DatabaseAccessException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConnexionTest {

    @Test
    void testConnectAndClose() {
        // Utilise une base SQLite en mémoire pour le test
        Connexion connexion = new Connexion("jdbc:sqlite::memory:");
        assertDoesNotThrow(connexion::connect);
        assertNotNull(connexion.getConn());
        assertDoesNotThrow(connexion::close);
    }
    @Test
    void testConnectAndMultipleClose() {
        // Utilise une base SQLite en mémoire pour le test
        Connexion connexion = new Connexion("jdbc:sqlite::memory:");
        assertDoesNotThrow(connexion::connect);
        assertNotNull(connexion.getConn());
        assertDoesNotThrow(connexion::close);
        assertDoesNotThrow(connexion::close);
    }

    @Test
    void testConnectWithInvalidUrl() {
        Connexion connexion = new Connexion("jdbc:invalid:badurl");
        Exception exception = assertThrows(DatabaseAccessException.class, connexion::connect);
        assertTrue(exception.getMessage().contains("Unable to connect to SGBD"));
    }

    @Test
    void testConnectWithEmptyUrl() {
        Connexion connexion = new Connexion("");
        Exception exception = assertThrows(DatabaseAccessException.class, connexion::connect);
        assertTrue(exception.getMessage().contains("Unable to connect to SGBD"));
    }
}
