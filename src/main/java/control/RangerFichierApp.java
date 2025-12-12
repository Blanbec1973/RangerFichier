package control;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.OptionPaneUI;
import view.UserInterface;
import model.FileProcessingService;
import org.heyner.common.Parameter;

public class RangerFichierApp {
    private static final Logger logger = LogManager.getLogger(RangerFichierApp.class);

    public static void main(String[] args) {
        Parameter parametres = new Parameter("config.properties");
        logger.info("RangerFichier v{}", parametres.getVersion());

        UserInterface ui = new OptionPaneUI();
        FileProcessingService service = new FileProcessingService(parametres);

        run(args, ui, service, parametres);
    }

    /**
     * Méthode testable qui contient la logique principale.
     */
    static void run(String[] args, UserInterface ui, FileProcessingService service, Parameter parametres) {
        if (args == null || args.length == 0) {
            String msg = parametres.getProperty("MsgErrNoFile");
            logger.error(msg);
            ui.showMessage(msg);
            return;
        }

        try {
            service.loadCatalogue();
            String rapport = service.processFiles(args);
            ui.showMessage(rapport);
        } catch (Exception e) {
            logger.fatal("Erreur critique", e);
            ui.showMessage("Erreur critique : " + e.getMessage());
        }
    }
}

