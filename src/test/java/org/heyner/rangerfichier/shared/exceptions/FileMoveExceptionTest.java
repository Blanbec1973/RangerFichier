package org.heyner.rangerfichier.shared.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileMoveExceptionTest {

    @Test
    void testExceptionMessageAndCause() {
        Throwable cause = new RuntimeException("Cause initiale");
        FileMoveException exception = new FileMoveException("Erreur lors du déplacement du fichier", cause);

        assertEquals("Erreur lors du déplacement du fichier", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}