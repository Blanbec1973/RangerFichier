package model;

import exceptions.DatabaseAccessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class Catalogue {
    private static Catalogue catalogue;
    private final ArrayList <String> regex = new ArrayList<>();
    private final ArrayList <String> dossierCible = new ArrayList<>();
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
    public String searchTargetDirectory(String nomFichier) {
        int i = 0;
        boolean trouve = false;
        logger.info("Nom fichier à parser regex : {}", nomFichier);
        while (i < this.regex.size() && !trouve) {
            trouve = nomFichier.matches(this.regex.get(i));
            String str = "i : "+i + " " + this.regex.get(i)+" "+ trouve;
            logger.info(str);
            i=i+1;
        }
        return trouve ? this.dossierCible.get(i-1) : null;
    }


    public void remplir(ResultSet resultSet) {
        this.regex.clear();
        this.dossierCible.clear();

        while (true) {
            String regexStr=null;
            try {
                if (!resultSet.next()) break;
                regexStr = resultSet.getString("REGEX");
                Pattern.compile(regexStr);
                this.regex.add(regexStr);
                this.dossierCible.add(resultSet.getString("DOSSIERDEST"));
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new DatabaseAccessException("Erreur lors du remplissage catalogue", e);
            } catch (PatternSyntaxException e) {
                logger.warn("REgex invalide, ignorée : {}", regexStr);
            }

        }
        logger.info("Nombre de REGEX chargés : {}", getTailleCatalogue());
    }

    public int getTailleCatalogue() {
        return this.regex.size();
    }

    public void clear() {
        this.regex.clear();
        this.dossierCible.clear();
    }
}
