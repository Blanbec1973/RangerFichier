package controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;


public final class Catalogue {
    private static Catalogue catalogue;
    //private static File fichier;
    private static final ArrayList <String> regex = new ArrayList<>();
    private static final ArrayList <String> dossierCible = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Controle.class);
    private Catalogue(File fichier) {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //this.fichier = fichier;
        remplir();
    }
    public static Catalogue getInstance(File fichier) {
        if (catalogue == null) {
            catalogue = new Catalogue(fichier);
        }
        return catalogue;
    }
    public static String parcourir(String nomFichier) {
        int i = 0;
        boolean trouve = false;
        logger.info("Nom fichier à parser regex : "+nomFichier);
        while (i < regex.size() && !trouve) {
            trouve = nomFichier.matches(regex.get(i));
            logger.info("i : "+i + " " + regex.get(i)+" "+ trouve);
            i=i+1;
        }
        return trouve ? dossierCible.get(i-1) : null;
    }


    private void remplir() {
        //TODO chargement par fichier
        regex.add("^NDF.*\\.(pdf|jpg|JPG|png)$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\B-BU Grand Ouest\\NDF");

        regex.add("^.*31719388832.*\\.pdf$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Banque Populaire\\BPVF\\Compte chèque");

        regex.add(".*31798828062.*\\.pdf");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Banque Populaire\\BPVF\\Richard\\31798828062-PEA");

        regex.add(".*31819264396.*\\.pdf");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Banque Populaire\\BPVF\\Juliette");

        regex.add("^.*AV MAIF.*Cécile.*\\.pdf$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Maif\\Assurance-vie");

        regex.add("^.*AV MAIF.*Richard.*\\.pdf$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Maif\\Assurance-vie");

        regex.add("^.*PER MAIF.*Cécile.*\\.pdf$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Maif\\PER Cécile");

        regex.add("^.*PER MAIF.*Richard.*\\.pdf$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Maif\\PER Richard");

        regex.add("^.*warranty.*\\.pdf$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Factures");

        regex.add("^.*Facture.*\\.(pdf|jpg|JPG)$");
        dossierCible.add("C:\\Users\\heynerr\\Documents\\0-Personnel\\Gestion\\Factures");

    }



}
