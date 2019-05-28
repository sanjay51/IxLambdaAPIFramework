package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StringValue extends Value {
    final String value;

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue(this.value);
    }
}
