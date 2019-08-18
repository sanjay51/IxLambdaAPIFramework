package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.CollectionUtils;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class StringSetValue extends Value<Set<String>> {
    final Set<String> value;

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        if (CollectionUtils.isNullOrEmpty(value))
            return new AttributeValue().withSS();
        
        return new AttributeValue().withSS(value);
    }

    @Override
    public Set<String> get() {
        return value;
    }
}
