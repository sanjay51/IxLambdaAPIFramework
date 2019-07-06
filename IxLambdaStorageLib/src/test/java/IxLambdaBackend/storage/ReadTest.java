package IxLambdaBackend.storage;

import IxLambdaBackend.storage.attribute.IndexType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReadTest {
    final GetItemResult result = new GetItemResult();
    static final AmazonDynamoDB mockDDB = Mockito.mock(AmazonDynamoDB.class);

    @BeforeAll
    public void init() {
        final Map<String, AttributeValue> item = new HashMap<>();

        item.put("name", new AttributeValue("sanjay"));
        item.put("address", new AttributeValue("earth"));

        result.setItem(item);
    }

    @Test
    public void testRead() throws Exception {
        doReturn(result).when(mockDDB).getItem(any());

        final UserEntity userEntity = new UserEntity("x@y.com");

        userEntity.read();
        assertEquals(userEntity.getPayload().get("name").getStringValue(), "sanjay");
        assertEquals(userEntity.getPayload().get("address").getStringValue(), "earth");
    }

    @Test
    public void testReadExceptionCastToInternal() {
        doThrow(new InternalServerErrorException("internal exception")).when(mockDDB).getItem(any());

        final UserEntity userEntity = new UserEntity("x@y.com");

        assertThrows(InternalException.class, () -> userEntity.read());
    }

    @Test
    public void testResourceNotFoundExceptionCastToEntityNotFound() {
        when(mockDDB.getItem(any())).thenThrow(new ResourceNotFoundException("not found"));

        final UserEntity userEntity = new UserEntity("x@y.com");

        assertThrows(EntityNotFoundException.class, () -> userEntity.read());
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
