package control;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
@Order(1)
class CatalogueTest {
    private final ResultSet resultSet = Mockito.mock(ResultSet.class);

    @Test
    void getInstance() {
        assertNotNull(Catalogue.getInstance());
        assertNotNull(Catalogue.getInstance());
    }

    @Test
    void remplir() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("REGEX")).thenReturn("^NDF.*\\.(pdf|jpg|JPG|png)$");
        when(resultSet.getString("DOSSIERDEST")).thenReturn("Dossier");
        Catalogue.getInstance().remplir(resultSet);

        assertEquals(1, Catalogue.getInstance().getTailleCatalogue());

    }
    @Test
    void searchTargetDirectory() {
        assertNull(Catalogue.getInstance().searchTargetDirectory("Toto"));
        assertEquals("Dossier", Catalogue.getInstance().searchTargetDirectory("NDF Richard.pdf"));
    }



}