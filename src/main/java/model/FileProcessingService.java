package model;

import exceptions.DatabaseAccessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;

public class FileProcessingService {
    private static final Logger logger = LogManager.getLogger(FileProcessingService.class);
    private final Parameter parametres;
    private Catalogue catalogue;

    public FileProcessingService(Parameter parametres) {
        this.parametres = parametres;
    }

    /**
     * Charge le catalogue en utilisant une connexion injectée (facilite les tests).
     */
    public void loadCatalogue(Connexion connexion) {
        try {
            connexion.connect();
            RegleRepository repository = new RegleRepository(connexion, parametres);
            catalogue = new Catalogue();
            catalogue.chargerDepuisRepository(repository);
            logger.info("Catalogue chargé avec {} règles", catalogue.getTailleCatalogue());
        } catch (DatabaseAccessException e) {
            logger.error("Erreur critique : {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Version pratique pour la production (crée la connexion automatiquement).
     */
    public void loadCatalogue() {
        try (Connexion connexion = new Connexion(parametres.getProperty("url"))) {
            loadCatalogue(connexion);
        }
    }

    /**
     * Traite les fichiers et retourne un rapport.
     */
    public String processFiles(String[] filePaths) {
        StringBuilder rapport = new StringBuilder();
        int nbDeplacements = 0;

        for (String filePath : filePaths) {
            OperationFichier operationFichier = new OperationFichier();
            operationFichier.setPathSource(java.nio.file.Path.of(filePath));
            String dossierCible = operationFichier.rechercheCible(catalogue);

            if (dossierCible == null) {
                rapport.append("Pas de correspondance pour : ").append(filePath).append("\n");
                logger.warn("Pas de correspondance trouvée pour {}", filePath);
            } else {
                if (dossierCible.startsWith("~")) {
                    dossierCible = System.getProperty("user.home") + dossierCible.substring(1);
                }
                logger.info("Chemin cible : {}", dossierCible);
                boolean success = operationFichier.deplacement();
                if (success) {
                    rapport.append("Déplacé : ").append(filePath)
                            .append(" -> ").append(dossierCible).append("\n");
                    logger.info("Copié vers : {}", dossierCible);
                    nbDeplacements++;
                } else {
                    rapport.append("Échec du déplacement : ").append(filePath)
                            .append(" -> ").append(dossierCible).append("\n");
                }
            }
        }

        if (nbDeplacements == 0) {
            rapport.append("Aucun fichier déplacé.");
        } else {
            rapport.append(nbDeplacements).append(" fichier(s) déplacé(s).");
        }

        return rapport.toString();
    }
}