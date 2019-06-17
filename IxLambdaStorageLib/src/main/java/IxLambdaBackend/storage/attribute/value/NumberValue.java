package IxLambdaBackend.storage.attribute.value;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NumberValue extends Value<Double> {
    final double value;

    public NumberValue(final String s) {
        this.value = Double.parseDouble(s);
    }

    @Override
    public AttributeValue toDynamoDBAttributeValue() {
        return new AttributeValue().withN(String.valueOf(value));
    }

    @Override
    public Double get() {
        return value;
    }
}
