package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StringValue extends Value<String> {
    final String value;

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue(this.value);
    }

    @Override
    public String get() {
        return value;
    }
}
