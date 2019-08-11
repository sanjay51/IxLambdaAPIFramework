package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashSet;
import java.util.List;

public abstract class Value<T> {
    //https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.DataTypes.html

    public abstract AttributeValue toDynamoDBAttributeValue();
    public abstract T get();

    public static Value fromDynamoDBAttributeValue(final AttributeValue attributeValue, final ValueType type) {
        if (attributeValue == null) return new NullValue();

        switch (type) {
            case NUMBER:
                if (attributeValue.getN() == null) return new NullValue();
                return new NumberValue(attributeValue.getN());

            case BOOLEAN:
                if (attributeValue.getB() == null) return new NullValue();
                return new BooleanValue(attributeValue.getBOOL());

            case STRING:
                if (attributeValue.getS() == null) return new NullValue();
                return new StringValue(attributeValue.getS());

            case STRING_SET:
                List<String> ss = attributeValue.getSS();
                return new StringSetValue(ss == null? new HashSet<>(): new HashSet<>(ss));

            default:
                return new NullValue();
        }
    }
}
