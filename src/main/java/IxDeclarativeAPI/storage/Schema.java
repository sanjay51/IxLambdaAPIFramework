package IxDeclarativeAPI.storage;

import IxDeclarativeAPI.storage.attribute.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class Schema {
    final String tableName;
    final Map<String, Type> attributeTypeMap;
}
