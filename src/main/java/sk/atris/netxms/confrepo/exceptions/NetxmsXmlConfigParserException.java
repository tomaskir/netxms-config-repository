package sk.atris.netxms.confrepo.exceptions;

public final class NetxmsXmlConfigParserException extends Exception{
    public NetxmsXmlConfigParserException(Throwable cause) {
        super(cause);
    }

    public NetxmsXmlConfigParserException(String message) {
        super(message);
    }
}
