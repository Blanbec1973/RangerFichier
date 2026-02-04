package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileProcessingService {
    private static final Logger logger = LogManager.getLogger(FileProcessingService.class);
    private final RegleRepository regleRepository;
    private final Catalogue catalogue;
    private final ReportService reportService;

    public FileProcessingService(RegleRepository regleRepository, Catalogue catalogue,
                                 ReportService reportService) {
        this.regleRepository = regleRepository;
        this.catalogue = catalogue;
        this.reportService = reportService;
    }

    protected OperationFichier createOperationFichier() {
        return new OperationFichier();
    }

    public void loadCatalog() {
        catalogue.chargerDepuisRepository(regleRepository);
    }

    /**
     * Traite les fichiers et retourne un rapport.
     */
    public void processFiles(String[] filePaths) {
        int nbDeplacements = 0;

        for (String filePath : filePaths) {
            OperationFichier operationFichier = createOperationFichier();
            operationFichier.setPathSource(java.nio.file.Path.of(filePath));
            String dossierCible = operationFichier.rechercheCible(catalogue);

            if (dossierCible == null) {
                reportService.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                if (dossierCible.startsWith("~")) {
                    dossierCible = System.getProperty("user.home") + dossierCible.substring(1);
                }
                logger.info("Chemin cible : {}", dossierCible);
                boolean success = operationFichier.deplacement();
                if (success) {
                    reportService.append("Déplacé : ").append(filePath)
                            .append(" -> ").append(dossierCible).append("\n");
                    logger.info("Copié vers : {}", dossierCible);
                    nbDeplacements++;
                } else {
                    reportService.append("Échec du déplacement : ").append(filePath)
                            .append(" -> ").append(dossierCible).append("\n");
                }
            }
        }
        reportService.addTotalReport(nbDeplacements);

    }

    public String getReport() { return reportService.getReport();
    }
}