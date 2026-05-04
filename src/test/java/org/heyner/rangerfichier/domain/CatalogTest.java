package org.heyner.rangerfichier.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    @Test
    void testRechercheDossierCible() {
        // Prépare des règles de test
        Rule Rule1 = new Rule("^facture.*\\.pdf$", "/factures/");
        Rule Rule2 = new Rule("^note.*\\.pdf$", "/notes/");

        List<Rule> rules = Arrays.asList(Rule1, Rule2);

        Catalog catalog = new Catalog(rules);

        assertEquals(Optional.of("/factures/"),
                catalog.searchTargetDirectory("facture2024.pdf"));
        assertEquals(Optional.of("/notes/"),
                catalog.searchTargetDirectory("note_de_frais.pdf"));
        assertEquals(Optional.empty(),
                catalog.searchTargetDirectory("autre.docx"));
    }

//    @Test
//    void testCatalogEmpty() {
//        Catalog catalogue = new Catalog();
//        when(mockRepo.findAllRules()).thenReturn(Collections.emptyList());
//        assertDoesNotThrow(() -> catalogue.chargerDepuisRepository(mockRepo));
//    }


}