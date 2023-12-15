package control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.sql.*;

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
    public String rechercheCible(ResultSet resultset) {
        Catalogue.getInstance();
        Catalogue.remplir(resultset);
        String dossierCible = Catalogue.searchTargetDirectory(pathSource.getFileName().toString());
        if (dossierCible != null) pathCible = Path.of(dossierCible+pathSource.getFileName().toString());
        return dossierCible;
    }

    public void deplacement()  {
        try {
            Files.move(pathSource,pathCible, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
