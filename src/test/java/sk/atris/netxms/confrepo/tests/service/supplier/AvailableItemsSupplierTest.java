package sk.atris.netxms.confrepo.tests.service.supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.NetxmsXmlConfigParserException;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfig;
import sk.atris.netxms.confrepo.service.parser.NetxmsXmlConfigParser;
import sk.atris.netxms.confrepo.service.supplier.AvailableItemsSupplier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AvailableItemsSupplierTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Test
    public void testAllAvailableItemsJson1() throws IOException, NetxmsXmlConfigParserException {
        InputStream fis1 = new FileInputStream("src/test/resources/full_config.xml");
        NetxmsConfig netxmsConfig = NetxmsXmlConfigParser.getInstance().parse(fis1);
        fis1.close();

        String generatedJsonString = AvailableItemsSupplier.getInstance().getAllAvailableItemsJson(netxmsConfig);
        JsonNode fromGeneratedString = mapper.readTree(generatedJsonString);

        InputStream fis2 = new FileInputStream("src/test/resources/allConfigItems1.json");
        JsonNode fromValidationFile = mapper.readTree(fis2);
        fis2.close();

        assert fromGeneratedString.toString().equals(fromValidationFile.toString());
    }

    @Test
    public void testAllAvailableItemsJson2() throws IOException, NetxmsXmlConfigParserException {
        InputStream fis1 = new FileInputStream("src/test/resources/full_config.xml");
        NetxmsConfig netxmsConfig = NetxmsXmlConfigParser.getInstance().parse(fis1);
        fis1.close();

        Revision r = new Revision(null, "a newer revision");

        netxmsConfig.getRepository(DciSummaryTable.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(EppRule.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Event.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(ObjectTool.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Script.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Template.class).getLastConfigItem().addRevision(r);
        netxmsConfig.getRepository(Trap.class).getLastConfigItem().addRevision(r);

        String generatedJsonString = AvailableItemsSupplier.getInstance().getAllAvailableItemsJson(netxmsConfig);
        JsonNode fromGeneratedString = mapper.readTree(generatedJsonString);

        InputStream fis2 = new FileInputStream("src/test/resources/allConfigItems2.json");
        JsonNode fromValidationFile = mapper.readTree(fis2);
        fis2.close();

        assert fromGeneratedString.toString().equals(fromValidationFile.toString());
    }
}
