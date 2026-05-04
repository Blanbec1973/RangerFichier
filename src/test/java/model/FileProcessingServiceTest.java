package model;

import org.apache.commons.io.FileUtils;
import org.heyner.common.Parameter;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.domain.ports.RuleRepositoryPort;
import org.heyner.rangerfichier.infrastructure.sgbd.Connexion;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileProcessingServiceTest {

    @BeforeEach
    void setUp() throws IOException {
        Parameter mockParam = mock(Parameter.class);
        Catalog mockCatalog = mock(Catalog.class);
        ReportService mockReportService = mock(ReportService.class);

        when(mockParam.getProperty("sql")).thenReturn("SELECT * FROM REGLES");
        FileProcessingService service = new FileProcessingService(mockCatalog, mockReportService);

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

//    @Test
//    void testLoadCatalog() {
//        ReportService mockReport = mock(ReportService.class);
//        Catalog mockCatalog = mock(Catalog.class);
//        RuleRepositoryPort mockRepo = mock(RuleRepositoryPort.class);
//
//        FileProcessingService service = new FileProcessingService(mockRepo, mockCatalog, mockReport);
//        service.loadCatalog();
//
//        verify(mockCatalog).chargerDepuisRepository(mockRepo);
//    }

    @Test
    void testProcessFiles_NoMatch() {
        ReportService mockReport = mock(ReportService.class);
        Catalog mockCatalog = mock(Catalog.class);
        when(mockReport.append(anyString())).thenReturn(mockReport);

        FileProcessingService service = new FileProcessingService(mockCatalog, mockReport);
        service.processFiles(new String[]{"toto.pdf"});

        verify(mockReport).append("Pas de correspondance pour : ");
        verify(mockReport).append("toto.pdf");
        verify(mockReport).addTotalReport(0);
    }


//    @Test
//    void testProcessFiles_MoveFails() {
//        ReportService mockReport = mock(ReportService.class);
//        Catalog mockCatalog = mock(Catalog.class);
//        RuleRepositoryPort mockRepo = mock(RuleRepositoryPort.class);
//
//        FileProcessingService fps = spy(new FileProcessingService(mockRepo, mockCatalog, mockReport));
//
//        OperationFichier mockOp = mock(OperationFichier.class);
//        doReturn(mockOp).when(fps).createOperationFichier();
//
//        when(mockReport.append(anyString())).thenReturn(mockReport);
//
//        // IMPORTANT
//        doNothing().when(mockOp).setPathSource(any());
//        //when(mockOp.rechercheCible(mockCatalog)).thenReturn("/tmp/");
//        when(mockOp.deplacement()).thenReturn(false);
//
//        fps.processFiles(new String[]{"fichier.pdf"});
//
//        verify(mockReport).append("Échec du déplacement : ");
//        verify(mockReport).addTotalReport(0);
//    }
    @Test
    void testProcessFiles_MoveSuccess() {
        ReportService mockReport = mock(ReportService.class);
        Catalog mockCatalog = mock(Catalog.class);

        // append chaining
        when(mockReport.append(anyString())).thenReturn(mockReport);

        FileProcessingService fps = spy(new FileProcessingService(mockCatalog, mockReport));
        OperationFichier mockOp = mock(OperationFichier.class);

        // On remplace l’instance créée par FileProcessingService
        doReturn(mockOp).when(fps).createOperationFichier();

        // IMPORTANT :
        doNothing().when(mockOp).setPathSource(any());
        when(mockCatalog.searchTargetDirectory(anyString())).thenReturn(Optional.of(Path.of("/tmp/")));
        //when(mockOp.rechercheCible(mockCatalog)).thenReturn("/tmp/");
        when(mockOp.deplacement()).thenReturn(true);

        fps.processFiles(new String[]{"doc.txt"});

        verify(mockReport).append("Déplacé : ");
        verify(mockReport).addTotalReport(1);
    }

    @Test
    void testGetReport() {
        ReportService mockReport = mock(ReportService.class);
        Catalog mockCatalog = mock(Catalog.class);

        when(mockReport.getReport()).thenReturn("Mon Rapport");

        FileProcessingService fps = new FileProcessingService(mockCatalog, mockReport);

        assertEquals("Mon Rapport", fps.getReport());
    }

}