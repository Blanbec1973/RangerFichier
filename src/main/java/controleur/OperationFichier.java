package controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.sql.*;

public final class OperationFichier {
    private static OperationFichier operationFichier;
    private static Path pathSource;
    private static Path pathCible;
    private static final Logger logger = LogManager.getLogger(OperationFichier.class);

    private OperationFichier() {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static OperationFichier getInstance() {
        if (operationFichier == null) {
            operationFichier = new OperationFichier();
        }
        return operationFichier;
    }

    public static void setPathSource(Path pathSource) {
        OperationFichier.pathSource = pathSource;
        logger.info("pathSource : {}", pathSource);
    }
    public static String rechercheCible(ResultSet resultset) {
        Catalogue.getInstance();
        Catalogue.remplir(resultset);
        String dossierCible = Catalogue.searchTargetDirectory(pathSource.getFileName().toString());
        if (dossierCible != null) pathCible = Path.of(dossierCible+pathSource.getFileName().toString());
        return dossierCible;
    }

    public static void deplacement()  {
        try {
            Files.move(pathSource,pathCible, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
