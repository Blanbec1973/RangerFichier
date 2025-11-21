package model;

import org.apache.commons.io.FileUtils;
import org.heyner.common.Parameter;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileProcessingServiceTest {

    private Parameter mockParam;
    private FileProcessingService service;

    @BeforeEach
    void setUp() throws IOException {
        mockParam = mock(Parameter.class);
        when(mockParam.getProperty("sql")).thenReturn("SELECT * FROM REGLES");
        service = new FileProcessingService(mockParam);

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
    void testLoadCatalogueSuccess() {
        Connexion mockConnexion = mock(Connexion.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnexion.getResultSet()).thenReturn(mockResultSet);

        assertDoesNotThrow(() -> service.loadCatalogue(mockConnexion));
        verify(mockConnexion).connect();
        verify(mockConnexion).query(anyString());
        verify(mockConnexion).getResultSet();
    }

    @Test
    void testLoadCatalogueThrowsException() {
        Connexion mockConnexion = mock(Connexion.class);
        doThrow(new exceptions.DatabaseAccessException("Erreur critique", new RuntimeException()))
                .when(mockConnexion).query(anyString());

        assertThrows(exceptions.DatabaseAccessException.class,
                () -> service.loadCatalogue(mockConnexion));
    }
    @Test
    void testMoveSimpleFile() {
        Path tempDir = Path.of("target/temp");
        File f = new File("target/temp/in/toto.txt");
        String expected = "Déplacé : target/temp/in/toto.txt -> target/temp/out/\n" +
                    "1 fichier(s) déplacé(s).";

        String[] files = {f.toString()};
        try (MockedStatic<Catalogue> catalogueMock = Mockito.mockStatic(Catalogue.class)) {
            Catalogue mockCatalogue = mock(Catalogue.class);
            when(mockCatalogue.searchTargetDirectory(any()))
                    .thenReturn(tempDir.toString() + "/out/");
            catalogueMock.when(Catalogue::getInstance).thenReturn(mockCatalogue);

            String result = service.processFiles(files);
            assertTrue(new File("target/temp/out/toto.txt").exists());
            assertFalse(new File("target/temp/in/toto.txt").exists());
        }
    }
    @Test
    void testProcessFilesMultiple() throws Exception {
        Path tempDir = Path.of("target/temp");
        File file1 = new File("target/temp/in/toto1.txt");
        file1.createNewFile();
        File file2 = new File("target/temp/in/toto2.txt");
        file2.createNewFile();

        try (MockedStatic<Catalogue> catalogueMock = Mockito.mockStatic(Catalogue.class)) {
            Catalogue mockCatalogue = mock(Catalogue.class);
            when(mockCatalogue.searchTargetDirectory(any()))
                    .thenReturn(tempDir.toString() + "/out/");
            catalogueMock.when(Catalogue::getInstance).thenReturn(mockCatalogue);

            Files.createDirectories(tempDir.resolve("out"));
            String[] files = {file1.toString(), file2.toString()};
            String result = service.processFiles(files);

            assertTrue(result.contains("toto1.txt"));
            assertTrue(result.contains("toto2.txt"));
            assertTrue(Files.exists(tempDir.resolve("out/toto1.txt")));
            assertTrue(Files.exists(tempDir.resolve("out/toto2.txt")));
        }
    }
}