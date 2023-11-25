package control;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.ginsberg.junit.exit.SystemExitPreventedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentErroneExceptionTest {
    @Test
    @ExpectSystemExitWithStatus(-1)
    void testExpectedException() {
        String [] args = {};
        SystemExitPreventedException thrown = assertThrows(SystemExitPreventedException.class, () -> {
            Controle.main(args);
                });
    }



}