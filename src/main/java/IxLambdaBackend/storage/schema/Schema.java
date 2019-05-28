package IxLambdaBackend.storage.schema;

import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class Schema {
    final String tableName;
    final Map<String, Types> attributeTypesMap;

    public String getPrimaryKeyName() {
        return this.attributeTypesMap.entrySet()
                .stream()
                .filter(e -> e.getValue().getAttributeType() == AttributeType.PRIMARY_KEY)
                .findFirst()
                .map(e -> e.getKey())
                .get();
    }

    public Optional<String> getSortKeyName() {
        return this.attributeTypesMap.entrySet()
                .stream()
                .filter(e -> e.getValue().getAttributeType() == AttributeType.SORT_KEY)
                .findFirst()
                .map(e -> e.getKey());
    }

    public ValueType getAttributeValueType(final String attributeName) {
        return this.attributeTypesMap.get(attributeName).getValueType();
    }
}
