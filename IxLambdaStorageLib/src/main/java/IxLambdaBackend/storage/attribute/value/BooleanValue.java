package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BooleanValue extends Value<Boolean> {
    final boolean value;

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue().withBOOL(value);
    }

    @Override
    public Boolean get() {
        return value;
    }
}
