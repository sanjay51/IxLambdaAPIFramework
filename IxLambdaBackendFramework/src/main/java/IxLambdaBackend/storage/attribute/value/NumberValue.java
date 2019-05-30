package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NumberValue extends Value {
    @Getter final double value;

    public NumberValue(final String s) {
        this.value = Double.parseDouble(s);
    }

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue().withN(String.valueOf(value));
    }
}
