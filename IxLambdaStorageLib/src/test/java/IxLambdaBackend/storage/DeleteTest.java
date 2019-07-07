package IxLambdaBackend.storage;

import IxLambdaBackend.storage.schema.IndexType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteTest {
    static final AmazonDynamoDB mockDDB = Mockito.mock(AmazonDynamoDB.class);

    @Test
    public void testRead() {
        doReturn(null).when(mockDDB).deleteItem(any());

        final UserEntity userEntity = new UserEntity("x@y.com");

        assertDoesNotThrow(() -> userEntity.delete());
    }

    @Test
    public void testReadExceptionCastToInternal() {
        doThrow(new InternalServerErrorException("internal exception")).when(mockDDB).deleteItem(any());

        final UserEntity userEntity = new UserEntity("x@y.com");

        assertThrows(InternalException.class, () -> userEntity.delete());
    }

    @Test
    public void testResourceNotFoundExceptionCastToEntityNotFound() {
        when(mockDDB.deleteItem(any())).thenThrow(new ResourceNotFoundException("not found"));

        final UserEntity userEntity = new UserEntity("x@y.com");

        assertThrows(InternalException.class, () -> userEntity.delete());
    }

    static class UserEntity extends DDBEntity<UserEntity> {

        public UserEntity(final String primaryKeyValue) {
            super(primaryKeyValue);
        }

        @Override
        public Schema createSchema() {
            final Map<String, Types> attributeTypeMap = new HashMap<String, Types>() {{
                put("email", new Types(ValueType.STRING, IndexType.PRIMARY_KEY));
                put("address", new Types(ValueType.STRING));
                put("name", new Types(ValueType.STRING));
            }};

            return new Schema("users", attributeTypeMap);
        }

        @Override
        public AmazonDynamoDB createDDBClient() {
            return mockDDB;
        }
    }
}
