package controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;


public class Controle {
    private static final Logger logger = LogManager.getLogger(Controle.class);
    private static Parametres parametres;

    public static void main(String[] args) throws ArgumentErroneException {
        parametres = new Parametres("config.properties");
        logger.info("RangerFichier v{}",parametres.getProperty("version"));
        controle(args);
        OperationFichier.getInstance(Path.of(args[0]));
        String dossierCible = OperationFichier.rechercheCible();
        if (dossierCible == null) {
            logger.info(parametres.getProperty("MsgNotFound"));
            javax.swing.JOptionPane.showMessageDialog(null, parametres.getProperty("MsgNotFound"));
            System.exit(0);
        }
        OperationFichier.deplacement();
        StringBuilder str = new StringBuilder(parametres.getProperty("MsgCopyDeb"));
        str.append("\n"+args[0]+"\n"+parametres.getProperty("MsgVers")+"\n");
        str.append(dossierCible);
        logger.info("Copié vers : "+dossierCible);
        javax.swing.JOptionPane.showMessageDialog(null, str.toString());

    }

    private static void controle(String[] args) throws ArgumentErroneException {
        if(args.length == 0) {
            logger.error(parametres.getProperty("MsgErrNoFile"));
            throw new ArgumentErroneException("Saisie erronée : chaine vide");
        }
        logger.info("Argument reçu : "+args[0]);
    }


}

