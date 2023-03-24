package controleur;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParametresTest {
    private static Parametres parametres;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        parametres = new Parametres("config.properties");
    }

    @Test
    void testGetProperty() {
        String nomFichier = parametres.getProperty("MsgErrNoFile");
        assertNotNull(nomFichier);
    }

    @Test
    void testGetNomFichierBase2() {
        assertThrows(Exception.class, ()->new Parametres("config2properties"));
    }
}