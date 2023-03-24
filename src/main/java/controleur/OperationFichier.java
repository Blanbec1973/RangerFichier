package controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class OperationFichier {
    private static OperationFichier operationFichier;
    private static Path pathSource;

    private static Path pathCible;
    private static final Logger logger = LogManager.getLogger(Controle.class);

    private OperationFichier(Path pathSource) {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        logger.info("pathSource : " + pathSource.toString());
        this.pathSource = pathSource;
    }

    public static OperationFichier getInstance(Path pathSource) {
        if (operationFichier == null) {
            operationFichier = new OperationFichier(pathSource);
        }
        return operationFichier;
    }

    public static String rechercheCible() {
        Catalogue.getInstance(null); //TODO nom de fichier
        String dossierCible = Catalogue.parcourir(pathSource.getFileName().toString());
        if (dossierCible != null) pathCible = Path.of(dossierCible+"\\"+pathSource.getFileName().toString());
        return dossierCible == null ? null : dossierCible;
    }

    public static void deplacement()  {
        try {
            Path path = Files.move(pathSource,pathCible, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
        }

    }
}
