package IxDeclarativeAPI.storage.attribute;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Attribute {
    final String name;
    final Value value;
    final Metadata metadata;

    public String getStringValue() {
        return (String) this.value.getValue();
    }
}
