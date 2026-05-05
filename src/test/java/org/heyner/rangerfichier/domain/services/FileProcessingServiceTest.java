package org.heyner.rangerfichier.domain.services;

import org.apache.commons.io.FileUtils;
import org.heyner.common.Parameter;
import org.heyner.rangerfichier.domain.Catalog;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileProcessingServiceTest {

    @BeforeEach
    void setUp() throws IOException {
        Parameter mockParam = mock(Parameter.class);
        Catalog mockCatalog = mock(Catalog.class);
        ReportBuilder mockReportBuilder = mock(ReportBuilder.class);
        OperationFichier mockOperationFichier = mock(OperationFichier.class);

        when(mockParam.getProperty("sql")).thenReturn("SELECT * FROM REGLES");
        FileProcessingService service = new FileProcessingService(mockCatalog,
                mockReportBuilder, mockOperationFichier);

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
//        ReportBuilder mockReport = mock(ReportBuilder.class);
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
        ReportBuilder mockReport = mock(ReportBuilder.class);
        Catalog mockCatalog = mock(Catalog.class);
        OperationFichier mockOperationFichier = mock(OperationFichier.class);
        when(mockReport.append(anyString())).thenReturn(mockReport);

        FileProcessingService service = new FileProcessingService(mockCatalog,
                mockReport,  mockOperationFichier);
        service.processFiles(new String[]{"toto.pdf"});

        verify(mockReport).append("Pas de correspondance pour : ");
        verify(mockReport).append("toto.pdf");
        verify(mockReport).addTotalReport(0);
    }


//    @Test
//    void testProcessFiles_MoveFails() {
//        ReportBuilder mockReport = mock(ReportBuilder.class);
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
        ReportBuilder mockReport = mock(ReportBuilder.class);
        Catalog mockCatalog = mock(Catalog.class);
        OperationFichier mockOperationFichier = mock(OperationFichier.class);

        // append chaining
        when(mockReport.append(anyString())).thenReturn(mockReport);

        FileProcessingService fps = spy(new FileProcessingService(mockCatalog,
                mockReport, mockOperationFichier));


        // IMPORTANT :
        //doNothing().when(mockOp).setPathSource(any());
        when(mockCatalog.searchTargetDirectory(anyString())).thenReturn(Optional.of("/tmp/"));
        //when(mockOp.rechercheCible(mockCatalog)).thenReturn("/tmp/");
        doNothing().when(mockOperationFichier).move(any(Path.class), any(Path.class));

        fps.processFiles(new String[]{"doc.txt"});

        verify(mockReport).append("Déplacé : ");
        verify(mockReport).addTotalReport(1);
    }

    @Test
    void testGetReport() {
        ReportBuilder mockReport = mock(ReportBuilder.class);
        Catalog mockCatalog = mock(Catalog.class);
        OperationFichier mockOperationFichier = mock(OperationFichier.class);

        when(mockReport.getReport()).thenReturn("Mon Rapport");

        FileProcessingService fps = new FileProcessingService(mockCatalog,
                mockReport,  mockOperationFichier);

        assertEquals("Mon Rapport", fps.getReport());
    }

}