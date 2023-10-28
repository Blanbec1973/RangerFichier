package controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;


public class Controle {
    private static final Logger logger = LogManager.getLogger(Controle.class);
    private static Parametres parametres;

    public static void main(String[] args) throws ArgumentErroneException {
        System.setProperty( "file.encoding", "UTF-8" );
        //Initialisation paramètre, sgbd et check arguments :
        parametres = new Parametres("config.properties");
        logger.info("RangerFichier v{}",parametres.getVersion());
        controle(args);
        Connexion connexion = new Connexion(parametres.getProperty("url"));
        connexion.connect();
        connexion.query(parametres.getProperty("sql"));
        OperationFichier.getInstance();
        OperationFichier.setPathSource(Path.of(args[0]));

        // Recherche du dossier cible :
        String dossierCible = OperationFichier.rechercheCible(connexion.getResultSet());
        if (dossierCible == null) {
            String str = parametres.getProperty("MsgNotFound");
            logger.info(str);
            javax.swing.JOptionPane.showMessageDialog(null, parametres.getProperty("MsgNotFound"));
            connexion.close();
            System.exit(0);
        }
        //Déplacement si chemin trouvé :
        OperationFichier.deplacement();
        String str = parametres.getProperty("MsgCopyDeb") + "\n" + args[0] + "\n" + parametres.getProperty("MsgVers") + "\n" +
                dossierCible;
        logger.info("Copié vers : {}", dossierCible);
        javax.swing.JOptionPane.showMessageDialog(null, str);
        connexion.close();
    }

    private static void controle(String[] args) throws ArgumentErroneException {
        if(args.length == 0) {
            throw new ArgumentErroneException(parametres.getProperty("MsgErrNoFile"));
        }
        else logger.info("Argument reçu : {}", args[0]);
    }


}

