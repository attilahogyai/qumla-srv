package io.katharsis.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.katharsis.jackson.exception.JsonSerializationException;
import io.katharsis.queryParams.params.IncludedFieldsParams;
import io.katharsis.queryParams.params.TypedParams;
import io.katharsis.request.dto.Attributes;
import io.katharsis.resource.field.ResourceField;
import io.katharsis.resource.information.ResourceInformation;
import io.katharsis.resource.registry.RegistryEntry;
import io.katharsis.resource.registry.ResourceRegistry;
import io.katharsis.response.Container;
import io.katharsis.response.DataLinksContainer;
import io.katharsis.utils.BeanUtils;
import io.katharsis.utils.PropertyUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class serializes an single resource which can be included in <i>data</i> field of JSON API response.
 *
 * @see Container
 */
public class ContainerSerializer extends JsonSerializer<Container> {

    private static final String TYPE_FIELD_NAME = "type";
    private static final String ID_FIELD_NAME = "id";
    private static final String ATTRIBUTES_FIELD_NAME = "attributes";
    private static final String RELATIONSHIPS_FIELD_NAME = "relationships";
    private static final String LINKS_FIELD_NAME = "links";
    private static final String SELF_FIELD_NAME = "self";

    private final ResourceRegistry resourceRegistry;

    public ContainerSerializer(ResourceRegistry resourceRegistry) {
        this.resourceRegistry = resourceRegistry;
    }

    @Override
    public void serialize(Container value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (value != null && value.getData() != null) {
            gen.writeStartObject();

            TypedParams<IncludedFieldsParams> includedFields = value.getResponse()
                .getQueryParams()
                .getIncludedFields();

            writeData(gen, value.getData(), includedFields);
            gen.writeEndObject();
        } else {
            gen.writeObject(null);
        }
    }

    /**
     * Writes a value. Each serialized container must contain type field whose value is string
     * <a href="http://jsonapi.org/format/#document-structure-resource-types"></a>.
     */
    private void writeData(JsonGenerator gen, Object data, TypedParams<IncludedFieldsParams> includedFields) throws IOException {
        Class<?> dataClass = data.getClass();
        String resourceType = resourceRegistry.getResourceType(dataClass);

        gen.writeStringField(TYPE_FIELD_NAME, resourceType);

        RegistryEntry entry = resourceRegistry.getEntry(dataClass);
        ResourceInformation resourceInformation = entry.getResourceInformation();
        try {
            writeId(gen, data, resourceInformation.getIdField());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new JsonSerializationException(
                "Error writing id field: " + resourceInformation.getIdField().getUnderlyingName());
        }

        try {
            writeAttributes(gen, data, resourceInformation.getAttributeFields(), includedFields);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            StringBuilder attributeFieldNames = new StringBuilder();
            for (ResourceField attributeField : resourceInformation.getAttributeFields()) {
                attributeFieldNames.append(attributeField.getUnderlyingName());
                attributeFieldNames.append(" ");
            }
            throw new JsonSerializationException("Error writing basic fields: " +
                attributeFieldNames);
        }

        Set<ResourceField> relationshipFields = getRelationshipFields(resourceType, resourceInformation, includedFields);
        writeRelationshipFields(gen, data, relationshipFields);
        //writeLinksField(gen, data);
    }

    private Set<ResourceField> getRelationshipFields(String resourceType, ResourceInformation resourceInformation, TypedParams<IncludedFieldsParams> includedFields) {
        Set<ResourceField> relationshipFields = new HashSet<>();
        for (ResourceField resourceField : resourceInformation.getRelationshipFields()) {
            if (isIncluded(resourceType, includedFields, resourceField)) {
                relationshipFields.add(resourceField);
            }
        }

        return relationshipFields;
    }

    /**
     * The id MUST be written as a string
     * <a href="http://jsonapi.org/format/#document-structure-resource-ids">Resource IDs</a>.
     */
    private void writeId(JsonGenerator gen, Object data, ResourceField idField)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        String sourceId = BeanUtils.getProperty(data, idField.getUnderlyingName());
        gen.writeObjectField(ID_FIELD_NAME, sourceId);
    }

    private void writeAttributes(JsonGenerator gen, Object data, Set<ResourceField> attributeFields,
                                 TypedParams<IncludedFieldsParams> includedFields)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {

        String resourceType = resourceRegistry.getResourceType(data.getClass());

        Attributes attributesObject = new Attributes();
        for (ResourceField attributeField : attributeFields) {
            if (isIncluded(resourceType, includedFields, attributeField)) {
                Object basicFieldValue = PropertyUtils.getProperty(data, attributeField.getUnderlyingName());
                attributesObject.addAttribute(attributeField.getJsonName(), basicFieldValue);
            }
        }

        gen.writeObjectField(ATTRIBUTES_FIELD_NAME, attributesObject);
    }

    private boolean isIncluded(String resourceType, TypedParams<IncludedFieldsParams> includedFields, ResourceField attributeField) {
        IncludedFieldsParams typeIncludedFields = findIncludedFields(includedFields, resourceType);
        if (typeIncludedFields == null || typeIncludedFields.getParams().isEmpty()) {
            return includedFields == null || includedFields.getParams().isEmpty();
        } else {
            return typeIncludedFields.getParams().contains(attributeField.getJsonName());
        }
    }

    private IncludedFieldsParams findIncludedFields(TypedParams<IncludedFieldsParams> includedFields, String
        elementName) {
        IncludedFieldsParams includedFieldsParams = null;
        if (includedFields != null) {
            for (Map.Entry<String, IncludedFieldsParams> entry : includedFields.getParams()
                .entrySet()) {
                if (elementName.equals(entry.getKey())) {
                    includedFieldsParams = entry.getValue();
                }
            }
        }
        return includedFieldsParams;
    }

    private void writeRelationshipFields(JsonGenerator gen, Object data, Set<ResourceField> relationshipFields)
        throws IOException {
        DataLinksContainer dataLinksContainer = new DataLinksContainer(data, relationshipFields);
        gen.writeObjectField(RELATIONSHIPS_FIELD_NAME, dataLinksContainer);
    }

    private void writeLinksField(JsonGenerator gen, Object data) throws IOException {
        gen.writeFieldName(LINKS_FIELD_NAME);
        gen.writeStartObject();
        writeSelfLink(gen, data);
        gen.writeEndObject();
    }

    private void writeSelfLink(JsonGenerator gen, Object data) throws IOException {
        Class<?> sourceClass = data.getClass();
        String resourceUrl = resourceRegistry.getResourceUrl(sourceClass);
        RegistryEntry entry = resourceRegistry.getEntry(sourceClass);
        ResourceField idField = entry.getResourceInformation().getIdField();

        Object sourceId = PropertyUtils.getProperty(data, idField.getUnderlyingName());
        gen.writeStringField(SELF_FIELD_NAME, resourceUrl + "/" + sourceId);
    }

    public Class<Container> handledType() {
        return Container.class;
    }
}
