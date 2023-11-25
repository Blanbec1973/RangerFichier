package control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Catalogue {
    private static Catalogue catalogue;
    private static final ArrayList <String> regex = new ArrayList<>();
    private static final ArrayList <String> dossierCible = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Catalogue.class);
    private Catalogue() {}
    public static Catalogue getInstance() {
        Catalogue instance = catalogue;
        if (instance != null) {
            return instance;
        }
        synchronized (Catalogue.class) {
            if (catalogue == null) catalogue = new Catalogue();
            return catalogue;
        }
    }
    public static String searchTargetDirectory(String nomFichier) {
        int i = 0;
        boolean trouve = false;
        logger.info("Nom fichier à parser regex : {}", nomFichier);
        while (i < regex.size() && !trouve) {
            trouve = nomFichier.matches(regex.get(i));
            String str = "i : "+i + " " + regex.get(i)+" "+ trouve;
            logger.info(str);
            i=i+1;
        }
        return trouve ? dossierCible.get(i-1) : null;
    }


    public static void remplir(ResultSet resultSet) {
        while (true) {
            try {
                if (!resultSet.next()) break;
                regex.add(resultSet.getString("REGEX"));
                dossierCible.add(resultSet.getString("DOSSIERDEST"));
            } catch (SQLException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

        }
        logger.info("Nombre de REGEX chargés : {}", getTailleCatalogue());
    }

    public static int getTailleCatalogue() {
        return regex.size();
    }

}
