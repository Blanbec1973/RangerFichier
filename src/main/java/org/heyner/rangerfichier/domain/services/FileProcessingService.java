package org.heyner.rangerfichier.domain.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.shared.util.PathNormalizer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileProcessingService {
    private static final Logger logger = LogManager.getLogger(FileProcessingService.class);

    private final Catalog catalog;
    private final ReportService reportService;
    private final OperationFichier operationFichier;

    public FileProcessingService(Catalog catalog,
                                 ReportService reportService, OperationFichier operationFichier) {
        this.catalog = catalog;
        this.reportService = reportService;
        this.operationFichier = operationFichier;
    }

    /**
     * Traite les fichiers et retourne un rapport.
     */
    public void processFiles(String[] filePaths) {
        int nbDeplacements = 0;

        for (String filePath : filePaths) {
            //operationFichier.setPathSource(Path.of(filePath));
            Path source = Path.of(filePath);
            String fileName = source.getFileName().toString();
            Optional<String> targetDirectory = catalog.searchTargetDirectory(fileName);
            //String dossierCible = operationFichier.rechercheCible(catalog);


            if (targetDirectory.isEmpty()) {
                reportService.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                logger.info("Chemin cible : {}", targetDirectory);
                try {
                    Path destinationDirectory = PathNormalizer.normalize(targetDirectory.get());
                    operationFichier.move(source, destinationDirectory);
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