package control;

import view.OptionPaneUI;
import view.UserInterface;
import model.FileProcessingService;
import org.heyner.common.Parameter;

public class RangerFichierApp {

    public static void main(String[] args) {
        Parameter parametres = new Parameter("config.properties");
        UserInterface ui = new OptionPaneUI();
        FileProcessingService service = new FileProcessingService(parametres);

        run(args, ui, service, parametres);
    }

    /**
     * Méthode testable qui contient la logique principale.
     */
    static void run(String[] args, UserInterface ui, FileProcessingService service, Parameter parametres) {
        if (args == null || args.length == 0) {
            ui.showMessage(parametres.getProperty("MsgErrNoFile"));
            return;
        }

        try {
            service.loadCatalogue();
            String rapport = service.processFiles(args);
            ui.showMessage(rapport);
        } catch (Exception e) {
            ui.showMessage("Erreur critique : " + e.getMessage());
        }
    }
}

