package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BooleanValue extends Value {
    private final boolean value;

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue().withBOOL(value);
    }
}
