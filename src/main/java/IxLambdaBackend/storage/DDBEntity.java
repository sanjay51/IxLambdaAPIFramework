package IxLambdaBackend.storage;

import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.Metadata;
import IxLambdaBackend.storage.attribute.Type;
import IxLambdaBackend.storage.attribute.Value;
import IxLambdaBackend.storage.ddb.DDBReadStrategy;
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

    public DDBEntity(final String primaryKeyValue) {
        this.primaryKey = new Attribute(this.getSchema().getPrimaryKeyName(),
                                        new Value<>(primaryKeyValue),
                                        new Metadata(Type.PRIMARY_KEY));

    }

    public DDBEntity(final String primaryKeyValue, final String sortKeyValue) {
        this(primaryKeyValue);

        if (this.getSchema().getSortKeyName().isPresent())
            this.sortKey = new Attribute(this.getSchema().getSortKeyName().get(),
                                                 new Value<>(sortKeyValue),
                                                 new Metadata(Type.SORT_KEY));
    }

    /* CRUD operations */
    @Override
    public DDBEntity create() {
        return this;
    }

    @Override
    public DDBEntity read() throws EntityNotFoundException, InternalException {
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

        return this;
    }

    @Override
    public DDBEntity update(final List<Attribute> updatedAttributes) {
        return this;
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

    public Map<String, Object> getAsKeyValueObject() {
        final Map<String, Object> entity = new HashMap<>();
        entity.put(this.primaryKey.getName(), this.primaryKey.getValue().getValue());
        if (this.sortKey != null) entity.put(this.sortKey.getName(), this.sortKey.getValue().getValue());

        for (final Map.Entry<String, Attribute> attribute: this.payload.entrySet()) {
            entity.put(attribute.getKey(), attribute.getValue().getValue().getValue());
        }

        return entity;
    }
}
