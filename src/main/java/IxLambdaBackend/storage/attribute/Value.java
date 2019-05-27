package IxLambdaBackend.storage.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value<T> {
    T value;
}
