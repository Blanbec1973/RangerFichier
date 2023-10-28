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
        String tempString = parametres.getProperty("MsgErrNoFile");
        assertNotNull(tempString);
        tempString = parametres.getProperty("version");
        assertNotNull(tempString);
        tempString = parametres.getProperty("url");
        assertNotNull(tempString);
        tempString = parametres.getProperty("MsgNotFound");
        assertNotNull(tempString);
        tempString = parametres.getProperty("MsgCopyDeb");
        assertNotNull(tempString);
        tempString = parametres.getProperty("MsgVers");
        assertNotNull(tempString);
    }

    @Test
    void testGetNomFichierBase2() {
        assertThrows(Exception.class, ()->new Parametres("config2properties"));
    }
}