package sk.atris.netxms.confrepo.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NetxmsGeneratedConfigXml {
    ROOT_ELEMENT_NAME("configuration"),

    FORMAT_VERSION_NAME("formatVersion"),
    FORMAT_VERSION_VALUE("4"),

    DESCRIPTION_NAME("description"),
    DESCRIPTION_VALUE("Generated configuration XML.");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
