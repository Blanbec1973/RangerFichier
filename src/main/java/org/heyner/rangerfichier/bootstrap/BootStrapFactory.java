package org.heyner.rangerfichier.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.domain.ports.RuleRepositoryPort;
import org.heyner.rangerfichier.domain.services.FileProcessingService;
import org.heyner.rangerfichier.domain.services.OperationFichier;
import org.heyner.rangerfichier.domain.services.ReportService;
import org.heyner.rangerfichier.infrastructure.config.ParameterFactory;
import org.heyner.rangerfichier.infrastructure.sgbd.Connexion;
import org.heyner.rangerfichier.infrastructure.sgbd.RuleRepositoryAdapter;
import view.OptionPaneUI;
import view.UserInterface;

public class BootStrapFactory {
    private static final Logger logger = LogManager.getLogger(BootStrapFactory.class);

    public RangerFichierApp initialize() {
        ParameterFactory parameterFactory = new ParameterFactory();
        parameterFactory.load();
        Parameter parametres = parameterFactory.getParameter();
        logger.info("RangerFichier v{}", parametres.getVersion());
        Catalog catalog = null;

        UserInterface ui = new OptionPaneUI();
        try (Connexion connexion = new Connexion(parametres.getProperty("url"))) {
            RuleRepositoryPort ruleRepositoryPort = new RuleRepositoryAdapter(parametres,
                    connexion);
            catalog = new Catalog(ruleRepositoryPort.findAllRules());
        }

        ReportService reportService = new ReportService();
        OperationFichier operationFichier = new OperationFichier();
        FileProcessingService service = new FileProcessingService(catalog,
                reportService,
                operationFichier);

        return new RangerFichierApp(ui, service, parametres);
    }
}
