package control;

import exceptions.DatabaseAccessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;

import java.nio.file.Path;


public class Controle {
    private static final Logger logger = LogManager.getLogger(Controle.class);

    public static void main(String[] args) {
        new Controle(args, new OptionPane());
    }

    public Controle(String[] args, OptionPane jOptionPane) {
        System.setProperty("file.encoding", "UTF-8");
        org.heyner.common.Parameter parametres = new org.heyner.common.Parameter("config.properties");
        logger.info("RangerFichier v{}", parametres.getVersion());

        if (args.length == 0) {
            jOptionPane.showMessageDialog(parametres.getProperty("MsgErrNoFile"));
            throw new IllegalArgumentException("Arguments manquants");
        }

        try {
           chargerCatalogue(parametres);
        } catch (DatabaseAccessException e) {
            jOptionPane.showMessageDialog("Erreur SGBD : " + e.getMessage());
            throw e;
        }

        StringBuilder rapport = traiterFichiers(args);

        jOptionPane.showMessageDialog(rapport.toString());
    }
    private void chargerCatalogue(Parameter parametres) {
        try (Connexion connexion = new Connexion(parametres.getProperty("url"))) {
            connexion.connect();
            connexion.query(parametres.getProperty("sql"));
            Catalogue.getInstance().remplir(connexion.getResultSet());
        } catch (DatabaseAccessException e) {
            logger.error("Erreur critique : {}", e.getMessage());
            throw e;
        }
    }
    private StringBuilder traiterFichiers(String[] args) {
        StringBuilder rapport = new StringBuilder();
        int nbDeplacements=0;
        for (String filePath : args) {
            OperationFichier operationFichier = new OperationFichier();
            operationFichier.setPathSource(Path.of(filePath));
            String dossierCible = operationFichier.rechercheCible(Catalogue.getInstance());

            if (dossierCible == null) {
                rapport.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                operationFichier.deplacement();
                rapport.append("Déplacé : ").append(filePath)
                        .append(" -> ").append(dossierCible).append("\n");
                logger.info("Copié vers : {}", dossierCible);
                nbDeplacements++;
            }
        }
        if (nbDeplacements == 0 ) {
            rapport.append("Aucun fichier déplacé.");
        } else {
            rapport.append(nbDeplacements+" fichier(s) déplacé(s).");
        }
        return rapport;
    }
}


