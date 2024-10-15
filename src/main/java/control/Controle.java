package control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;


public class Controle {
    private static final Logger logger = LogManager.getLogger(Controle.class);
    private final org.heyner.common.Parameter parametres;

    public static void main(String[] args) {
        new Controle(args,new OptionPane());
    }

    public Controle(String[] args, OptionPane jOptionPane) {

        System.setProperty( "file.encoding", "UTF-8" );
        //Initialisation paramètre, sgbd et check arguments :
        parametres = new org.heyner.common.Parameter("config.properties");
        logger.info("RangerFichier v{}",parametres.getVersion());
        try {
            checkArgs(args);
        }
        catch (ArgumentErroneException exception) {
            System.exit(-1);
        }
        logger.info("url SGBD : {}",parametres.getProperty("url"));
        Connexion connexion = new Connexion(parametres.getProperty("url"));
        connexion.connect();
        connexion.query(parametres.getProperty("sql"));
        OperationFichier operationFichier = new OperationFichier();
        operationFichier.setPathSource(Path.of(args[0]));

        // Recherche du dossier cible :
        String dossierCible = operationFichier.rechercheCible(connexion.getResultSet());
        if (dossierCible == null) {
            String str = parametres.getProperty("MsgNotFound");
            logger.info(str);
            jOptionPane.showMessageDialog(parametres.getProperty("MsgNotFound"));
            connexion.close();
            System.exit(0);
        }
        //Déplacement si chemin trouvé :
        operationFichier.deplacement();
        String str = parametres.getProperty("MsgCopyDeb") + "\n" + args[0] + "\n" + parametres.getProperty("MsgVers") + "\n" +
                dossierCible;
        logger.info("Copié vers : {}", dossierCible);
        jOptionPane.showMessageDialog(str);
        connexion.close();
    }

    private void checkArgs(String[] args) throws ArgumentErroneException {
        if(args.length == 0) {
            throw new ArgumentErroneException(parametres.getProperty("MsgErrNoFile"));
        }
        else logger.info("Argument reçu : {}", args[0]);
    }


}

