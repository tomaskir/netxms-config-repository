package sk.atris.netxms.confrepo.tests.service.parser;

import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.NetxmsXmlConfigParserException;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.ReceivedNetxmsConfig;
import sk.atris.netxms.confrepo.service.parser.NetxmsXmlConfigParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetxmsXmlConfigParserTest {
    private final NetxmsXmlConfigParser netxmsXmlConfigParser = NetxmsXmlConfigParser.getInstance();

    @Test
    public void testBasicParsing() throws IOException, NetxmsXmlConfigParserException {
        ReceivedNetxmsConfig receivedConfig;

        InputStream fis = new FileInputStream("src/test/resources/valid_config.xml");
        receivedConfig = netxmsXmlConfigParser.parse(fis);
        fis.close();

        assert receivedConfig.getRepository(DciSummaryTable.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(EppRule.class).getConfigItemCount() == 2;
        assert receivedConfig.getRepository(Event.class).getConfigItemCount() == 2;
        assert receivedConfig.getRepository(ObjectTool.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(Script.class).getConfigItemCount() == 1;
        assert receivedConfig.getRepository(Template.class).getConfigItemCount() == 1;
        assert receivedConfig.getRepository(Trap.class).getConfigItemCount() == 2;
    }

    @Test
    public void testAllParsing() throws IOException, NetxmsXmlConfigParserException {
        ReceivedNetxmsConfig receivedConfig;

        InputStream fis = new FileInputStream("src/test/resources/full_config.xml");
        receivedConfig = netxmsXmlConfigParser.parse(fis);
        fis.close();

        // dciSummaryTables section parsing
        assert receivedConfig.getRepository(DciSummaryTable.class).getLastConfigItem().getGuid().equals("c0d8fc76-1fee-4a84-9067-c332c1089989");
        assert receivedConfig.getRepository(DciSummaryTable.class).getLastConfigItem().getTitle().equals("Test");

        // rules section parsing
        assert receivedConfig.getRepository(EppRule.class).getLastConfigItem().getGuid().equals("68a629ef-c645-49e5-8a7b-c5e79308080e");
        assert receivedConfig.getRepository(EppRule.class).getLastConfigItem().getComment().equals("Generate alarm when MAC address change detected on interface");

        // events section parsing
        assert receivedConfig.getRepository(Event.class).getLastConfigItem().getGuid().equals("guid-1");
        assert receivedConfig.getRepository(Event.class).getLastConfigItem().getName().equals("SYS_NODE_ADDED");

        // objectTools section parsing
        assert receivedConfig.getRepository(ObjectTool.class).getLastConfigItem().getGuid().equals("1223b7fe-2098-2a4d-8618-67bdb66a907e");
        assert receivedConfig.getRepository(ObjectTool.class).getLastConfigItem().getName().equals("Restart &agent");

        // scripts section parsing
        assert receivedConfig.getRepository(Script.class).getLastConfigItem().getGuid().equals("guid-3");
        assert receivedConfig.getRepository(Script.class).getLastConfigItem().getName().equals("DCI::SampleTransform");

        // templates section parsing
        assert receivedConfig.getRepository(Template.class).getLastConfigItem().getGuid().equals("22a93d86-a72b-47e7-8178-0040548a3a47");
        assert receivedConfig.getRepository(Template.class).getLastConfigItem().getName().equals("Server Performance");

        // traps section parsing
        assert receivedConfig.getRepository(Trap.class).getLastConfigItem().getGuid().equals("guid-2");
        assert receivedConfig.getRepository(Trap.class).getLastConfigItem().getDescription().equals("Generic authenticationFailure trap");
    }

    @Test
    public void testInvalidConfigParsing() throws IOException, NetxmsXmlConfigParserException {
        ReceivedNetxmsConfig receivedConfig;

        InputStream fis = new FileInputStream("src/test/resources/invalid_config.xml");
        receivedConfig = netxmsXmlConfigParser.parse(fis);
        fis.close();

        assert receivedConfig.getRepository(DciSummaryTable.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(EppRule.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(Event.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(ObjectTool.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(Script.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(Template.class).getConfigItemCount() == 0;
        assert receivedConfig.getRepository(Trap.class).getConfigItemCount() == 0;
    }

    @Test(expected = NetxmsXmlConfigParserException.class)
    public void testMissingGuidParsing() throws IOException, NetxmsXmlConfigParserException {
        InputStream fis = new FileInputStream("src/test/resources/missing_guid_config.xml");
        netxmsXmlConfigParser.parse(fis);
        fis.close();
    }

    @Test(expected = NetxmsXmlConfigParserException.class)
    public void testInvalidXmlParsing() throws IOException, NetxmsXmlConfigParserException {
        InputStream fis = new FileInputStream("src/test/resources/empty.xml");
        netxmsXmlConfigParser.parse(fis);
        fis.close();
    }
}
