package IxDeclarativeAPI.storage;

import IxDeclarativeAPI.exception.EntityNotFoundException;
import IxDeclarativeAPI.exception.InternalException;
import IxDeclarativeAPI.storage.attribute.Attribute;
import IxDeclarativeAPI.storage.attribute.Metadata;
import IxDeclarativeAPI.storage.attribute.Type;
import IxDeclarativeAPI.storage.attribute.Value;
import IxDeclarativeAPI.storage.ddb.DDBReadStrategy;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public abstract class DDBEntity implements Entity {
    @Setter private Attribute primaryKey;
    @Setter private Attribute sortKey;
    @Setter @Getter private Map<String, Attribute> payload = new HashMap<>();

    public abstract Schema getSchema();
    public abstract AmazonDynamoDB getDDB();

    /* CRUD operations */
    @Override
    public void create() {

    }

    @Override
    public void read() throws EntityNotFoundException, InternalException {
        // read
        final Map<String, AttributeValue> response = DDBReadStrategy.execute(this.primaryKey,
                this.sortKey, this.getSchema().getTableName(), this.getDDB());

        // populate
        for (final Map.Entry<String, Type> attributeTypeEntry: this.getSchema().getAttributeTypeMap().entrySet()) {
            if (attributeTypeEntry.getValue() != Type.REGULAR) continue;

            final String attributeName = attributeTypeEntry.getKey();

            final AttributeValue value = response.get(attributeName);
            final Attribute attribute = new Attribute(attributeName, new Value(value.getS()), new Metadata(Type.REGULAR));
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
