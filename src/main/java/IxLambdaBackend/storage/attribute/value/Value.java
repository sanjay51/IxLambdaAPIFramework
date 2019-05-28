package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public abstract class Value {
    //https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.DataTypes.html

    public abstract AttributeValue toDynamoDBAttributeValue();

    public static Value fromDynamoDBAttributeValue(final AttributeValue attributeValue, final ValueType type) {
        switch (type) {
            case NUMBER:
                return new NumberValue(attributeValue.getN());

            case BOOLEAN:
                return new BooleanValue(attributeValue.getBOOL());

            default:
                return new StringValue(attributeValue.getS());
        }
    }
}
