package controleur;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parametres {
    private Properties prop;
    private static final Logger logger = LogManager.getLogger(Controle.class);


    public Parametres(String nomFichier) {
        prop = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream(nomFichier);

        try {
            prop.load(input);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String getProperty(String nomProperty) {return prop.getProperty(nomProperty);}
}