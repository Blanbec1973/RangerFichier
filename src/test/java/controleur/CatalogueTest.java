package controleur;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CatalogueTest {
    private ResultSet resultSet = Mockito.mock(ResultSet.class);
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
        assertNotNull(Catalogue.getInstance());
    }

    @Test
    void remplir() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("REGEX")).thenReturn("^NDF.*\\.(pdf|jpg|JPG|png)$");
        when(resultSet.getString("DOSSIERDEST")).thenReturn("Dossier");
        Catalogue.remplir(resultSet);

        assertEquals(1, Catalogue.getTailleCatalogue());

    }
    @Test
    void searchTargetDirectory() {
        // TODO Create test searchTargetDirectory
        assertNull(Catalogue.searchTargetDirectory("Toto"));
        assertEquals("Dossier", Catalogue.searchTargetDirectory("NDF Richard.pdf"));
    }


}