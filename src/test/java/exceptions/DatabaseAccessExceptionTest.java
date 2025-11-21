package exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseAccessExceptionTest {

    @Test
    void testExceptionMessageAndCause() {
        Throwable cause = new RuntimeException("Cause initiale");
        DatabaseAccessException exception = new DatabaseAccessException("Erreur critique", cause);

        assertEquals("Erreur critique", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}