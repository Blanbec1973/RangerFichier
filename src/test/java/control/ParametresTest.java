package control;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParametresTest {
    private Parametres parametres;

    @BeforeAll
    void setUpBeforeClass() throws Exception {
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
    @Test()
    void testGetNomFichierBase2() {
        assertDoesNotThrow(()->new Parametres("config2properties"));
    }
}