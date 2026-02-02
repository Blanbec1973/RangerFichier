package model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OperationFichierTest {
    /** Temporary area. */
    private static OperationFichier operationFichier;
    private static final File TEMP_DIR = new File("target/temp");
    private static final File TEMP_DIR2 = new File("target/temp2");
    /** Name of the temporary XML file. */
    private static final String FILE_NAME = "dumpfile.txt";
    @Mock
    private Catalogue mockCatalogue;

    @BeforeAll
    static void beforeAll() throws IOException {
        operationFichier = new OperationFichier();
        TEMP_DIR.mkdirs();
        TEMP_DIR2.mkdirs();
        File file = new File(TEMP_DIR,FILE_NAME);
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(FILE_NAME.getBytes());
        outputStream.close();
    }

    @AfterAll
    static void afterAll() {
        File file = new File(TEMP_DIR2+"/"+FILE_NAME);
        file.delete();
        File directory1 = new File(String.valueOf(TEMP_DIR2));
        directory1.delete();
        File directory2 = new File(String.valueOf(TEMP_DIR));
        directory2.delete();
    }

    @Test
    @Order(1)
    void setPathSource() {
        operationFichier.setPathSource(Path.of(TEMP_DIR + "/" + FILE_NAME));
        assertEquals(Path.of("target/temp/dumpfile.txt"),operationFichier.getPathSource());
    }

    @Test
    @Order(2)
    void rechercheCible() {
        operationFichier.setPathSource(Path.of(TEMP_DIR + "/" + FILE_NAME));
        Catalogue catalogue = mock(Catalogue.class);
        when(catalogue.searchTargetDirectory(any())).thenReturn("target/temp2/");
        assertEquals("target/temp2/", operationFichier.rechercheCible(catalogue));

    }


//    public String rechercheCible(Catalogue catalogue) {
//        String dossierCible = catalogue.searchTargetDirectory(pathSource.getFileName().toString());
//        if (dossierCible != null) pathCible = Path.of(dossierCible+pathSource.getFileName().toString());
//        return dossierCible;
//    }

    @Test
    @Order(3)
    void deplacement() {
        operationFichier.deplacement();
        File file = new File(TEMP_DIR2+"/"+FILE_NAME);
        assertTrue(file.exists());
        File file2 = new File(TEMP_DIR+"/"+FILE_NAME);
        assertFalse(file2.exists());
    }

    @Test
    void testDeplacementFichierInexistant() {
        operationFichier.setPathSource(Path.of("target/temp/inexistant.txt"));
        assertDoesNotThrow(operationFichier::deplacement); // Ne doit pas planter
    }

    @Test
    void testPathCibleCorrect() {
        operationFichier.setPathSource(Path.of(TEMP_DIR + "/" + FILE_NAME));
        when(mockCatalogue.searchTargetDirectory(any())).thenReturn("target/temp2/");
        operationFichier.rechercheCible(mockCatalogue);
       assertEquals(Path.of("target/temp2", FILE_NAME), operationFichier.getPathCible());
    }

    @Test
    void testRechercheCibleNull() {
        operationFichier.setPathSource(Path.of(TEMP_DIR + "/" + FILE_NAME));
        when(mockCatalogue.searchTargetDirectory(any())).thenReturn(null);
        String result = operationFichier.rechercheCible(mockCatalogue);
        assertNull(result);
        assertDoesNotThrow(operationFichier::deplacement);
    }
    @Test
    void testDeplacementMultiple() throws IOException {
        File file1 = new File(TEMP_DIR, "file1.txt");
        File file2 = new File(TEMP_DIR, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();

        when(mockCatalogue.searchTargetDirectory(any())).thenReturn("target/temp2/");
        operationFichier.setPathSource(file1.toPath());
        operationFichier.rechercheCible(mockCatalogue);
        operationFichier.deplacement();

        operationFichier.setPathSource(file2.toPath());
        operationFichier.rechercheCible(mockCatalogue);
        operationFichier.deplacement();

        assertTrue(new File(TEMP_DIR2, "file1.txt").exists());
        assertTrue(new File(TEMP_DIR2, "file2.txt").exists());
    }

}