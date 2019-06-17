package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public abstract class Value<T> {
    //https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.DataTypes.html

    public abstract AttributeValue toDynamoDBAttributeValue();
    public abstract T get();

    public static Value fromDynamoDBAttributeValue(final AttributeValue attributeValue, final ValueType type) {
        if (attributeValue == null) return new NullValue();

        switch (type) {
            case NUMBER:
                return new NumberValue(attributeValue.getN());

            case BOOLEAN:
                return new BooleanValue(attributeValue.getBOOL());

            case STRING:
                return new StringValue(attributeValue.getS());

            default:
                return new NullValue();
        }
    }
}
