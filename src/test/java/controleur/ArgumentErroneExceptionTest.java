package controleur;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentErroneExceptionTest {


    @Test
    void testExpectedException() {
        String [] args = {};
        ArgumentErroneException thrown = Assertions.assertThrows(ArgumentErroneException.class, () -> {
            Controle.main(args);
                });

        Assertions.assertEquals("Saisie erronée : chaine vide.", thrown.getMessage());
    }



}