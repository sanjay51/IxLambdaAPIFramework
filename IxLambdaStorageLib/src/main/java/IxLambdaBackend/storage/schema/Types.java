package IxLambdaBackend.storage.schema;


import IxLambdaBackend.storage.attribute.value.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Types {
    IndexType indexType = IndexType.NONE;
    AccessType access = AccessType.READ_WRITE;
    final ValueType valueType;

    public Types(final ValueType valueType, final IndexType indexType) {
        this.indexType = indexType;
        this.valueType = valueType;
    }

    public Types withAccess(final AccessType access) {
        this.access = access;
        return this;
    }
}
