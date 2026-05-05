package org.heyner.rangerfichier.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.domain.ports.RuleRepositoryPort;
import org.heyner.rangerfichier.domain.services.FileProcessingService;
import org.heyner.rangerfichier.domain.services.OperationFichier;
import org.heyner.rangerfichier.domain.services.ReportBuilder;
import org.heyner.rangerfichier.infrastructure.config.ConfigLoader;
import org.heyner.rangerfichier.infrastructure.sgbd.Connexion;
import org.heyner.rangerfichier.infrastructure.sgbd.RuleRepositoryAdapter;
import ui.OptionPaneUI;
import ui.UserInterface;

public class BootStrap {
    private static final Logger logger = LogManager.getLogger(BootStrap.class);

    public RangerFichierApp createApp() {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.load();
        Parameter parameters = configLoader.getParameter();
        logger.info("RangerFichier v{}", parameters.getVersion());
        Catalog catalog = null;

        UserInterface ui = new OptionPaneUI();
        try (Connexion connexion = new Connexion(parameters.getProperty("url"))) {
            RuleRepositoryPort ruleRepositoryPort = new RuleRepositoryAdapter(parameters,
                    connexion);
            catalog = new Catalog(ruleRepositoryPort.findAllRules());
        }

        ReportBuilder reportBuilder = new ReportBuilder();
        OperationFichier operationFichier = new OperationFichier();
        FileProcessingService service = new FileProcessingService(catalog,
                reportBuilder,
                operationFichier);

        return new RangerFichierApp(ui, service, parameters);
    }
}
