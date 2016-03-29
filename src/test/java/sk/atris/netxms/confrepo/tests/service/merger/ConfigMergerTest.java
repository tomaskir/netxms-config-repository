package sk.atris.netxms.confrepo.tests.service.merger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfig;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.service.merger.ConfigMerger;

public class ConfigMergerTest {
    private final NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();
    private final ConfigMerger configMerger = ConfigMerger.getInstance();

    private final DciSummaryTable originalDciSummaryTable = new DciSummaryTable("guid1", "title");
    private final EppRule originalEppRule = new EppRule("guid2", "comment");
    private final Event originalEvent = new Event("guid3", "name");
    private final ObjectTool originalObjectTool = new ObjectTool("guid4", "name");
    private final Script originalScript = new Script("guid5", "name");
    private final Template originalTemplate = new Template("guid6", "name");
    private final Trap originalTrap = new Trap("guid7", "description");

    private final Revision originalRevision = new Revision("xml-code", "Revision message.");
    private final Revision newRevision = new Revision("xml-code-updated", "New revision message.");

    @Before
    public void environmentSetup() {
        originalDciSummaryTable.addRevision(originalRevision);
        originalEppRule.addRevision(originalRevision);
        originalEvent.addRevision(originalRevision);
        originalObjectTool.addRevision(originalRevision);
        originalScript.addRevision(originalRevision);
        originalTemplate.addRevision(originalRevision);
        originalTrap.addRevision(originalRevision);

        netxmsConfigRepository.addItem(originalDciSummaryTable);
        netxmsConfigRepository.addItem(originalEppRule);
        netxmsConfigRepository.addItem(originalEvent);
        netxmsConfigRepository.addItem(originalObjectTool);
        netxmsConfigRepository.addItem(originalScript);
        netxmsConfigRepository.addItem(originalTemplate);
        netxmsConfigRepository.addItem(originalTrap);
    }

    @After
    public void environmentCleaup() {
        netxmsConfigRepository.clearAllConfig();

        originalDciSummaryTable.clearAllRevisions();
        originalEppRule.clearAllRevisions();
        originalEvent.clearAllRevisions();
        originalObjectTool.clearAllRevisions();
        originalScript.clearAllRevisions();
        originalTemplate.clearAllRevisions();
        originalTrap.clearAllRevisions();
    }

    @Test
    public void testEmptyMerge() {
        // build an empty NetxmsConfig
        NetxmsConfig netxmsConfig = new NetxmsConfig();

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
    public void testNoItemsAdded() {
        // build a NetxmsConfig object containing no new config items
        NetxmsConfig netxmsConfig = new NetxmsConfig();

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
    public void testAllItemsAdded() {
        // build a NetxmsConfig object containing all possible new config items
        DciSummaryTable newDciSummaryTable = new DciSummaryTable(originalDciSummaryTable.getGuid() + "-new", originalDciSummaryTable.getTitle() + "-new");
        EppRule newEppRule = new EppRule(originalEppRule.getGuid() + "-new", originalEppRule.getComment() + "-new");
        Event newEvent = new Event(originalEvent.getGuid() + "-new", originalEvent.getName() + "-new");
        ObjectTool newObjectTool = new ObjectTool(originalObjectTool.getGuid() + "-new", originalObjectTool.getName() + "-new");
        Script newScript = new Script(originalScript.getGuid() + "-new", originalScript.getName() + "-new");
        Template newTemplate = new Template(originalTemplate.getGuid() + "-new", originalTemplate.getName() + "-new");
        Trap newTrap = new Trap(originalTrap.getGuid() + "-new", originalTrap.getDescription() + "-new");

        newDciSummaryTable.addRevision(originalRevision);
        newEppRule.addRevision(originalRevision);
        newEvent.addRevision(originalRevision);
        newObjectTool.addRevision(originalRevision);
        newScript.addRevision(originalRevision);
        newTemplate.addRevision(originalRevision);
        newTrap.addRevision(originalRevision);

        NetxmsConfig netxmsConfig = new NetxmsConfig();

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
    public void testNoRevisionChanges() {
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

        NetxmsConfig netxmsConfig = new NetxmsConfig();

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
    public void testFullRevisionMerge() {
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

        NetxmsConfig netxmsConfig = new NetxmsConfig();

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
