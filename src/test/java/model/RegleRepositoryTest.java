package model;

import org.heyner.common.Parameter;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegleRepositoryTest {

    private Connexion connexion;
    private Parameter parametres;

    @BeforeEach
    void setUp() throws Exception {
        connexion = new Connexion("jdbc:sqlite::memory:");
        connexion.connect();

        // Création de la table et insertion de données de test
        Connection conn = connexion.getConn();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE REGLES (REGEX TEXT, DOSSIERDEST TEXT)");
            stmt.execute("INSERT INTO REGLES (REGEX, DOSSIERDEST) VALUES ('^test.*', '/tmp/')");
        }

        // Mock ou stub de Parameter pour fournir la requête SQL
        parametres = mock(Parameter.class);
        when(parametres.getProperty("sql")).thenReturn("SELECT REGEX, DOSSIERDEST FROM REGLES");
    }

    @AfterEach
    void tearDown() {
        connexion.close();
    }

    @Test
    void testFindAllRegles() {
        RegleRepository repo = new RegleRepository(connexion, parametres);
        List<Regle> regles = repo.findAllRegles();
        assertEquals(1, regles.size());
        assertEquals("^test.*", regles.get(0).regex());
        assertEquals("/tmp/", regles.get(0).dossier());
    }
}