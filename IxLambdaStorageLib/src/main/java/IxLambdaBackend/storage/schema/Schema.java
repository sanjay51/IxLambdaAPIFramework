package IxLambdaBackend.storage.schema;

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

    public boolean hasWriteAccess(final String attributeName) {
        final AccessType access = this.attributeTypesMap.get(attributeName).getAccess();
        return (access == AccessType.READ_WRITE) || (access == AccessType.WRITE_ONLY);
    }

    public boolean hasReadAccess(final String attributeName) {
        final AccessType access = this.attributeTypesMap.get(attributeName).getAccess();
        return (access == AccessType.READ_WRITE) || (access == AccessType.READ_ONLY);
    }
}
