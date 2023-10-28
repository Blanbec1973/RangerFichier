package controleur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArgumentErroneException extends Exception {
    private static final Logger logger = LogManager.getLogger(ArgumentErroneException.class);
    public ArgumentErroneException(String s) {
        super(s);
        logger.error(s);
    }
}
