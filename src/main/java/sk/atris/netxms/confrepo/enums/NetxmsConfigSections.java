package sk.atris.netxms.confrepo.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NetxmsConfigSections {
    EVENTS("events"),
    TEMPLATES("templates"),
    TRAPS("traps"),
    RULES("rules"),
    SCRIPTS("scripts"),
    OBJECT_TOOLS("objectTools"),
    DCI_SUMMARY_TABLES("dciSummaryTables");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
