package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class Value<T> {
    //https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.DataTypes.html

    public abstract AttributeValue toDynamoDBAttributeValue();
    public abstract T get();

    static ObjectMapper objectMapper = new ObjectMapper();

    public abstract ValueType getType();

    public String getAsString() throws Exception {
        if (this.getType() == ValueType.STRING) return (String) get();
        if (this.getType() == ValueType.BOOLEAN) return Boolean.toString((boolean) this.get());
        if (this.getType() == ValueType.NUMBER) return Double.toString((Double) this.get());
        if (this.getType() == ValueType.STRING_SET) {
            return objectMapper.writeValueAsString(this.get());
        }
        if (this.getType() == ValueType.MAP) {
            return objectMapper.writeValueAsString(this.get());
        }

        throw new RuntimeException("Invalid value type");
    }

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
                if (ss == null) return new NullValue();
                return new StringSetValue(new HashSet<>(ss));

            case MAP:
                Map<String, AttributeValue> map = attributeValue.getM();
                if (map == null) return new NullValue();
                return MapValue.newInstanceWithGenericValues(map);

            case NULL:
                return new NullValue();

            default:
                throw new RuntimeException("Invalid value type");
        }
    }
}
