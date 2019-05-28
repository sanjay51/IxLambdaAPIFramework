package IxLambdaBackend.storage.schema;


import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Types {
    final AttributeType attributeType;
    final ValueType valueType;
}
