package IxLambdaBackend.storage.schema;


import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Types {
    final AttributeType attributeType;
    final ValueType valueType;
    boolean isConfidential = false;
}
