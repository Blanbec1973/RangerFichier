package org.heyner.rangerfichier.domain.services;

import org.heyner.rangerfichier.shared.exceptions.FileMoveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class OperationFichier {

    private static final Logger logger = LogManager.getLogger(OperationFichier.class);

    public Path move(Path source, Path targetDirectory) {
        try {
            Path targetFile = targetDirectory.resolve(source.getFileName());
            Files.createDirectories(targetFile.getParent());
            Files.move(source, targetFile, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Fichier déplacé : {} -> {}", source, targetFile);
        } catch (IOException e) {
            logger.error("Échec du déplacement de {} vers {} : {}", source, targetDirectory, e.getMessage());
            throw new FileMoveException(
                    "Impossible de déplacer le fichier : " + source.getFileName(), e);
        }
        return targetDirectory;
    }
}
