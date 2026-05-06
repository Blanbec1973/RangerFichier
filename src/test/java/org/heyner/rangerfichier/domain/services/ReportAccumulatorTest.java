package org.heyner.rangerfichier.domain.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportAccumulatorTest {

    @Test
    void append_shouldConcatenateText_andReturnSameInstance() {
        ReportAccumulator rb = new ReportAccumulator();

        ReportAccumulator returned = rb.append("Hello ").append("World");

        assertSame(rb, returned, "append() doit retourner la même instance (fluent)");
        assertEquals("Hello World", rb.getReport());
    }

    @Test
    void addSummary_whenZero_shouldAppendNoFileMovedMessage() {
        ReportAccumulator rb = new ReportAccumulator();

        rb.addSummary(0);

        assertEquals("Aucun fichier déplacé.", rb.getReport());
    }

    @Test
    void addSummary_whenPositive_shouldAppendCountMessage() {
        ReportAccumulator rb = new ReportAccumulator();

        rb.addSummary(3);

        assertEquals("3 fichier(s) déplacé(s).", rb.getReport());
    }

    @Test
    void clear_shouldResetReport() {
        ReportAccumulator rb = new ReportAccumulator();
        rb.append("something");

        rb.clear();

        assertEquals("", rb.getReport());
    }
}