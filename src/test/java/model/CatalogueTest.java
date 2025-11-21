package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
class CatalogueTest {
    private final ResultSet resultSet = Mockito.mock(ResultSet.class);

    @BeforeEach
    void resetCatalogue() {
        Catalogue.getInstance().clear();
    }

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
    void searchTargetDirectory() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("REGEX")).thenReturn("^NDF.*\\.(pdf|jpg|JPG|png)$");
        when(resultSet.getString("DOSSIERDEST")).thenReturn("Dossier");
        Catalogue.getInstance().remplir(resultSet);

        assertNull(Catalogue.getInstance().searchTargetDirectory("Toto"));
        assertEquals("Dossier", Catalogue.getInstance().searchTargetDirectory("NDF Richard.pdf"));
    }

    @Test
    void testCatalogueVide() {
        Catalogue.getInstance().remplir(Mockito.mock(ResultSet.class)); // vide
        assertEquals(0, Catalogue.getInstance().getTailleCatalogue());
        assertNull(Catalogue.getInstance().searchTargetDirectory("NDF Richard.pdf"));
    }

    @Test
    void testMultipleRegexMatch() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getString("REGEX"))
                .thenReturn("^NDF.*\\.(pdf)$")
                .thenReturn("^FACTURE.*\\.(pdf)$");
        when(rs.getString("DOSSIERDEST"))
                .thenReturn("DossierNDF")
                .thenReturn("DossierFacture");

        Catalogue.getInstance().remplir(rs);

        assertEquals("DossierNDF", Catalogue.getInstance().searchTargetDirectory("NDF123.pdf"));
        assertEquals("DossierFacture", Catalogue.getInstance().searchTargetDirectory("FACTURE456.pdf"));
    }


    @Test
    void testRegexInvalide() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("REGEX")).thenReturn("[invalid(");
        when(rs.getString("DOSSIERDEST")).thenReturn("Dossier");

        Catalogue.getInstance().remplir(rs);
        assertEquals(0, Catalogue.getInstance().getTailleCatalogue());
        assertNull(Catalogue.getInstance().searchTargetDirectory("test.pdf"));
    }
    @Test
    void testTailleCatalogue() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getString("REGEX")).thenReturn("^NDF.*").thenReturn("^FACTURE.*");
        when(rs.getString("DOSSIERDEST")).thenReturn("Dossier1").thenReturn("Dossier2");

        Catalogue.getInstance().remplir(rs);
        assertEquals(2, Catalogue.getInstance().getTailleCatalogue());
    }
}