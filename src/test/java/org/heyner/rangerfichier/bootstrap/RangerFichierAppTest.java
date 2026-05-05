package org.heyner.rangerfichier.bootstrap;

import ui.UserInterface;
import org.heyner.rangerfichier.domain.services.FileProcessingService;
import org.heyner.common.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.*;

class RangerFichierAppTest {

    private static final File TEMP_SOURCE = new File("target/test-source");
    private static final File TEMP_TARGET = new File("target/test-target");
    private static final String TEST_FILE = "testfile.txt";

    @BeforeEach
    void setUp() throws IOException {
        //noinspection ResultOfMethodCallIgnored
        TEMP_SOURCE.mkdirs();
        //noinspection ResultOfMethodCallIgnored
        TEMP_TARGET.mkdirs();
        // Créer un fichier de test
        File testFile = new File(TEMP_SOURCE, TEST_FILE);
        OutputStream outputStream = new FileOutputStream(testFile);
        outputStream.write("Test content".getBytes());
        outputStream.close();
    }

    @AfterEach
    void tearDown() {
        // Nettoyer les fichiers de test
        File testFile = new File(TEMP_SOURCE, TEST_FILE);
        if (testFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            testFile.delete();
        }
        File targetFile = new File(TEMP_TARGET, TEST_FILE);
        if (targetFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            targetFile.delete();
        }
        //noinspection ResultOfMethodCallIgnored
        TEMP_SOURCE.delete();
        //noinspection ResultOfMethodCallIgnored
        TEMP_TARGET.delete();
    }

    @Test
    void testRunWithNoArgs() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);
        when(mockParam.getProperty("MsgErrNoFile")).thenReturn("Arguments manquants");

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(new String[]{});

        verify(mockUI).showMessage("Arguments manquants");
        verifyNoInteractions(mockService);
    }

    @Test
    void testRunWithNullArgs() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);
        when(mockParam.getProperty("MsgErrNoFile")).thenReturn("Arguments manquants");

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(null);

        verify(mockUI).showMessage("Arguments manquants");
        verifyNoInteractions(mockService);
    }

    @Test
    void testRunWithArgsSuccess() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);

        when(mockService.getReport()).thenReturn("Report OK");

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(new String[]{"file.txt"});

        verify(mockService).processFiles(any());
        verify(mockUI).showMessage("Report OK");
    }

//    @Test
//    void testRunWithException() {
//        UserInterface mockUI = mock(UserInterface.class);
//        FileProcessingService mockService = mock(FileProcessingService.class);
//        Parameter mockParam = mock(Parameter.class);
//
//        doThrow(new RuntimeException("Erreur simulée")).when(mockService).loadCatalog();
//
//        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
//        app.run(new String[]{"file.txt"});
//
//        verify(mockUI).showMessage("Erreur critique : Erreur simulée");
//    }

    @Test
    void testRunWithNoRulesFound() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);

        String reportWithNoMatch = "Pas de correspondance pour : file.txt\nTotal : 0 déplacements effectués\n";
        when(mockService.getReport()).thenReturn(reportWithNoMatch);

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(new String[]{"file.txt"});

        verify(mockService).processFiles(any());
        verify(mockUI).showMessage(reportWithNoMatch);
    }

    @Test
    void testRunWithSuccessfulFileMove() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);

        String testFilePath = TEMP_SOURCE + "/" + TEST_FILE;
        String successReport = "Déplacé : " + testFilePath + " -> " + TEMP_TARGET + "/" + TEST_FILE + "\nTotal : 1 déplacements effectués\n";
        when(mockService.getReport()).thenReturn(successReport);

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(new String[]{testFilePath});

        verify(mockService).processFiles(any());
        verify(mockUI).showMessage(successReport);
    }

    @Test
    void testRunWithFailedFileMove() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);

        String testFilePath = TEMP_SOURCE + "/" + TEST_FILE;
        String errorReport = "ERREUR : " + testFilePath + " -> Impossible de déplacer le fichier\nTotal : 0 déplacements effectués\n";
        when(mockService.getReport()).thenReturn(errorReport);

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(new String[]{testFilePath});

        verify(mockService).processFiles(any());
        verify(mockUI).showMessage(errorReport);
    }
}
