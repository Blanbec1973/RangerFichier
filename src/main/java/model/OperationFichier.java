package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class OperationFichier {
    private Path pathSource;
    private Path pathCible;
    private static final Logger logger = LogManager.getLogger(OperationFichier.class);

    public void setPathSource(Path pathSource) {
        this.pathSource = pathSource;
        logger.info("pathSource : {}", pathSource);
    }
    public Path getPathSource() {
        return pathSource;
    }
    public String rechercheCible(Catalogue catalogue) {
        String dossierCible = catalogue.searchTargetDirectory(pathSource.getFileName().toString());
        if (dossierCible != null) pathCible = Path.of(dossierCible+pathSource.getFileName().toString());
        return dossierCible;
    }

    public void deplacement()  {
        try {
            Files.move(pathSource, pathCible, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Fichier déplacé : {} -> {}", pathSource, pathCible);
        } catch (Exception e) {
            logger.error("Échec du déplacement de {} vers {} : {}", pathSource, pathCible, e.getMessage());
        }
    }

    public Path getPathCible() {
        return pathCible;
    }
}
