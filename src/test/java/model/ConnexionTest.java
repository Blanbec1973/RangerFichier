package model;

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
}
