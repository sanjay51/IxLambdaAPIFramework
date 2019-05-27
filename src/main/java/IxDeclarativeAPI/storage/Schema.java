package IxDeclarativeAPI.storage;

import IxDeclarativeAPI.storage.attribute.Type;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class Schema {
    final String tableName;
    final Map<String, Type> attributeTypeMap;
}
