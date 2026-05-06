package org.heyner.rangerfichier.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.rangerfichier.shared.exceptions.DatabaseAccessException;
import ui.UserInterface;
import org.heyner.rangerfichier.domain.services.FileProcessingService;
import org.heyner.common.Parameter;

public class RangerFichierApp {
    private static final Logger logger = LogManager.getLogger(RangerFichierApp.class);
    private final UserInterface ui;
    private final FileProcessingService service;
    private final Parameter parameter;

    public RangerFichierApp(UserInterface ui, FileProcessingService service, Parameter parameter) {
        this.ui = ui;
        this.service = service;
        this.parameter = parameter;
    }

    public static void main(String[] args) {

        // Injection manuelle
        RangerFichierApp app = new Bootstrap().createApp();
        app.execute(args);
    }

    /**
     * Méthode testable qui contient la logique principale.
     */
     void execute(String[] args) {
        if (args == null || args.length == 0) {
            String msg = parameter.getProperty("MsgErrNoFile");
            logger.error(msg);
            ui.showMessage(msg);
            return;
        }

        try {
            service.processFiles(args);
            String report = service.getReport();
            ui.showMessage(report);
        } catch (DatabaseAccessException e) {
            logger.fatal("Erreur critique", e);
            ui.showMessage("Erreur critique : " + e.getMessage());
        }
    }
}

