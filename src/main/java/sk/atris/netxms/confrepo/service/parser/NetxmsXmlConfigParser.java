package sk.atris.netxms.confrepo.service.parser;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import sk.atris.netxms.confrepo.enums.NetxmsConfigSections;
import sk.atris.netxms.confrepo.exceptions.NetxmsXmlConfigParserException;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetxmsXmlConfigParser {
    @Getter
    private final static NetxmsXmlConfigParser instance = new NetxmsXmlConfigParser();

    private final SAXBuilder saxBuilder = new SAXBuilder();
    private final XMLOutputter xmlOutputter = new XMLOutputter();

    /**
     * Parse the supplied InputStream for NetXMS XML configuration.
     *
     * @param receiveStream InputStream on which the NetXMS configuration XML is received
     * @return {@link NetxmsConfig} object
     * @throws NetxmsXmlConfigParserException
     */
    public NetxmsConfig parse(InputStream receiveStream) throws NetxmsXmlConfigParserException {
        Document xmlDoc;

        log.trace("Starting to parse a received InputStream.");

        try {
            xmlDoc = saxBuilder.build(receiveStream);
        } catch (JDOMException | IOException e) {
            log.warn("Parsing xml failed, exception '{}', error '{}'!", e.getClass().getSimpleName(), e.getMessage());
            throw new NetxmsXmlConfigParserException(e);
        }

        Element root = xmlDoc.getRootElement();
        List<Element> elements = root.getChildren();

        // set the revision message
        String revisionMessage = root.getChildText("description");

        return extractConfiguration(elements, revisionMessage);
    }

    private NetxmsConfig extractConfiguration(List<Element> elements, String revisionMessage) throws NetxmsXmlConfigParserException {
        NetxmsConfig receivedNetxmsConfig = new NetxmsConfig();

        log.debug("Starting to extract configuration from the received XML.");
        log.trace("Started building a new ReceivedNetxmsConfig object.");

        // loop through all elements inside the "configuration" element
        for (Element element : elements) {
            // get all children of this configuration element
            List<Element> cfgItemsXml = element.getChildren();

            // if this element doesn't have children, we are not interested (configuration sections have children elements)
            if (cfgItemsXml.size() == 0) {
                log.trace("Skipping element '{}' of the received XML since it has no children.", element.getName());
                continue;
            }

            // this holds the current config section we are in in enum form
            NetxmsConfigSections currentConfigSection = null;

            // this holds the current config section we are in in String form
            String currentSection = element.getName();

            log.trace("Iterating over the '{}' element of the configuration section of the XML.", currentSection);

            // find out which of the supported configuration sections we are in
            for (NetxmsConfigSections ncs : NetxmsConfigSections.values()) {
                if (ncs.toString().equals(currentSection))
                    currentConfigSection = ncs;
            }

            // if we are in an unsupported section, simply continue parsing
            if (currentConfigSection == null) {
                log.warn("Skipping an unsupported element '{}' of the configuration section of the XML.", currentSection);
                continue;
            }

            // parse the configuration elements we are interested in
            for (Element cfgItemXml : cfgItemsXml) {
                ConfigItem newConfigItem;
                String newConfigItemGuid = cfgItemXml.getChildText("guid");

                if (newConfigItemGuid == null) {
                    log.warn("A config item without a GUID was present in a received NetXMS configuration XML!");
                    throw new NetxmsXmlConfigParserException("A config item without a GUID was present in the provided XML!");
                }

                switch (currentConfigSection) {
                    case EVENTS:
                        // build an Event object
                        newConfigItem = new Event(newConfigItemGuid, cfgItemXml.getChildText("name"));
                        break;
                    case TEMPLATES:
                       // build a Template object
                        newConfigItem = new Template(newConfigItemGuid, cfgItemXml.getChildText("name"));
                        break;
                    case TRAPS:
                       // build a Trap object
                        newConfigItem = new Trap(newConfigItemGuid, cfgItemXml.getChildText("description"));
                        break;
                    case RULES:
                        // build an EppRule object
                        newConfigItem = new EppRule(newConfigItemGuid, cfgItemXml.getChildText("comments"));
                        break;
                    case SCRIPTS:
                        // build a Script object
                        newConfigItem = new Script(newConfigItemGuid, cfgItemXml.getChildText("name"));
                        break;
                    case OBJECT_TOOLS:
                        // build an ObjectTool object
                        newConfigItem = new ObjectTool(newConfigItemGuid, cfgItemXml.getChildText("name"));
                        break;
                    case DCI_SUMMARY_TABLES:
                        // build a DciSummaryTable object
                        newConfigItem = new DciSummaryTable(newConfigItemGuid, cfgItemXml.getChildText("title"));
                        break;
                    default:
                        log.error("Internal application error, please see the stack trace!");
                        throw new RuntimeException("This switch statement should never reach this point!");
                }

                // add the received revision to the newConfigItem
                addRevision(newConfigItem, cfgItemXml, revisionMessage);

                // add the item to the receivedNetxmsConfig
                addItemToReceivedNetxmsConfig(receivedNetxmsConfig, newConfigItem);
            }
        }

        log.trace("Finished building a new ReceivedNetxmsConfig object.");
        log.debug("Finished extracting configuration from the received XML.");
        return receivedNetxmsConfig;
    }

    private <T extends ConfigItem> void addRevision(T item, Element xmlElement, String revisionMessage) {
        Revision revision = new Revision(xmlOutputter.outputString(xmlElement), revisionMessage, item.getNextFreeRevisionVersion());
        item.addRevision(revision);
    }

    private <T extends ConfigItem> void addItemToReceivedNetxmsConfig(NetxmsConfig config, T item) {
        log.trace("Adding a new '{}' from the received XML to the '{}' object.", item.getClass().getSimpleName(), config.getClass().getSimpleName());

        config.addItem(item);
    }
}
