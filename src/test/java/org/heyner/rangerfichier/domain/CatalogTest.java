package org.heyner.rangerfichier.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    @Test
    void testRechercheDossierCible() {
        // Prépare des règles de test
        Rule rule1 = new Rule("^facture.*\\.pdf$", "/factures/");
        Rule rule2 = new Rule("^note.*\\.pdf$", "/notes/");

        List<Rule> rules = Arrays.asList(rule1, rule2);

        Catalog catalog = new Catalog(rules);

        assertEquals(Optional.of("/factures/"),
                catalog.searchTargetDirectory("facture2024.pdf"));
        assertEquals(Optional.of("/notes/"),
                catalog.searchTargetDirectory("note_de_frais.pdf"));
        assertEquals(Optional.empty(),
                catalog.searchTargetDirectory("autre.docx"));
        assertEquals(2, catalog.getSize());
    }

    @Test
    void testCatalogEmpty() {
        Catalog catalogue = new Catalog(Collections.emptyList());
        assertEquals(0, catalogue.getSize());
        assertEquals(Optional.empty(), catalogue.searchTargetDirectory("anyfile.txt"));
    }


}