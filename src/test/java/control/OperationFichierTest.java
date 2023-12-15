package control;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
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
    private Catalogue catalogue ;

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
        MockedStatic<Catalogue> catalogueMock = mockStatic(Catalogue.class);
        catalogueMock.when(Catalogue::getInstance).thenReturn(catalogue);
        when(Catalogue.searchTargetDirectory(any())).thenReturn("target/temp2/");
        assertEquals("target/temp2/", operationFichier.rechercheCible(null));
    }

    @Test
    @Order(3)
    void deplacement() {
        operationFichier.deplacement();
        File file = new File(TEMP_DIR2+"/"+FILE_NAME);
        assertTrue(file.exists());
        File file2 = new File(TEMP_DIR+"/"+FILE_NAME);
        assertFalse(file2.exists());
    }
}