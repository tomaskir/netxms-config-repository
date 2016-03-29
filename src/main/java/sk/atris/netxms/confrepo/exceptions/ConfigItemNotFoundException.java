package sk.atris.netxms.confrepo.exceptions;

public class ConfigItemNotFoundException extends Exception {
    public ConfigItemNotFoundException() {
    }

    public ConfigItemNotFoundException(String message) {
        super(message);
    }
}
