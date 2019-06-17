package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NullValue extends Value {
    public String get() {
        return null;
    }

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue().withNULL(true);
    }
}
