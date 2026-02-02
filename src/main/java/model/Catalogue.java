package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class Catalogue {
    private final ArrayList <String> regex = new ArrayList<>();
    private final ArrayList <String> dossierCible = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Catalogue.class);

    public Catalogue() {

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


        public void chargerDepuisRepository(RegleRepository repository) {
            List<Regle> regles = repository.findAllRegles();
            this.regex.clear();
            this.dossierCible.clear();
            for (Regle regle : regles) {
                this.regex.add(regle.regex());
                this.dossierCible.add(regle.dossier());
            }
            logger.info("Catalogue chargé avec {} règles", getTailleCatalogue());
        }


    public int getTailleCatalogue() {
        return this.regex.size();
    }

}
