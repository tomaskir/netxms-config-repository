package sk.atris.netxms.confrepo.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApplicationConfiguration {
    CONFIG_FILE_NAME("config.properties"),

    PROPERTY_RO_ACCESS_TOKEN("access.token.ReadOnly"),
    PROPERTY_RW_ACCESS_TOKEN("access.token.ReadWrite"),

    PROPERTY_DATABASE_PATH("database.file.path"),
    PROPERTY_DATABASE_USER("database.user"),
    PROPERTY_DATABASE_PASS("database.pass");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
