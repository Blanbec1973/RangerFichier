package control;


import model.Catalogue;
import model.RegleRepository;
import model.ReportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.OptionPaneUI;
import view.UserInterface;
import model.FileProcessingService;
import org.heyner.common.Parameter;

public class RangerFichierApp {
    private static final Logger logger = LogManager.getLogger(RangerFichierApp.class);
    private final UserInterface ui;
    private final FileProcessingService service;
    private final Parameter parametres;

    public RangerFichierApp(UserInterface ui, FileProcessingService service, Parameter parametres) {
        this.ui = ui;
        this.service = service;
        this.parametres = parametres;
    }

    public static void main(String[] args) {
        ParameterFactory parameterFactory = new ParameterFactory();
        Parameter parametres = parameterFactory.getParameter();
        logger.info("RangerFichier v{}", parametres.getVersion());

        UserInterface ui = new OptionPaneUI();
        RegleRepository regleRepository = new RegleRepository(parametres);
        Catalogue catalog = new Catalogue();
        ReportService reportService = new ReportService();
        FileProcessingService service = new FileProcessingService(regleRepository, catalog, reportService);

        // Injection manuelle
        RangerFichierApp app = new RangerFichierApp(ui, service, parametres);
        app.run(args);
    }

    /**
     * Méthode testable qui contient la logique principale.
     */
     void run(String[] args) {
        if (args == null || args.length == 0) {
            String msg = parametres.getProperty("MsgErrNoFile");
            logger.error(msg);
            ui.showMessage(msg);
            return;
        }

        try {
            service.loadCatalog();
            service.processFiles(args);
            String report = service.getReport();
            ui.showMessage(report);
        } catch (Exception e) {
            logger.fatal("Erreur critique", e);
            ui.showMessage("Erreur critique : " + e.getMessage());
        }
    }
}

