package sk.atris.netxms.confrepo.service.supplier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import sk.atris.netxms.confrepo.enums.NetxmsConfigSections;
import sk.atris.netxms.confrepo.enums.NetxmsGeneratedConfigXml;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.exceptions.NoConfigItemsRequestedException;
import sk.atris.netxms.confrepo.exceptions.RevisionNotFoundException;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemSupplier {
    @Getter
    private static final ItemSupplier instance = new ItemSupplier();

    private final NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

    private final SAXBuilder saxBuilder = new SAXBuilder();
    private final XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    public <T extends ConfigItem> String getItemsXml(List<RequestedConfigItem> requestedItems) throws ConfigItemNotFoundException, JDOMException, IOException, NoConfigItemsRequestedException, RevisionNotFoundException {
        log.debug("Starting to prepare requested configuration XML.");

        if (requestedItems.size() == 0) {
            log.warn("No requested items were present in a configuration XML request!");
            throw new NoConfigItemsRequestedException("No config items were requested.");
        }

        Document xmlDoc = new Document();
        seedConfigXml(xmlDoc);

        for (RequestedConfigItem requestedItem : requestedItems) {
            T foundRequestedItem = netxmsConfigRepository.getConfigItemByGuid(requestedItem.getGuid());

            addConfigItemToXml(xmlDoc, foundRequestedItem, requestedItem.getRequestedRevisionVersion());
        }

        log.debug("Finished preparing requested configuration XML.");
        return xmlOutputter.outputString(xmlDoc);
    }

    private void seedConfigXml(Document xmlDoc) {
        log.trace("Seeding the XML Document with NetXMS configuration elements.");

        Element root = new Element(NetxmsGeneratedConfigXml.ROOT_ELEMENT_NAME.toString());
        xmlDoc.addContent(root);

        Element formatVersion = new Element(NetxmsGeneratedConfigXml.FORMAT_VERSION_NAME.toString());
        formatVersion.setText(NetxmsGeneratedConfigXml.FORMAT_VERSION_VALUE.toString());
        root.addContent(formatVersion);

        Element description = new Element(NetxmsGeneratedConfigXml.DESCRIPTION_NAME.toString());
        description.setText(NetxmsGeneratedConfigXml.DESCRIPTION_VALUE.toString());
        root.addContent(description);

        Element eventsSection = new Element(NetxmsConfigSections.EVENTS.toString());
        Element templatesSection = new Element(NetxmsConfigSections.TEMPLATES.toString());
        Element trapsSection = new Element(NetxmsConfigSections.TRAPS.toString());
        Element rulesSection = new Element(NetxmsConfigSections.RULES.toString());
        Element scriptsSection = new Element(NetxmsConfigSections.SCRIPTS.toString());
        Element objectToolsSection = new Element(NetxmsConfigSections.OBJECT_TOOLS.toString());
        Element dciSummaryTablesSection = new Element(NetxmsConfigSections.DCI_SUMMARY_TABLES.toString());

        root.addContent(eventsSection);
        root.addContent(templatesSection);
        root.addContent(trapsSection);
        root.addContent(rulesSection);
        root.addContent(scriptsSection);
        root.addContent(objectToolsSection);
        root.addContent(dciSummaryTablesSection);

        log.trace("Finished seeding the XML Document with NetXMS configuration elements.");
    }

    private <T extends ConfigItem> void addConfigItemToXml(Document xmlDoc, T item, int requestedRevisionVersion) throws JDOMException, IOException, RevisionNotFoundException {
        Revision r = item.getRevision(requestedRevisionVersion);

        log.trace("Building an Element object from the requested revision's XML string.");
        Document itemXml = saxBuilder.build(new StringReader(r.getXmlCode()));
        Element itemElement = itemXml.getRootElement().detach();

        Element root = xmlDoc.getRootElement();

        Element configSection = null;

        log.trace("Finding the actual class of config item '{}'.", item.getGuid());
        if (item instanceof DciSummaryTable)
            configSection = root.getChild(NetxmsConfigSections.DCI_SUMMARY_TABLES.toString());
        if (item instanceof EppRule)
            configSection = root.getChild(NetxmsConfigSections.RULES.toString());
        if (item instanceof Event)
            configSection = root.getChild(NetxmsConfigSections.EVENTS.toString());
        if (item instanceof ObjectTool)
            configSection = root.getChild(NetxmsConfigSections.OBJECT_TOOLS.toString());
        if (item instanceof Script)
            configSection = root.getChild(NetxmsConfigSections.SCRIPTS.toString());
        if (item instanceof Template)
            configSection = root.getChild(NetxmsConfigSections.TEMPLATES.toString());
        if (item instanceof Trap)
            configSection = root.getChild(NetxmsConfigSections.TRAPS.toString());

        if (configSection == null) {
            log.error("Attempted to add a subclass of ConfigItem that is not supported!");
            throw new RuntimeException("A subclass of ConfigItem that is not supported was passed to addConfigItemToXml()!");
        }

        log.trace("Adding config item '{}' to the requested configuration XML.", item.getGuid());
        configSection.addContent(itemElement);
    }
}
