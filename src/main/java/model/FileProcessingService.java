package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.domain.services.ReportService;

import java.nio.file.Path;
import java.util.Optional;

public class FileProcessingService {
    private static final Logger logger = LogManager.getLogger(FileProcessingService.class);

    private final Catalog catalog;
    private final ReportService reportService;

    public FileProcessingService( Catalog catalog,
                                 ReportService reportService) {
        this.catalog = catalog;
        this.reportService = reportService;
    }

    protected OperationFichier createOperationFichier() {
        return new OperationFichier();
    }


    /**
     * Traite les fichiers et retourne un rapport.
     */
    public void processFiles(String[] filePaths) {
        int nbDeplacements = 0;

        for (String filePath : filePaths) {
            OperationFichier operationFichier = createOperationFichier();
            operationFichier.setPathSource(Path.of(filePath));
            String fileName = Path.of(filePath).getFileName().toString();
            Optional<Path> targetDirectory = catalog.searchTargetDirectory(fileName);
            //String dossierCible = operationFichier.rechercheCible(catalog);


            if (targetDirectory.isEmpty()) {
                reportService.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                logger.info("Chemin cible : {}", targetDirectory);
                try {
                    operationFichier.setPathSource(targetDirectory.get());
                    operationFichier.deplacement();
                    reportService.append("Déplacé : ")
                            .append(filePath)
                            .append(" -> ")
                            .append(targetDirectory.toString())
                            .append("\n");
                    logger.info("Déplacé vers : {}", targetDirectory);
                    nbDeplacements++;
                } catch (Exception e) {
                    reportService.append("ERREUR : ")
                            .append(filePath)
                            .append(" -> ")
                            .append(e.getMessage())
                            .append("\n");
                    logger.error("Déplacement échoué pour {}", filePath, e);
                }
            }
        }
        reportService.addTotalReport(nbDeplacements);

    }

    public String getReport() { return reportService.getReport();
    }
}