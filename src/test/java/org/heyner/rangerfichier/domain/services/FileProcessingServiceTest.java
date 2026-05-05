package org.heyner.rangerfichier.domain.services;

import org.apache.commons.io.FileUtils;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.shared.util.PathNormalizer;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileProcessingServiceTest {

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
    void testProcessFiles_NoMatch() {
        ReportBuilder mockReport = mock(ReportBuilder.class);
        Catalog mockCatalog = mock(Catalog.class);
        OperationFichier mockOperationFichier = mock(OperationFichier.class);
        PathNormalizer mockPathNormalizer = mock(PathNormalizer.class);
        when(mockReport.append(anyString())).thenReturn(mockReport);

        FileProcessingService service = new FileProcessingService(mockCatalog,
                mockReport,  mockOperationFichier,  mockPathNormalizer);
        service.processFiles(new String[]{"toto.pdf"});

        verify(mockReport).append("Pas de correspondance pour : ");
        verify(mockReport).append("toto.pdf");
        verify(mockReport).addTotalReport(0);
    }

    @Test
    void testProcessFiles_MoveSuccess() {
        ReportBuilder mockReport = mock(ReportBuilder.class);
        Catalog mockCatalog = mock(Catalog.class);
        OperationFichier mockOperationFichier = mock(OperationFichier.class);
        PathNormalizer mockPathNormalizer = mock(PathNormalizer.class);

        when(mockReport.append(anyString())).thenReturn(mockReport);

        FileProcessingService fps = spy(new FileProcessingService(mockCatalog,
                mockReport, mockOperationFichier,  mockPathNormalizer));

        when(mockCatalog.searchTargetDirectory(anyString())).thenReturn(Optional.of("/tmp/"));

        fps.processFiles(new String[]{"doc.txt"});

        verify(mockReport).append("Déplacé : ");
        verify(mockReport).addTotalReport(1);
    }

    @Test
    void testGetReport() {
        ReportBuilder mockReport = mock(ReportBuilder.class);
        Catalog mockCatalog = mock(Catalog.class);
        OperationFichier mockOperationFichier = mock(OperationFichier.class);
        PathNormalizer mockPathNormalizer = mock(PathNormalizer.class);

        when(mockReport.getReport()).thenReturn("Mon Rapport");

        FileProcessingService fps = new FileProcessingService(mockCatalog,
                mockReport,  mockOperationFichier,   mockPathNormalizer);

        assertEquals("Mon Rapport", fps.getReport());
    }

}