package sk.atris.netxms.confrepo.tests.service.merger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.exceptions.RepositoryInitializationException;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.model.netxmsConfig.ReceivedNetxmsConfig;
import sk.atris.netxms.confrepo.service.merger.ConfigMerger;
import sk.atris.netxms.confrepo.tests.MockedConfigRepository;
import sk.atris.netxms.confrepo.tests.MockedDatabase;

public class ConfigMergerTest {
    private final ConfigMerger configMerger = ConfigMerger.getInstance();

    private final DciSummaryTable originalDciSummaryTable = MockedConfigRepository.getDciSummaryTable();
    private final EppRule originalEppRule = MockedConfigRepository.getEppRule();
    private final Event originalEvent = MockedConfigRepository.getEvent();
    private final ObjectTool originalObjectTool = MockedConfigRepository.getObjectTool();
    private final Script originalScript = MockedConfigRepository.getScript();
    private final Template originalTemplate = MockedConfigRepository.getTemplate();
    private final Trap originalTrap = MockedConfigRepository.getTrap();

    private final Revision originalRevision = new Revision("xml-code", "Revision message.", 1);
    private final Revision newRevision = new Revision("xml-code-updated", "New revision message.", 2);

    @Before
    public void environmentSetup() throws ReflectiveOperationException, DatabaseException, RepositoryInitializationException {
        MockedDatabase.setup();
        MockedConfigRepository.setup(originalRevision);
    }

    @After
    public void environmentCleanup() throws ReflectiveOperationException, DatabaseException, RepositoryInitializationException {
        MockedConfigRepository.cleanup();
        MockedDatabase.cleanup();
    }

    @Test
    public void testEmptyMerge() throws DatabaseException, RepositoryInitializationException {
        NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

        // build an empty NetxmsConfig
        ReceivedNetxmsConfig netxmsConfig = new ReceivedNetxmsConfig();

        // merge it to the NetxmsConfigRepository
        configMerger.mergeConfiguration(netxmsConfig);

        // assert if the merged data is as intended
        assert netxmsConfigRepository.getRepository(DciSummaryTable.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(EppRule.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Event.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(ObjectTool.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Script.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Template.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Trap.class).getConfigItemCount() == 1;

        assert netxmsConfigRepository.getRepository(DciSummaryTable.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(EppRule.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Event.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(ObjectTool.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Script.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Template.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Trap.class).getLastConfigItem().getRevisionCount() == 1;
    }

    @Test
    public void testNoItemsAdded() throws DatabaseException, RepositoryInitializationException {
        NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

        // build a NetxmsConfig object containing no new config items
        ReceivedNetxmsConfig netxmsConfig = new ReceivedNetxmsConfig();

        netxmsConfig.addItem(originalDciSummaryTable);
        netxmsConfig.addItem(originalEppRule);
        netxmsConfig.addItem(originalEvent);
        netxmsConfig.addItem(originalObjectTool);
        netxmsConfig.addItem(originalScript);
        netxmsConfig.addItem(originalTemplate);
        netxmsConfig.addItem(originalTrap);

        // merge it to the NetxmsConfigRepository
        configMerger.mergeConfiguration(netxmsConfig);

        // assert if the merged data is as intended
        assert netxmsConfigRepository.getRepository(DciSummaryTable.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(EppRule.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Event.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(ObjectTool.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Script.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Template.class).getConfigItemCount() == 1;
        assert netxmsConfigRepository.getRepository(Trap.class).getConfigItemCount() == 1;
    }

    @Test
    public void testAllItemsAdded() throws DatabaseException, RepositoryInitializationException {
        NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

        // build a NetxmsConfig object containing all possible new config items
        DciSummaryTable newDciSummaryTable = new DciSummaryTable(originalDciSummaryTable.getGuid() + "-new", originalDciSummaryTable.getTitle() + "-new");
        EppRule newEppRule = new EppRule(originalEppRule.getGuid() + "-new", originalEppRule.getComment() + "-new");
        Event newEvent = new Event(originalEvent.getGuid() + "-new", originalEvent.getName() + "-new");
        ObjectTool newObjectTool = new ObjectTool(originalObjectTool.getGuid() + "-new", originalObjectTool.getName() + "-new");
        Script newScript = new Script(originalScript.getGuid() + "-new", originalScript.getName() + "-new");
        Template newTemplate = new Template(originalTemplate.getGuid() + "-new", originalTemplate.getName() + "-new");
        Trap newTrap = new Trap(originalTrap.getGuid() + "-new", originalTrap.getDescription() + "-new");

        newDciSummaryTable.addRevision(newRevision);
        newEppRule.addRevision(newRevision);
        newEvent.addRevision(newRevision);
        newObjectTool.addRevision(newRevision);
        newScript.addRevision(newRevision);
        newTemplate.addRevision(newRevision);
        newTrap.addRevision(newRevision);

        ReceivedNetxmsConfig netxmsConfig = new ReceivedNetxmsConfig();

        netxmsConfig.addItem(newDciSummaryTable);
        netxmsConfig.addItem(newEppRule);
        netxmsConfig.addItem(newEvent);
        netxmsConfig.addItem(newObjectTool);
        netxmsConfig.addItem(newScript);
        netxmsConfig.addItem(newTemplate);
        netxmsConfig.addItem(newTrap);

        // merge it to the NetxmsConfigRepository
        configMerger.mergeConfiguration(netxmsConfig);

        // assert if the merged data is as intended
        assert netxmsConfigRepository.getRepository(DciSummaryTable.class).getConfigItemCount() == 2;
        assert netxmsConfigRepository.getRepository(EppRule.class).getConfigItemCount() == 2;
        assert netxmsConfigRepository.getRepository(Event.class).getConfigItemCount() == 2;
        assert netxmsConfigRepository.getRepository(ObjectTool.class).getConfigItemCount() == 2;
        assert netxmsConfigRepository.getRepository(Script.class).getConfigItemCount() == 2;
        assert netxmsConfigRepository.getRepository(Template.class).getConfigItemCount() == 2;
        assert netxmsConfigRepository.getRepository(Trap.class).getConfigItemCount() == 2;
    }

    @Test
    public void testNoRevisionChanges() throws DatabaseException, RepositoryInitializationException {
        NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

        // build a NetxmsConfig object containing no new revisions
        DciSummaryTable newDciSummaryTable = new DciSummaryTable(originalDciSummaryTable.getGuid(), originalDciSummaryTable.getTitle());
        EppRule newEppRule = new EppRule(originalEppRule.getGuid(), originalEppRule.getComment());
        Event newEvent = new Event(originalEvent.getGuid(), originalEvent.getName());
        ObjectTool newObjectTool = new ObjectTool(originalObjectTool.getGuid(), originalObjectTool.getName());
        Script newScript = new Script(originalScript.getGuid(), originalScript.getName());
        Template newTemplate = new Template(originalTemplate.getGuid(), originalTemplate.getName());
        Trap newTrap = new Trap(originalTrap.getGuid(), originalTrap.getDescription());

        newDciSummaryTable.addRevision(originalRevision);
        newEppRule.addRevision(originalRevision);
        newEvent.addRevision(originalRevision);
        newObjectTool.addRevision(originalRevision);
        newScript.addRevision(originalRevision);
        newTemplate.addRevision(originalRevision);
        newTrap.addRevision(originalRevision);

        ReceivedNetxmsConfig netxmsConfig = new ReceivedNetxmsConfig();

        netxmsConfig.addItem(newDciSummaryTable);
        netxmsConfig.addItem(newEppRule);
        netxmsConfig.addItem(newEvent);
        netxmsConfig.addItem(newObjectTool);
        netxmsConfig.addItem(newScript);
        netxmsConfig.addItem(newTemplate);
        netxmsConfig.addItem(newTrap);

        // merge it to the NetxmsConfigRepository
        configMerger.mergeConfiguration(netxmsConfig);

        // assert if the merged data is as intended
        assert netxmsConfigRepository.getRepository(DciSummaryTable.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(EppRule.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Event.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(ObjectTool.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Script.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Template.class).getLastConfigItem().getRevisionCount() == 1;
        assert netxmsConfigRepository.getRepository(Trap.class).getLastConfigItem().getRevisionCount() == 1;
    }

    @Test
    public void testFullRevisionMerge() throws DatabaseException, RepositoryInitializationException {
        NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

        // build a NetxmsConfig object containing new revisions of all existing items
        DciSummaryTable newDciSummaryTable = new DciSummaryTable(originalDciSummaryTable.getGuid(), originalDciSummaryTable.getTitle());
        EppRule newEppRule = new EppRule(originalEppRule.getGuid(), originalEppRule.getComment());
        Event newEvent = new Event(originalEvent.getGuid(), originalEvent.getName());
        ObjectTool newObjectTool = new ObjectTool(originalObjectTool.getGuid(), originalObjectTool.getName());
        Script newScript = new Script(originalScript.getGuid(), originalScript.getName());
        Template newTemplate = new Template(originalTemplate.getGuid(), originalTemplate.getName());
        Trap newTrap = new Trap(originalTrap.getGuid(), originalTrap.getDescription());

        newDciSummaryTable.addRevision(newRevision);
        newEppRule.addRevision(newRevision);
        newEvent.addRevision(newRevision);
        newObjectTool.addRevision(newRevision);
        newScript.addRevision(newRevision);
        newTemplate.addRevision(newRevision);
        newTrap.addRevision(newRevision);

        ReceivedNetxmsConfig netxmsConfig = new ReceivedNetxmsConfig();

        netxmsConfig.addItem(newDciSummaryTable);
        netxmsConfig.addItem(newEppRule);
        netxmsConfig.addItem(newEvent);
        netxmsConfig.addItem(newObjectTool);
        netxmsConfig.addItem(newScript);
        netxmsConfig.addItem(newTemplate);
        netxmsConfig.addItem(newTrap);

        // merge it to the NetxmsConfigRepository
        configMerger.mergeConfiguration(netxmsConfig);

        // assert if the merged data is as intended
        assert netxmsConfigRepository.getRepository(DciSummaryTable.class).getLastConfigItem().getRevisionCount() == 2;
        assert netxmsConfigRepository.getRepository(EppRule.class).getLastConfigItem().getRevisionCount() == 2;
        assert netxmsConfigRepository.getRepository(Event.class).getLastConfigItem().getRevisionCount() == 2;
        assert netxmsConfigRepository.getRepository(ObjectTool.class).getLastConfigItem().getRevisionCount() == 2;
        assert netxmsConfigRepository.getRepository(Script.class).getLastConfigItem().getRevisionCount() == 2;
        assert netxmsConfigRepository.getRepository(Template.class).getLastConfigItem().getRevisionCount() == 2;
        assert netxmsConfigRepository.getRepository(Trap.class).getLastConfigItem().getRevisionCount() == 2;
    }
}
