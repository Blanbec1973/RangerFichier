package org.heyner.rangerfichier.domain.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.shared.exceptions.FileMoveException;
import org.heyner.rangerfichier.shared.util.PathNormalizer;

import java.nio.file.Path;
import java.util.Optional;

public class FileProcessingService {
    private static final Logger logger = LogManager.getLogger(FileProcessingService.class);

    private final Catalog catalog;
    private final ReportBuilder reportBuilder;
    private final OperationFichier operationFichier;
    private final PathNormalizer pathNormalizer;

    public FileProcessingService(Catalog catalog,
                                 ReportBuilder reportBuilder, OperationFichier operationFichier, PathNormalizer pathNormalizer) {
        this.catalog = catalog;
        this.reportBuilder = reportBuilder;
        this.operationFichier = operationFichier;
        this.pathNormalizer = pathNormalizer;
    }

    /**
     * Traite les fichiers et retourne un rapport.
     */
    public void processFiles(String[] filePaths) {
        int nbDeplacements = 0;

        for (String filePath : filePaths) {
            Path source = Path.of(filePath);
            String fileName = source.getFileName().toString();
            Optional<String> targetDirectory = catalog.searchTargetDirectory(fileName);

            if (targetDirectory.isEmpty()) {
                reportBuilder.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                logger.info("Target directory : {}", targetDirectory.get());
                try {
                    Path targetDirectory2 = pathNormalizer.normalize(targetDirectory.get());
                    operationFichier.move(source, targetDirectory2);
                    reportBuilder.append("Déplacé : ")
                            .append(filePath)
                            .append(" -> ")
                            .append(targetDirectory.toString())
                            .append("\n");
                    logger.info("Moved to : {}", targetDirectory.get());
                    nbDeplacements++;
                } catch (FileMoveException e) {
                    reportBuilder.append("ERREUR : ")
                            .append(filePath)
                            .append(" -> ")
                            .append(e.getMessage())
                            .append("\n");
                    logger.error("Déplacement échoué pour {}", filePath, e);
                }
            }
        }
        reportBuilder.addTotalReport(nbDeplacements);

    }

    public String getReport() { return reportBuilder.getReport();
    }
}