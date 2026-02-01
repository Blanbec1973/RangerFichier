package control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;

import java.io.File;
import java.net.URISyntaxException;

public class ParameterFactory {
    private static final Logger logger = LogManager.getLogger(ParameterFactory.class);
    Parameter parameter;

    public Parameter getParameter() {
        return parameter;
    }

    ParameterFactory() {
        boolean loaded = false;
        try {
            logger.debug("Début try");
            String jarDir = new File(ParameterFactory.class.getProtectionDomain()
                                                     .getCodeSource()
                                                     .getLocation()
                                                     .toURI())
                                                     .getParent();
            logger.debug("Après jarDir : {}", jarDir);
            File externalConfig = new File(jarDir, "config.properties");
            logger.debug("Après file : {}", externalConfig.getAbsolutePath());
            if (externalConfig.exists()) {
                logger.debug("Existe !");
                parameter = new Parameter(externalConfig.getAbsolutePath());
                loaded = true;
                logger.debug("loaded : {}", loaded);
            }
        } catch (URISyntaxException e) {
            logger.error("Erreur lecture fichier config proche jar.");
            throw new RuntimeException(e);
        }

        if (!loaded) {
            logger.info("Config proche jar non trouvée, chargement config interne au jar.");
            parameter = new Parameter("config.properties");
        }
    }

}
