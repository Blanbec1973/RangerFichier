package control;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class ControleTest {
    private final OptionPane mockOptionPane = Mockito.mock(OptionPane.class);

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
    void testControleVide() {
        String[] argumentVide = new String[0];
        doAnswer(invocation -> {
            String msg = invocation.getArgument(0);
            assertTrue(msg.contains("Saisie erronée : chaine vide."));
            return null;
        }).when(mockOptionPane).showMessageDialog(any(String.class));

        assertThrows(IllegalArgumentException.class, () -> new Controle(argumentVide, mockOptionPane));
    }
    @Test
    void testControleSansCorrespondance() {
        doAnswer(invocation -> {
            String msg = invocation.getArgument(0);
            assertTrue(msg.contains("Pas de correspondance")); // Vérifie le contenu
            assertTrue(msg.contains("pastrouve.txt")); // Vérifie le nom du fichier
            return null;
        }).when(mockOptionPane).showMessageDialog(any(String.class));

        new Controle(new String[] {"pastrouve.txt"}, mockOptionPane);
    }

    @Test
    void testControle2() {
        doAnswer(invocation -> {
            String msg = invocation.getArgument(0);
            String expected = "Déplacé : target/temp/in/toto.txt -> target/temp/out/\n" +
                    "1 fichier(s) déplacé(s).";
            assertEquals(expected, msg);
            return null;
        }).when(mockOptionPane).showMessageDialog(any(String.class));

        new Controle(new String [] {"target/temp/in/toto.txt"}, mockOptionPane);
        assertTrue(new File("target/temp/out/toto.txt").exists());
        assertFalse(new File("target/temp/in/toto.txt").exists());
    }
    @Test
    void testControleMultiSelection() throws IOException {
        new File("target/temp/in/toto1.txt").createNewFile();
        new File("target/temp/in/toto2.txt").createNewFile();

        doAnswer(invocation -> {
            String msg = invocation.getArgument(0);
            assertTrue(msg.contains("toto1.txt"));
            assertTrue(msg.contains("toto2.txt"));
            return null;
        }).when(mockOptionPane).showMessageDialog(any(String.class));

        new Controle(new String[] {
                "target/temp/in/toto1.txt",
                "target/temp/in/toto2.txt"
        }, mockOptionPane);

        assertTrue(new File("target/temp/out/toto1.txt").exists());
        assertTrue(new File("target/temp/out/toto2.txt").exists());
    }
}