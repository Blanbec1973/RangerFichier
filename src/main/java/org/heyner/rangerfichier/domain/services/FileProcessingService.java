package org.heyner.rangerfichier.domain.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.shared.util.PathNormalizer;

import java.nio.file.Path;
import java.util.Optional;

public class FileProcessingService {
    private static final Logger logger = LogManager.getLogger(FileProcessingService.class);

    private final Catalog catalog;
    private final ReportBuilder reportBuilder;
    private final OperationFichier operationFichier;

    public FileProcessingService(Catalog catalog,
                                 ReportBuilder reportBuilder, OperationFichier operationFichier) {
        this.catalog = catalog;
        this.reportBuilder = reportBuilder;
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
                reportBuilder.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                logger.info("Chemin cible : {}", targetDirectory);
                try {
                    Path targetDirectory2 = PathNormalizer.normalize(targetDirectory.get());
                    operationFichier.move(source, targetDirectory2);
                    reportBuilder.append("Déplacé : ")
                            .append(filePath)
                            .append(" -> ")
                            .append(targetDirectory.toString())
                            .append("\n");
                    logger.info("Déplacé vers : {}", targetDirectory);
                    nbDeplacements++;
                } catch (Exception e) {
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