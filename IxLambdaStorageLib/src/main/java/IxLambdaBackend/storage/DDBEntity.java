package IxLambdaBackend.storage;

import IxLambdaBackend.storage.exception.EntityAlreadyExistsException;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.storage.exception.InvalidInputException;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.Metadata;
import IxLambdaBackend.storage.attribute.value.NumberValue;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.value.Value;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.ddb.DDBCreateStrategy;
import IxLambdaBackend.storage.ddb.DDBDeleteStrategy;
import IxLambdaBackend.storage.ddb.DDBReadStrategy;
import IxLambdaBackend.storage.ddb.DDBUpdateStrategy;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DDBEntity <T extends DDBEntity<T>> implements Entity <T> {
    @Setter private Attribute primaryKey;
    @Setter private Attribute sortKey;

    private Schema schema;
    @Setter @Getter private Map<String, Attribute> payload = new HashMap<>();

    public abstract Schema createSchema();
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
    public T create() throws EntityAlreadyExistsException, InternalException {
        DDBCreateStrategy.execute(this.primaryKey, this.sortKey, this.payload, this.getSchema(), this.getDDB());
        return (T) this;
    }

    @Override
    public T read() throws EntityNotFoundException, InternalException {
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

        return (T) this;
    }

    @Override
    public T update() throws EntityNotFoundException, InternalException {
        DDBUpdateStrategy.execute(this.primaryKey, this.sortKey, this.payload, this.getSchema(), this.getDDB());
        return (T) this;
    }

    @Override
    public void delete() throws InternalException {
        DDBDeleteStrategy.execute(this.primaryKey, this.sortKey, this.getSchema().getTableName(), this.getDDB());
    }

    /* List operations */
    @Override
    public List<T> getAll() {
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

    public Schema getSchema() {
        if (this.schema == null) this.schema = createSchema();

        return this.schema;
    }

    public void setAttributeValue(final String attributeName, final Attribute attribute) {
        if (this.primaryKey.getName().equals(attributeName))
            this.primaryKey = attribute;
        else if (this.sortKey != null && this.sortKey.getName().equals(attributeName))
            this.sortKey = attribute;
        else if (this.schema.getAttributeTypesMap().containsKey(attributeName))
            this.payload.put(attributeName, attribute);
        else throw new InvalidInputException("Attribute not in schema: " + attributeName);
    }

    public Value getAttribute(final String attributeName) {
        if (this.primaryKey.getName().equals(attributeName))
            return this.primaryKey.getValue();
        else if (this.sortKey != null && this.sortKey.getName().equals(attributeName))
            return this.sortKey.getValue();
        else if (this.schema.getAttributeTypesMap().containsKey(attributeName))
            return this.payload.get(attributeName).getValue();
        else throw new InvalidInputException("Attribute does not exist: " + attributeName);
    }

    public void clear() {
        this.payload = new HashMap<>();
    }
}
