package sk.atris.netxms.confrepo.tests;

import lombok.Getter;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.exceptions.RepositoryInitializationException;
import sk.atris.netxms.confrepo.model.entities.configItem.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.service.database.DbObjectHandler;

public class MockedConfigRepository {
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

    public static void setup(ItemRevision revision) throws RepositoryInitializationException, DatabaseException {
        NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

        dciSummaryTable.addRevision(revision);
        eppRule.addRevision(revision);
        event.addRevision(revision);
        objectTool.addRevision(revision);
        script.addRevision(revision);
        template.addRevision(revision);
        trap.addRevision(revision);

        DbObjectHandler.getInstance().saveToDb(revision);

        netxmsConfigRepository.addItem(dciSummaryTable);
        netxmsConfigRepository.addItem(eppRule);
        netxmsConfigRepository.addItem(event);
        netxmsConfigRepository.addItem(objectTool);
        netxmsConfigRepository.addItem(script);
        netxmsConfigRepository.addItem(template);
        netxmsConfigRepository.addItem(trap);
    }

    public static void cleanup() throws RepositoryInitializationException, DatabaseException {
        NetxmsConfigRepository.getInstance().clearAllConfig();

        dciSummaryTable.clearAllRevisions();
        eppRule.clearAllRevisions();
        event.clearAllRevisions();
        objectTool.clearAllRevisions();
        script.clearAllRevisions();
        template.clearAllRevisions();
        trap.clearAllRevisions();
    }
}
