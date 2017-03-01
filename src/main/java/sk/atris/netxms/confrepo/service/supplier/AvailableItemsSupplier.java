package sk.atris.netxms.confrepo.service.supplier;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.enums.NetxmsConfigSections;
import sk.atris.netxms.confrepo.model.entities.configItem.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AvailableItemsSupplier {
    @Getter
    private static final AvailableItemsSupplier instance = new AvailableItemsSupplier();

    private final JsonNodeFactory jsonFactory = JsonNodeFactory.instance;

    public String getAllAvailableItemsJson(NetxmsConfigRepository netxmsConfig) {
        ObjectNode allItemsJson = jsonFactory.objectNode();

        log.debug("Starting to prepare all available items JSON.");

        for (DciSummaryTable item : netxmsConfig.getRepository(DciSummaryTable.class).getShallowCopy()) {
            ArrayNode thisItemsRevisions = getItemRevisionsForJson(item);

            ObjectNode thisItemJson = jsonFactory.objectNode();
            thisItemJson.put("title", item.getTitle());
            thisItemJson.set("instances", thisItemsRevisions);

            allItemsJson.with(NetxmsConfigSections.DCI_SUMMARY_TABLES.toString()).set(item.getGuid(), thisItemJson);
        }

        for (EppRule item : netxmsConfig.getRepository(EppRule.class).getShallowCopy()) {
            ArrayNode thisItemsRevisions = getItemRevisionsForJson(item);

            ObjectNode thisItemJson = jsonFactory.objectNode();
            thisItemJson.put("comments", item.getComment());
            thisItemJson.set("instances", thisItemsRevisions);

            allItemsJson.with(NetxmsConfigSections.RULES.toString()).set(item.getGuid(), thisItemJson);
        }

        for (Event item : netxmsConfig.getRepository(Event.class).getShallowCopy()) {
            ArrayNode thisItemsRevisions = getItemRevisionsForJson(item);

            ObjectNode thisItemJson = jsonFactory.objectNode();
            thisItemJson.put("name", item.getName());
            thisItemJson.set("instances", thisItemsRevisions);

            allItemsJson.with(NetxmsConfigSections.EVENTS.toString()).set(item.getGuid(), thisItemJson);
        }

        for (ObjectTool item : netxmsConfig.getRepository(ObjectTool.class).getShallowCopy()) {
            ArrayNode thisItemsRevisions = getItemRevisionsForJson(item);

            ObjectNode thisItemJson = jsonFactory.objectNode();
            thisItemJson.put("name", item.getName());
            thisItemJson.set("instances", thisItemsRevisions);

            allItemsJson.with(NetxmsConfigSections.OBJECT_TOOLS.toString()).set(item.getGuid(), thisItemJson);
        }

        for (Script item : netxmsConfig.getRepository(Script.class).getShallowCopy()) {
            ArrayNode thisItemsRevisions = getItemRevisionsForJson(item);

            ObjectNode thisItemJson = jsonFactory.objectNode();
            thisItemJson.put("name", item.getName());
            thisItemJson.set("instances", thisItemsRevisions);

            allItemsJson.with(NetxmsConfigSections.SCRIPTS.toString()).set(item.getGuid(), thisItemJson);
        }

        for (Template item : netxmsConfig.getRepository(Template.class).getShallowCopy()) {
            ArrayNode thisItemsRevisions = getItemRevisionsForJson(item);

            ObjectNode thisItemJson = jsonFactory.objectNode();
            thisItemJson.put("name", item.getName());
            thisItemJson.set("instances", thisItemsRevisions);

            allItemsJson.with(NetxmsConfigSections.TEMPLATES.toString()).set(item.getGuid(), thisItemJson);
        }

        for (Trap item : netxmsConfig.getRepository(Trap.class).getShallowCopy()) {
            ArrayNode thisItemsRevisions = getItemRevisionsForJson(item);

            ObjectNode thisItemJson = jsonFactory.objectNode();
            thisItemJson.put("description", item.getDescription());
            thisItemJson.set("instances", thisItemsRevisions);

            allItemsJson.with(NetxmsConfigSections.TRAPS.toString()).set(item.getGuid(), thisItemJson);
        }

        log.debug("Finished preparing all available items JSON.");
        return allItemsJson.toString();
    }

    private <T extends ConfigItem> ArrayNode getItemRevisionsForJson(T item) {
        List<ItemRevision> revisions = item.getRevisionsShallowCopy();
        ArrayNode revisionsJson = jsonFactory.arrayNode();

        // loop over revisions from back to front to return them from newest to oldest
        for (int i = item.getRevisionCount() - 1; i >= 0; i--) {
            ItemRevision r = revisions.get(i);

            ObjectNode revision = jsonFactory.objectNode();

            revision.put("version", r.getVersion());
            revision.put("timestamp", r.getTimestamp());
            revision.put("comment", r.getMessage());

            revisionsJson.add(revision);
        }

        return revisionsJson;
    }
}
