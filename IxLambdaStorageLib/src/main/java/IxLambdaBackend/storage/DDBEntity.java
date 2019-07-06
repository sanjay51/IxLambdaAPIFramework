package IxLambdaBackend.storage;

import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.IndexType;
import IxLambdaBackend.storage.attribute.value.NumberValue;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.value.Value;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.ddb.*;
import IxLambdaBackend.storage.exception.EntityAlreadyExistsException;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.storage.exception.InvalidInputException;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class DDBEntity <T extends DDBEntity<T>> implements Entity <T> {
    @Setter private Attribute primaryKey;
    @Setter private Attribute sortKey;

    @Setter private Attribute gsiPrimaryKey;
    @Setter private Attribute gsiSortKey;

    private boolean shouldReadFromGSI = false;
    private String globalSecondaryIndexName = "";

    private Schema schema;
    private AmazonDynamoDB ddb;

    @Setter @Getter private Map<String, Attribute> payload = new HashMap<>();

    public abstract Schema createSchema();
    public abstract AmazonDynamoDB createDDBClient();

    /* Constructors */
    public DDBEntity(final String primaryKeyValue) {
        this.primaryKey = new Attribute(this.getSchema().getPrimaryKeyName(),
                new StringValue(primaryKeyValue));

    }

    public DDBEntity(final double primaryKeyValue) {
        this.primaryKey = new Attribute(this.getSchema().getPrimaryKeyName(),
                new NumberValue(primaryKeyValue));

    }

    public DDBEntity(final String primaryKeyValue, final String sortKeyValue) {
        this(primaryKeyValue);

        if (this.getSchema().getSortKeyName().isPresent())
            this.sortKey = new Attribute(this.getSchema().getSortKeyName().get(),
                                                 new StringValue(sortKeyValue));
    }

    /** GSI based constructor **/
    public DDBEntity(final String gsiPrimaryKeyValue, final String gsiSortKeyValue, final String gsiIndexName) {
        this.gsiPrimaryKey = new Attribute(this.getSchema().getGSIPrimaryKeyName().get(),
                new StringValue(gsiPrimaryKeyValue));

        final Optional<String> gsiSortKeyName = this.getSchema().getSortKeyName();
        if (gsiSortKeyName.isPresent())
            this.sortKey = new Attribute(gsiSortKeyName.get(),
                    new StringValue(gsiSortKeyValue));

        this.shouldReadFromGSI = true;
        this.globalSecondaryIndexName = gsiIndexName;
    }

    /* CRUD operations */
    @Override
    public T create() throws EntityAlreadyExistsException, InternalException {
        DDBCreateStrategy.execute(this.primaryKey, this.sortKey, this.payload, this.getSchema(), this.getDDB());
        return (T) this;
    }

    @Override
    public T read() throws EntityNotFoundException, InternalException {
        if (shouldReadFromGSI) {
            readByGSI();
            return (T) this;
        }

        // read
        final Map<String, AttributeValue> response = DDBReadStrategy.execute(this.primaryKey,
                this.sortKey, this.getSchema().getTableName(), this.getDDB());

        // populate
        populate(response);

        return (T) this;
    }

    private T readByGSI() throws EntityNotFoundException, InternalException {
        // read
        final Map<String, AttributeValue> response = DDBGSIReadStrategy.execute(this.gsiPrimaryKey,
                this.gsiSortKey, this.getSchema().getTableName(), this.globalSecondaryIndexName, this.getDDB());

        // populate
        populate(response);

        return (T) this;
    }

    private void populate(Map<String, AttributeValue> values) {
        for (final Map.Entry<String, Types> attributeTypesEntry: this.getSchema().getAttributeTypesMap().entrySet()) {
            final String attributeName = attributeTypesEntry.getKey();
            final ValueType attributeValueType = this.getSchema().getAttributeValueType(attributeName);
            final Value value = Value.fromDynamoDBAttributeValue(values.get(attributeName), attributeValueType);

            // Is it a primary key?
            if (attributeTypesEntry.getValue().getIndexType() == IndexType.PRIMARY_KEY) {
                this.primaryKey = new Attribute(attributeName, value);
            }

            // Is this a secondary key?
            else if (attributeTypesEntry.getValue().getIndexType() == IndexType.SORT_KEY) {
                this.sortKey = new Attribute(attributeName, value);
            }

            else {
                final Attribute attribute = new Attribute(attributeName, value);
                this.payload.put(attributeName, attribute);
            }
        }
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

        // primary key
        if (! isConfidential(primaryKey.getName()))
            entity.put(this.primaryKey.getName(), this.primaryKey.getValue().get());

        // sort key
        if (this.sortKey != null && !isConfidential(this.sortKey.getName()))
            entity.put(this.sortKey.getName(), this.sortKey.getValue().get());

        for (final Map.Entry<String, Attribute> entry: this.payload.entrySet()) {
            final Attribute attribute = entry.getValue();

            if (!isConfidential(entry.getKey()))
                entity.put(entry.getKey(), attribute.getValue().get());
        }

        return entity;
    }

    private boolean isConfidential(final String attributeName) {
        return this.getSchema().getAttributeTypes(attributeName).isConfidential();
    }

    public Schema getSchema() {
        if (this.schema == null) this.schema = createSchema();

        return this.schema;
    }

    public AmazonDynamoDB getDDB() {
        if (this.ddb == null) this.ddb = createDDBClient();

        return this.ddb;
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

    public void setAttributeValue(final String attributeName, final String attributeValue) {
        this.setAttributeValue(attributeName,
                new Attribute(attributeName, new StringValue(attributeValue)));
    }

    public void setAttributeValue(final String attributeName, final double attributeValue) {
        this.setAttributeValue(attributeName,
                new Attribute(attributeName, new NumberValue(attributeValue)));
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
