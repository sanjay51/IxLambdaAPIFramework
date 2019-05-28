package IxLambdaBackend.storage;

import IxLambdaBackend.exception.EntityAlreadyExistException;
import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.Metadata;
import IxLambdaBackend.storage.attribute.value.NumberValue;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.value.Value;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.ddb.DDBCreateStrategy;
import IxLambdaBackend.storage.ddb.DDBReadStrategy;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
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

    /* Constructors */
    public DDBEntity(final String primaryKeyValue) {
        this.primaryKey = new Attribute(this.getSchema().getPrimaryKeyName(),
                new StringValue(primaryKeyValue),
                new Metadata(AttributeType.PRIMARY_KEY));

    }

    public DDBEntity(final double primaryKeyValue) {
        this.primaryKey = new Attribute(this.getSchema().getPrimaryKeyName(),
                new NumberValue(primaryKeyValue),
                new Metadata(AttributeType.PRIMARY_KEY));

    }

    public DDBEntity(final String primaryKeyValue, final String sortKeyValue) {
        this(primaryKeyValue);

        if (this.getSchema().getSortKeyName().isPresent())
            this.sortKey = new Attribute(this.getSchema().getSortKeyName().get(),
                                                 new StringValue(sortKeyValue),
                                                 new Metadata(AttributeType.SORT_KEY));
    }

    /* CRUD operations */
    @Override
    public DDBEntity create() throws EntityAlreadyExistException, InternalException {
        DDBCreateStrategy.execute(this.primaryKey, this.sortKey, this.payload, this.getSchema(), this.getDDB());
        return this;
    }

    @Override
    public DDBEntity read() throws EntityNotFoundException, InternalException {
        // read
        final Map<String, AttributeValue> response = DDBReadStrategy.execute(this.primaryKey,
                this.sortKey, this.getSchema().getTableName(), this.getDDB());

        // populate
        for (final Map.Entry<String, Types> attributeTypesEntry: this.getSchema().getAttributeTypesMap().entrySet()) {
            if (attributeTypesEntry.getValue().getAttributeType() != AttributeType.REGULAR) continue;

            final String attributeName = attributeTypesEntry.getKey();
            final ValueType attributeValueType = this.getSchema().getAttributeValueType(attributeName);

            final AttributeValue value = response.get(attributeName);
            final Attribute attribute = new Attribute(attributeName,
                    Value.fromDynamoDBAttributeValue(value, attributeValueType),
                    new Metadata(AttributeType.REGULAR));
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
        entity.put(this.primaryKey.getName(), this.primaryKey.getValue());
        if (this.sortKey != null) entity.put(this.sortKey.getName(), this.sortKey.getValue());

        for (final Map.Entry<String, Attribute> attribute: this.payload.entrySet()) {
            entity.put(attribute.getKey(), attribute.getValue().getValue());
        }

        return entity;
    }
}