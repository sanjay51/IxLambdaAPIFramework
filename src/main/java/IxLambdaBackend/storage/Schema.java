package IxLambdaBackend.storage;

import IxLambdaBackend.storage.attribute.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class Schema {
    final String tableName;
    final Map<String, Type> attributeTypeMap;
}
