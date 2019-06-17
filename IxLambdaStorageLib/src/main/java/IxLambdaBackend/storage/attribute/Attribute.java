package IxLambdaBackend.storage.attribute;

import IxLambdaBackend.storage.attribute.value.BooleanValue;
import IxLambdaBackend.storage.attribute.value.NumberValue;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.value.Value;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Attribute {
    @NonNull final String name;
    @NonNull final Value value;
    @NonNull final Metadata metadata;

    public AttributeValue getDynamoDBAttributeValue() {
        return this.value.toDynamoDBAttributeValue();
    }

    public String getStringValue() {
        return ((StringValue) this.value).get();
    }

    public int getIntValue() {
        return ((NumberValue) this.value).get().intValue();
    }

    public float getFloatValue() {
        return ((NumberValue) this.value).get().floatValue();
    }

    public boolean getBooleanValue() {
        return ((BooleanValue) this.value).get();
    }
}
