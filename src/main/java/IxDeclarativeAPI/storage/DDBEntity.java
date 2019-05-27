package IxDeclarativeAPI.storage;

import IxDeclarativeAPI.storage.attribute.Attribute;
import IxDeclarativeAPI.storage.attribute.Metadata;
import IxDeclarativeAPI.storage.attribute.Type;
import IxDeclarativeAPI.storage.attribute.Value;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public abstract class DDBEntity implements Entity {
    @Setter private Attribute primaryKey;
    @Setter private Attribute sortKey;
    @Setter private final Map<String, Attribute> payload = new HashMap<>();

    public abstract Schema getSchema();
    public abstract AmazonDynamoDB getDDB();

    /* CRUD operations */
    @Override
    public void create() {

    }

    @Override
    public void read() {
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put(this.primaryKey.getName(), new AttributeValue(this.primaryKey.getStringValue()));

        if (this.sortKey != null)
            key.put(this.sortKey.getName(), new AttributeValue(this.sortKey.getStringValue()));

        final GetItemRequest request = new GetItemRequest()
                .withKey(key)
                .withTableName(this.getSchema().getTableName());

        final Map<String, AttributeValue> response = this.getDDB().getItem(request).getItem();

        for (final Map.Entry<String, Type> attributeTypeEntry: this.getSchema().getAttributeTypeMap().entrySet()) {
            if (attributeTypeEntry.getValue() != Type.REGULAR) continue;

            final String attributeName = attributeTypeEntry.getKey();

            final AttributeValue value = response.get(attributeName);
            final Attribute attribute = new Attribute(attributeName,
                                                new Value(value.getS()),
                                                new Metadata(Type.REGULAR));
            this.payload.put(attributeName, attribute);
        }
    }

    @Override
    public void update(final List<Attribute> updatedAttributes) {

    }

    @Override
    public void delete() {

    }

    /* List operations */
    @Override
    public List<? extends DDBEntity> getAll() {
        return null;
    }

    /* Utilities */
    @Override
    public void validate() {

    }
}
