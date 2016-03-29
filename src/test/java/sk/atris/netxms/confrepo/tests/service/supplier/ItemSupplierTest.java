package sk.atris.netxms.confrepo.tests.service.supplier;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.exceptions.NoConfigItemsRequestedException;
import sk.atris.netxms.confrepo.model.entities.Revision;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;
import sk.atris.netxms.confrepo.service.supplier.ItemSupplier;
import sk.atris.netxms.confrepo.tests.service.TestEnvironment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ItemSupplierTest {
    private final ItemSupplier itemSupplier = ItemSupplier.getInstance();

    private final Revision revision = new Revision("<test></test>", "Test revision.");

    @Before
    public void environmentSetup() {
        TestEnvironment.setup(revision);
    }

    @After
    public void environmentCleaup() {
        TestEnvironment.cleanup();
    }

    @Test(expected = NoConfigItemsRequestedException.class)
    public void testNoRequestedItems() throws JDOMException, NoConfigItemsRequestedException, ConfigItemNotFoundException, IOException {
        itemSupplier.getItemsXml(new ArrayList<RequestedConfigItem>());
    }

    @Test
    public void testFullGeneratedXml() throws JDOMException, NoConfigItemsRequestedException, ConfigItemNotFoundException, IOException {
        List<RequestedConfigItem> requestedItems = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getCompactFormat());

        requestedItems.add(new RequestedConfigItem("guid1", ""));
        requestedItems.add(new RequestedConfigItem("guid2", ""));
        requestedItems.add(new RequestedConfigItem("guid3", ""));
        requestedItems.add(new RequestedConfigItem("guid4", ""));
        requestedItems.add(new RequestedConfigItem("guid5", ""));
        requestedItems.add(new RequestedConfigItem("guid6", ""));
        requestedItems.add(new RequestedConfigItem("guid7", ""));

        String rawGeneratedXmlString = itemSupplier.getItemsXml(requestedItems);

        Document generatedXmlDocument = saxBuilder.build(new StringReader(rawGeneratedXmlString));
        Document validationXmlDocument = saxBuilder.build(new FileInputStream("src/test/resources/generated_config.xml"));

        String generatedXmlString = xmlOutputter.outputString(validationXmlDocument);
        String validationXmlString = xmlOutputter.outputString(generatedXmlDocument);

        assert validationXmlString.equals(generatedXmlString);
    }
}
