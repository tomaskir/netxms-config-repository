package sk.atris.netxms.confrepo.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApplicationConfiguration {
    CONFIG_FILE_NAME("config.properties"),

    READONLY_ACCESS_TOKEN_PROPERTY("access.token.ReadOnly"),
    READWRITE_ACCESS_TOKEN_PROPERTY("access.token.ReadWrite");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
