package org.heyner.rangerfichier.domain.services;

import org.heyner.rangerfichier.domain.Catalog;
import org.heyner.rangerfichier.shared.exceptions.FileMoveException;
import org.heyner.rangerfichier.shared.util.PathNormalizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class OperationFichier {
    private Path pathSource;

    public void setPathCible(Path pathCible) {
        this.pathCible = pathCible;
    }

    private Path pathCible;
    private static final Logger logger = LogManager.getLogger(OperationFichier.class);

    public void setPathSource(Path pathSource) {
        this.pathSource = pathSource;
        logger.info("pathSource : {}", pathSource);
    }
    public Path getPathSource() {
        return pathSource;
    }

    public String rechercheCible(Catalog catalog) {
        String dossierCible = String.valueOf(catalog.searchTargetDirectory(pathSource.getFileName().toString()));
        if (dossierCible != null) {
            Path base = PathNormalizer.normalize(dossierCible);
            pathCible = base.resolve(pathSource.getFileName());
        }
        return dossierCible;
    }

    public boolean deplacement() {
        try {
            Files.createDirectories(pathCible.getParent());
            Files.move(pathSource, pathCible, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Fichier déplacé : {} -> {}", pathSource, pathCible);
            return true;
        } catch (Exception e) {
            logger.error("Échec du déplacement de {} vers {} : {}", pathSource, pathCible, e.getMessage());
            throw new FileMoveException(
                    "Impossible de déplacer le fichier : " + pathSource.getFileName(), e);
        }
    }

    public Path getPathCible() {
        return pathCible;
    }
}
