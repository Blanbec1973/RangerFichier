package control;

import org.junit.jupiter.api.*;
import java.sql.SQLException;


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
        Assertions.assertTrue(nbLignes>10);
    }
}