package sk.atris.netxms.confrepo.tests.service;

import lombok.Getter;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;

public class TestEnvironment {
    private final static NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

    @Getter
    private final static DciSummaryTable dciSummaryTable = new DciSummaryTable("guid1", "title");

    @Getter
    private final static EppRule eppRule = new EppRule("guid2", "comment");

    @Getter
    private final static Event event = new Event("guid3", "name");

    @Getter
    private final static ObjectTool objectTool = new ObjectTool("guid4", "name");

    @Getter
    private final static Script script = new Script("guid5", "name");

    @Getter
    private final static Template template = new Template("guid6", "name");

    @Getter
    private final static Trap trap = new Trap("guid7", "description");

    public static void setup(Revision revision) {
        dciSummaryTable.addRevision(revision);
        eppRule.addRevision(revision);
        event.addRevision(revision);
        objectTool.addRevision(revision);
        script.addRevision(revision);
        template.addRevision(revision);
        trap.addRevision(revision);

        netxmsConfigRepository.addItem(dciSummaryTable);
        netxmsConfigRepository.addItem(eppRule);
        netxmsConfigRepository.addItem(event);
        netxmsConfigRepository.addItem(objectTool);
        netxmsConfigRepository.addItem(script);
        netxmsConfigRepository.addItem(template);
        netxmsConfigRepository.addItem(trap);
    }

    public static void cleanup() {
        netxmsConfigRepository.clearAllConfig();

        dciSummaryTable.clearAllRevisions();
        eppRule.clearAllRevisions();
        event.clearAllRevisions();
        objectTool.clearAllRevisions();
        script.clearAllRevisions();
        template.clearAllRevisions();
        trap.clearAllRevisions();
    }
}
