package control;

import view.UserInterface;
import model.FileProcessingService;
import org.heyner.common.Parameter;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RangerFichierAppTest {

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
    void testRunWithArgsSuccess() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);

        when(mockService.getReport()).thenReturn("Report OK");

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(new String[]{"file.txt"});

        verify(mockService).loadCatalog();
        verify(mockService).processFiles(any());
        verify(mockUI).showMessage("Report OK");
    }

    @Test
    void testRunWithException() {
        UserInterface mockUI = mock(UserInterface.class);
        FileProcessingService mockService = mock(FileProcessingService.class);
        Parameter mockParam = mock(Parameter.class);

        doThrow(new RuntimeException("Erreur simulée")).when(mockService).loadCatalog();

        RangerFichierApp app = new RangerFichierApp(mockUI, mockService, mockParam);
        app.run(new String[]{"file.txt"});

        verify(mockUI).showMessage("Erreur critique : Erreur simulée");
    }
}
