package control;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.ginsberg.junit.exit.SystemExitPreventedException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ControleTest {
    private OptionPane jOptionPane= Mockito.mock(OptionPane.class);

    @BeforeEach
    void setUp() throws IOException {
        new File("target/temp").mkdir();
        new File("target/temp/in").mkdir();
        new File("target/temp/out").mkdir();
        if (Files.exists(Paths.get("target/temp/in")))
            FileUtils.cleanDirectory(new File("target/temp/in"));
        if (Files.exists(Paths.get("target/temp/out")))
            FileUtils.cleanDirectory(new File("target/temp/out"));
        File f = new File("target/temp/in/toto.txt");
        f.createNewFile();
    }
    @Test
    @ExpectSystemExitWithStatus(-1)
    void testControleVide() throws ArgumentErroneException {
        String [] argumentVide = new String[0];
        // Si argument vide, on lève une exception
        Assertions.assertThrows(SystemExitPreventedException.class, () -> {
            new Controle(argumentVide, jOptionPane);
        });
    }
    @Test
    @ExpectSystemExitWithStatus(0)
    void testControle() {
        doAnswer(invocation -> {
            String msg = invocation.getArgument(0);
            assertEquals("Pas de correspondance trouvée.", msg);
            return null;
        }).when(jOptionPane).showMessageDialog(any(String.class));

        new Controle(new String [] {"pastrouve.txt"},jOptionPane);
    }

    @Test
    void testControle2() {
        doAnswer(invocation -> {
            String msg = invocation.getArgument(0);
            String expected = """
                    Déplacement du fichier :
                    target/temp/in/toto.txt
                    Vers le dossier :
                    target/temp/out/""";
            assertEquals(expected, msg);
            return null;
        }).when(jOptionPane).showMessageDialog(any(String.class));

        new Controle(new String [] {"target/temp/in/toto.txt"},jOptionPane);
        assertTrue(new File("target/temp/out/toto.txt").exists());
        assertFalse(new File("target/temp/in/toto.txt").exists());
    }

}