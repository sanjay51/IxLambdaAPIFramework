package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.CollectionUtils;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MapValue extends Value<Map<String, Value>> {
    final Map<String, Value> value;

    @Override
    public ValueType getType() {
        return ValueType.MAP;
    }

    public static MapValue newInstanceWithStringValues(final Map<String, String> m) {
        final Map<String, Value> value = m.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> new StringValue(entry.getValue())));

        return new MapValue(value);
    }

    public static MapValue newInstanceWithGenericValues(final Map<String, AttributeValue> m) {
        final Map<String, Value> value = m.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> new StringValue(entry.getValue().getS())));

        return new MapValue(value);
    }

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        if (value == null || value.size() == 0)
            return new AttributeValue().withM(new HashMap<>());

        final Map<String, AttributeValue> m = value.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(), e -> e.getValue().toDynamoDBAttributeValue())
        );

        return new AttributeValue().withM(m);
    }

    @Override
    public Map<String, Value> get() {
        return value;
    }
}
