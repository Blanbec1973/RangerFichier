package controleur;

import controleur.ArgumentErroneException;
import controleur.Controle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ControleTest {

    @Test
    public void testMain() throws ArgumentErroneException {
        // Si argument vide, on lève une exception
        ArgumentErroneException thrown = Assertions.assertThrows(ArgumentErroneException.class, () -> {
            Controle.main(null);
        });
    }


}