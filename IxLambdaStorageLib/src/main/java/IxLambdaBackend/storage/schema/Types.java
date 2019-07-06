package IxLambdaBackend.storage.schema;


import IxLambdaBackend.storage.attribute.IndexType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Types {
    IndexType indexType = IndexType.NONE;
    final ValueType valueType;
    boolean isConfidential = false;

    public Types(final ValueType valueType, final IndexType indexType) {
        this.indexType = indexType;
        this.valueType = valueType;
    }

    public Types markConfidential() {
        this.isConfidential = isConfidential = true;
        return this;
    }
}
