package org.heyner.rangerfichier.domain.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportBuilderTest {

    @Test
    void append_shouldConcatenateText_andReturnSameInstance() {
        ReportBuilder rb = new ReportBuilder();

        ReportBuilder returned = rb.append("Hello ").append("World");

        assertSame(rb, returned, "append() doit retourner la même instance (fluent)");
        assertEquals("Hello World", rb.getReport());
    }

    @Test
    void addTotalReport_whenZero_shouldAppendNoFileMovedMessage() {
        ReportBuilder rb = new ReportBuilder();

        rb.addTotalReport(0);

        assertEquals("Aucun fichier déplacé.", rb.getReport());
    }

    @Test
    void addTotalReport_whenPositive_shouldAppendCountMessage() {
        ReportBuilder rb = new ReportBuilder();

        rb.addTotalReport(3);

        assertEquals("3 fichier(s) déplacé(s).", rb.getReport());
    }

    @Test
    void clear_shouldResetReport() {
        ReportBuilder rb = new ReportBuilder();
        rb.append("something");

        rb.clear();

        assertEquals("", rb.getReport());
    }
}