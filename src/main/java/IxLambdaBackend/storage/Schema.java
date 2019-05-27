package IxLambdaBackend.storage;

import IxLambdaBackend.storage.attribute.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class Schema {
    final String tableName;
    final Map<String, Type> attributeTypeMap;

    final String getPrimaryKeyName() {
        return this.attributeTypeMap.entrySet()
                .stream()
                .filter(e -> e.getValue() == Type.PRIMARY_KEY)
                .findFirst()
                .map(e -> e.getKey())
                .get();
    }

    final Optional<String> getSortKeyName() {
        return this.attributeTypeMap.entrySet()
                .stream()
                .filter(e -> e.getValue() == Type.SORT_KEY)
                .findFirst()
                .map(e -> e.getKey());
    }
}
