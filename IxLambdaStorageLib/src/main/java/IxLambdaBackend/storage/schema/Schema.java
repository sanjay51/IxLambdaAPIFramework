package IxLambdaBackend.storage.schema;

import IxLambdaBackend.storage.attribute.IndexType;
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
                .filter(e -> e.getValue().getIndexType() == IndexType.PRIMARY_KEY)
                .findFirst()
                .map(e -> e.getKey())
                .get();
    }

    public Optional<String> getSortKeyName() {
        return this.attributeTypesMap.entrySet()
                .stream()
                .filter(e -> e.getValue().getIndexType() == IndexType.SORT_KEY)
                .findFirst()
                .map(e -> e.getKey());
    }

    public Optional<String> getGSIPrimaryKeyName() {
        return this.attributeTypesMap.entrySet()
                .stream()
                .filter(e -> e.getValue().getIndexType() == IndexType.GSI_PRIMARY_KEY)
                .findFirst()
                .map(e -> e.getKey());

    }

    public Optional<String> getGSISortKeyName() {
        return this.attributeTypesMap.entrySet()
                .stream()
                .filter(e -> e.getValue().getIndexType() == IndexType.GSI_SORT_KEY)
                .findFirst()
                .map(e -> e.getKey());

    }

    public ValueType getAttributeValueType(final String attributeName) {
        return this.attributeTypesMap.get(attributeName).getValueType();
    }

    public Types getAttributeTypes(final String attributeName) {
        return this.attributeTypesMap.get(attributeName);
    }

    public boolean isValidAttribute(final String attributeName) {
        return this.attributeTypesMap.get(attributeName) != null;
    }
}
