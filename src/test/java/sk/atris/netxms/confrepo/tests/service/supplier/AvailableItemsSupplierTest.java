package sk.atris.netxms.confrepo.tests.service.supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.exceptions.NetxmsXmlConfigParserException;
import sk.atris.netxms.confrepo.exceptions.RepositoryInitializationException;
import sk.atris.netxms.confrepo.model.entities.configItem.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.model.netxmsConfig.ReceivedNetxmsConfig;
import sk.atris.netxms.confrepo.service.merger.ConfigMerger;
import sk.atris.netxms.confrepo.service.parser.NetxmsXmlConfigParser;
import sk.atris.netxms.confrepo.service.supplier.AvailableItemsSupplier;
import sk.atris.netxms.confrepo.tests.MockedDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class AvailableItemsSupplierTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void environmentSetup() throws ReflectiveOperationException {
        MockedDatabase.setup();
    }

    @After
    public void environmentCleanup() throws ReflectiveOperationException, RepositoryInitializationException, DatabaseException {
        NetxmsConfigRepository.getInstance().clearAllConfig();
        MockedDatabase.cleanup();
    }

    @Test
    public void testAllAvailableItemsJson1() throws IOException, NetxmsXmlConfigParserException, NoSuchFieldException, IllegalAccessException, DatabaseException, RepositoryInitializationException {
        InputStream fis1 = new FileInputStream("src/test/resources/full_config.xml");
        ReceivedNetxmsConfig netxmsConfig = NetxmsXmlConfigParser.getInstance().parse(fis1);
        fis1.close();

        // adjust the timestamp on all latest revisions to match the timestamp in the validation file
        adjustAllLastRevisionsTimestamp(netxmsConfig, 1);

        ConfigMerger.getInstance().mergeConfiguration(netxmsConfig);

        String generatedJsonString = AvailableItemsSupplier.getInstance().getAllAvailableItemsJson(NetxmsConfigRepository.getInstance());
        JsonNode fromGeneratedString = mapper.readTree(generatedJsonString);

        InputStream fis2 = new FileInputStream("src/test/resources/allConfigItems1.json");
        JsonNode fromValidationFile = mapper.readTree(fis2);
        fis2.close();

        assert fromGeneratedString.toString().equals(fromValidationFile.toString());
    }

    @Test
    public void testAllAvailableItemsJson2() throws IOException, NetxmsXmlConfigParserException, NoSuchFieldException, IllegalAccessException, DatabaseException, RepositoryInitializationException {
        InputStream fis1 = new FileInputStream("src/test/resources/full_config.xml");
        ReceivedNetxmsConfig netxmsConfig = NetxmsXmlConfigParser.getInstance().parse(fis1);
        fis1.close();

        // adjust the timestamp on all latest revisions to match the timestamp in the validation file
        adjustAllLastRevisionsTimestamp(netxmsConfig, 1);

        ItemRevision r = new ItemRevision(null, "a newer revision", 2);
        addRevisionToAllConfigItems(netxmsConfig, r);

        // adjust the timestamp again, since we added a revision
        adjustAllLastRevisionsTimestamp(netxmsConfig, 2);

        ConfigMerger.getInstance().mergeConfiguration(netxmsConfig);

        String generatedJsonString = AvailableItemsSupplier.getInstance().getAllAvailableItemsJson(NetxmsConfigRepository.getInstance());
        JsonNode fromGeneratedString = mapper.readTree(generatedJsonString);

        InputStream fis2 = new FileInputStream("src/test/resources/allConfigItems2.json");
        JsonNode fromValidationFile = mapper.readTree(fis2);
        fis2.close();

        assert fromGeneratedString.toString().equals(fromValidationFile.toString());
    }

    private void adjustAllLastRevisionsTimestamp(ReceivedNetxmsConfig netxmsConfig, long newTimestamp) throws NoSuchFieldException, IllegalAccessException {
        List<ItemRevision> revisionList = new ArrayList<>();

        revisionList.add(netxmsConfig.getRepository(DciSummaryTable.class).getLastConfigItem().getLatestRevision());
        revisionList.add(netxmsConfig.getRepository(EppRule.class).getLastConfigItem().getLatestRevision());
        revisionList.add(netxmsConfig.getRepository(Event.class).getLastConfigItem().getLatestRevision());
        revisionList.add(netxmsConfig.getRepository(ObjectTool.class).getLastConfigItem().getLatestRevision());
        revisionList.add(netxmsConfig.getRepository(Script.class).getLastConfigItem().getLatestRevision());
        revisionList.add(netxmsConfig.getRepository(Template.class).getLastConfigItem().getLatestRevision());
        revisionList.add(netxmsConfig.getRepository(Trap.class).getLastConfigItem().getLatestRevision());

        for (ItemRevision r : revisionList) {
            Field f = r.getClass().getDeclaredField("timestamp");

            f.setAccessible(true);

            // reset the final modifier
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);

            // set the new value
            f.set(r, newTimestamp);
        }
    }

    private void addRevisionToAllConfigItems(ReceivedNetxmsConfig netxmsConfig, ItemRevision r) {
        netxmsConfig.getRepository(DciSummaryTable.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(EppRule.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Event.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(ObjectTool.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Script.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Template.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Trap.class).getLastConfigItem().addRevision(r);
    }
}
