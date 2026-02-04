package model;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueTest {

    @Test
    void testRechercheDossierCible() {
        // Prépare des règles de test
        Regle regle1 = new Regle("^facture.*\\.pdf$", "/factures/");
        Regle regle2 = new Regle("^note.*\\.pdf$", "/notes/");

        // Mock du repository
        RegleRepository mockRepo = mock(RegleRepository.class);
        when(mockRepo.findAllRegles()).thenReturn(Arrays.asList(regle1, regle2));

        Catalogue catalogue = new Catalogue();
        catalogue.chargerDepuisRepository(mockRepo);

        assertEquals("/factures/", catalogue.searchTargetDirectory("facture2024.pdf"));
        assertEquals("/notes/", catalogue.searchTargetDirectory("note_de_frais.pdf"));
        assertNull(catalogue.searchTargetDirectory("autre.docx"));
    }

    @Test
    void testCatalogEmpty() {
        RegleRepository mockRepo = mock(RegleRepository.class);
        Catalogue catalogue = new Catalogue();
        when(mockRepo.findAllRegles()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> catalogue.chargerDepuisRepository(mockRepo));
    }


}