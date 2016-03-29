package sk.atris.netxms.confrepo.tests.service.parser;

import org.junit.Test;
import sk.atris.netxms.confrepo.exceptions.ItemRequestJsonParserException;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;
import sk.atris.netxms.confrepo.service.parser.ItemRequestJsonParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ItemRequestJsonParserTest {
    private final ItemRequestJsonParser itemRequestJsonParser = ItemRequestJsonParser.getInstance();

    @Test(expected = ItemRequestJsonParserException.class)
    public void testEmptyRequestJson() throws IOException, ItemRequestJsonParserException {
        String inputJson = "{}";

        InputStream inputStream = new ByteArrayInputStream(inputJson.getBytes());

        itemRequestJsonParser.parse(inputStream);
    }

    @Test(expected = ItemRequestJsonParserException.class)
    public void testInvalidRequestJson1() throws IOException, ItemRequestJsonParserException {
        String inputJson = "{\"invalid\": {}, \"invalid_again\": \"\"}";

        InputStream inputStream = new ByteArrayInputStream(inputJson.getBytes());

        itemRequestJsonParser.parse(inputStream);
    }

    @Test(expected = ItemRequestJsonParserException.class)
    public void testInvalidRequestJson2() throws IOException, ItemRequestJsonParserException {
        String inputJson = "{\"get-items\": {}}";

        InputStream inputStream = new ByteArrayInputStream(inputJson.getBytes());

        itemRequestJsonParser.parse(inputStream);
    }

    @Test(expected = ItemRequestJsonParserException.class)
    public void testInvalidRequestJson3() throws IOException, ItemRequestJsonParserException {
        String inputJson = "{\"get-items\": [{\"invalid\":\"\"}]}";

        InputStream inputStream = new ByteArrayInputStream(inputJson.getBytes());

        itemRequestJsonParser.parse(inputStream);
    }

    @Test
    public void testValidRequestSingle() throws IOException, ItemRequestJsonParserException {
        String inputJson = "{\"get-items\": [{\"guid\":\"\", \"version\":\"\"}]}";

        InputStream inputStream = new ByteArrayInputStream(inputJson.getBytes());

        List<RequestedConfigItem> requestedConfigItems = itemRequestJsonParser.parse(inputStream);

        assert requestedConfigItems.size() == 1;

        assert requestedConfigItems.get(0).getGuid().equals("");
        assert requestedConfigItems.get(0).getRevisionVersion().equals("");
    }

    @Test
    public void testValidRequestMultiple() throws IOException, ItemRequestJsonParserException {
        String inputJson = "{\"get-items\": [{\"guid\":\"1\", \"version\":\"1\"},{\"guid\":\"2\", \"version\":\"2\"}]}";

        InputStream inputStream = new ByteArrayInputStream(inputJson.getBytes());

        List<RequestedConfigItem> requestedConfigItems = itemRequestJsonParser.parse(inputStream);

        assert requestedConfigItems.size() == 2;

        assert requestedConfigItems.get(0).getGuid().equals("1");
        assert requestedConfigItems.get(0).getRevisionVersion().equals("1");

        assert requestedConfigItems.get(1).getGuid().equals("2");
        assert requestedConfigItems.get(1).getRevisionVersion().equals("2");
    }
}
