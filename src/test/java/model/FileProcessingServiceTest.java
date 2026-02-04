package model;

import org.apache.commons.io.FileUtils;
import org.heyner.common.Parameter;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileProcessingServiceTest {

    @BeforeEach
    void setUp() throws IOException {
        Parameter mockParam = mock(Parameter.class);
        RegleRepository mockRepository = mock(RegleRepository.class);
        Catalogue mockCatalogue = mock(Catalogue.class);
        ReportService mockReportService = mock(ReportService.class);

        when(mockParam.getProperty("sql")).thenReturn("SELECT * FROM REGLES");
        FileProcessingService service = new FileProcessingService(mockRepository, mockCatalogue, mockReportService);

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
    void testLoadCatalog() {
        RegleRepository mockRepo = mock(RegleRepository.class);
        Catalogue mockCatalogue = mock(Catalogue.class);
        ReportService mockReport = mock(ReportService.class);

        FileProcessingService service = new FileProcessingService(mockRepo, mockCatalogue, mockReport);
        service.loadCatalog();

        verify(mockCatalogue).chargerDepuisRepository(mockRepo);
    }

    @Test
    void testProcessFiles_NoMatch() {
        Catalogue mockCatalogue = mock(Catalogue.class);
        RegleRepository mockRepo = mock(RegleRepository.class);
        ReportService mockReport = mock(ReportService.class);

        // append() doit renvoyer le même object sinon chainage impossible
        when(mockReport.append(anyString())).thenReturn(mockReport);

        when(mockCatalogue.searchTargetDirectory(anyString())).thenReturn(null);

        FileProcessingService fps = new FileProcessingService(mockRepo, mockCatalogue, mockReport);

        fps.processFiles(new String[]{"toto.pdf"});

        verify(mockReport).append("Pas de correspondance pour : ");
        verify(mockReport).append("toto.pdf");
        verify(mockReport).addTotalReport(0);
    }


    @Test
    void testProcessFiles_MoveFails() {
        Catalogue mockCatalogue = mock(Catalogue.class);
        RegleRepository mockRepo = mock(RegleRepository.class);
        ReportService mockReport = mock(ReportService.class);

        FileProcessingService fps = spy(new FileProcessingService(mockRepo, mockCatalogue, mockReport));

        OperationFichier mockOp = mock(OperationFichier.class);
        doReturn(mockOp).when(fps).createOperationFichier();

        when(mockReport.append(anyString())).thenReturn(mockReport);

        // IMPORTANT
        doNothing().when(mockOp).setPathSource(any());
        when(mockOp.rechercheCible(mockCatalogue)).thenReturn("/tmp/");
        when(mockOp.deplacement()).thenReturn(false);

        fps.processFiles(new String[]{"fichier.pdf"});

        verify(mockReport).append("Échec du déplacement : ");
        verify(mockReport).addTotalReport(0);
    }
    @Test
    void testProcessFiles_MoveSuccess() {
        Catalogue mockCatalogue = mock(Catalogue.class);
        RegleRepository mockRepo = mock(RegleRepository.class);
        ReportService mockReport = mock(ReportService.class);

        // append chaining
        when(mockReport.append(anyString())).thenReturn(mockReport);

        FileProcessingService fps = spy(new FileProcessingService(mockRepo, mockCatalogue, mockReport));
        OperationFichier mockOp = mock(OperationFichier.class);

        // On remplace l’instance créée par FileProcessingService
        doReturn(mockOp).when(fps).createOperationFichier();

        // IMPORTANT :
        doNothing().when(mockOp).setPathSource(any());
        when(mockCatalogue.searchTargetDirectory(anyString())).thenReturn("/tmp/");
        when(mockOp.rechercheCible(mockCatalogue)).thenReturn("/tmp/");
        when(mockOp.deplacement()).thenReturn(true);

        fps.processFiles(new String[]{"doc.txt"});

        verify(mockReport).append("Déplacé : ");
        verify(mockReport).addTotalReport(1);
    }

    @Test
    void testGetReport() {
        ReportService mockReport = mock(ReportService.class);
        Catalogue mockCatalogue = mock(Catalogue.class);
        RegleRepository mockRepo = mock(RegleRepository.class);

        when(mockReport.getReport()).thenReturn("Mon Rapport");

        FileProcessingService fps = new FileProcessingService(mockRepo, mockCatalogue, mockReport);

        assertEquals("Mon Rapport", fps.getReport());
    }

    @Test
    void testLoadCatalogueSuccess() {
        Connexion mockConnexion = mock(Connexion.class);
        ResultSet mockResultSet = mock(ResultSet.class);

//        when(mockConnexion.getResultSet()).thenReturn(mockResultSet);
//
//        assertDoesNotThrow(() -> service.loadCatalogue(mockConnexion));
//        verify(mockConnexion).connect();
//        verify(mockConnexion).query(anyString());
//        verify(mockConnexion).getResultSet();
    }

//    @Test
//    void testLoadCatalogueThrowsException() {
//        Connexion mockConnexion = mock(Connexion.class);
//        doThrow(new exceptions.DatabaseAccessException("Erreur critique", new RuntimeException()))
//                .when(mockConnexion).query(anyString());
//
//        assertThrows(exceptions.DatabaseAccessException.class,
//                () -> service.loadCatalogue(mockConnexion));
//    }
//    @Test
//    void testMoveSimpleFile() {
//        Path tempDir = Path.of("target/temp");
//        File f = new File("target/temp/in/toto.txt");
//
//        String[] files = {f.toString()};
//        try (MockedStatic<Catalogue> catalogueMock = Mockito.mockStatic(Catalogue.class)) {
//            Catalogue mockCatalogue = mock(Catalogue.class);
//            when(mockCatalogue.searchTargetDirectory(any()))
//                    .thenReturn(tempDir + "/out/");
//            catalogueMock.when(Catalogue::getInstance).thenReturn(mockCatalogue);
//
//            service.processFiles(files);
//            assertTrue(new File("target/temp/out/toto.txt").exists());
//            assertFalse(new File("target/temp/in/toto.txt").exists());
//        }
//    }
//    @Test
//    void testProcessFilesMultiple() throws Exception {
//        Path tempDir = Path.of("target/temp");
//        File file1 = new File("target/temp/in/toto1.txt");
//        file1.createNewFile();
//        File file2 = new File("target/temp/in/toto2.txt");
//        file2.createNewFile();
//
//        try (MockedStatic<Catalogue> catalogueMock = Mockito.mockStatic(Catalogue.class)) {
//            Catalogue mockCatalogue = mock(Catalogue.class);
//            when(mockCatalogue.searchTargetDirectory(any()))
//                    .thenReturn(tempDir + "/out/");
//            catalogueMock.when(Catalogue::getInstance).thenReturn(mockCatalogue);
//
//            Files.createDirectories(tempDir.resolve("out"));
//            String[] files = {file1.toString(), file2.toString()};
//            String result = service.processFiles(files);
//
//            assertTrue(result.contains("toto1.txt"));
//            assertTrue(result.contains("toto2.txt"));
//            assertTrue(Files.exists(tempDir.resolve("out/toto1.txt")));
//            assertTrue(Files.exists(tempDir.resolve("out/toto2.txt")));
//        }
//    }
}