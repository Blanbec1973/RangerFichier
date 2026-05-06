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
    private final ReportAccumulator reportAccumulator;
    private final OperationFichier operationFichier;
    private final PathNormalizer pathNormalizer;

    public FileProcessingService(Catalog catalog,
                                 ReportAccumulator reportAccumulator, OperationFichier operationFichier, PathNormalizer pathNormalizer) {
        this.catalog = catalog;
        this.reportAccumulator = reportAccumulator;
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
                reportAccumulator.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                logger.info("Target directory : {}", targetDirectory.get());
                try {
                    Path targetDirectory2 = pathNormalizer.normalize(targetDirectory.get());
                    operationFichier.move(source, targetDirectory2);
                    appendMoveReport(filePath, targetDirectory.get());
                    logger.info("Moved to : {}", targetDirectory.get());
                    nbDeplacements++;
                } catch (FileMoveException e) {
                    appendErrorReport(filePath, e);
                    logger.error("Déplacement échoué pour {}", filePath, e);
                }
            }
        }
        reportAccumulator.addSummary(nbDeplacements);

    }

    private void appendErrorReport(String filePath, FileMoveException e) {
        reportAccumulator.append("ERREUR : ")
                .append(filePath)
                .append(" -> ")
                .append(e.getMessage())
                .append("\n");
    }

    private void appendMoveReport(String filePath, String targetDirectory) {
        reportAccumulator.append("Déplacé : ")
                .append(filePath)
                .append(" -> ")
                .append(targetDirectory)
                .append("\n");
    }

    public String getReport() { return reportAccumulator.getReport();
    }
}