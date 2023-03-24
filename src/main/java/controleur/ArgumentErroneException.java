package controleur;

public class ArgumentErroneException extends Exception {

    public ArgumentErroneException() {
        super();
    }

    public ArgumentErroneException(String s) {
        super(s);
    }
}
