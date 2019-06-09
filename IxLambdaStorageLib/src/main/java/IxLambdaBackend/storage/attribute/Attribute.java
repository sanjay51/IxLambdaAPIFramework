package IxLambdaBackend.storage.attribute;

import IxLambdaBackend.storage.attribute.value.BooleanValue;
import IxLambdaBackend.storage.attribute.value.NumberValue;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.value.Value;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Attribute {
    final String name;
    final Value value;
    final Metadata metadata;

    public AttributeValue getDynamoDBAttributeValue() {
        return this.value.toDynamoDBAttributeValue();
    }

    public String getStringValue() {
        return ((StringValue) this.value).getValue();
    }

    public int getIntValue() {
        return (int) ((NumberValue) this.value).getValue();
    }

    public float getFloatValue() {
        return (float) ((NumberValue) this.value).getValue();
    }

    public boolean getBooleanValue() {
        return ((BooleanValue) this.value).isValue();
    }
}
