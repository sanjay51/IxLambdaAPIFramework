package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class StringSetValue extends Value<Set<String>> {
    final Set<String> value;

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue().withSS();
    }

    @Override
    public Set<String> get() {
        return value;
    }
}
