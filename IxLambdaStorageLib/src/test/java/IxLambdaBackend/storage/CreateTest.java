package IxLambdaBackend.storage;

import IxLambdaBackend.storage.attribute.IndexType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.exception.EntityAlreadyExistsException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateTest {
    private AmazonDynamoDB mockDDB;

    @BeforeAll
    void init() {
        mockDDB = mock(AmazonDynamoDB.class);
    }

    @Test
    void testHappyCase() {
        doReturn(null).when(mockDDB).putItem(any());

        final UserEntity entity = new UserEntity("sanjay");
        Assertions.assertDoesNotThrow(() -> entity.create());
    }

    @Test
    void assertEntityAlreadyExistsExceptionIfEntityAlreadyExists() {
        doThrow(ConditionalCheckFailedException.class).when(mockDDB).putItem(any());

        final UserEntity entity = new UserEntity("sanjay");
        Assertions.assertThrows(EntityAlreadyExistsException.class,() -> entity.create());
    }

    @Test
    void assertInternalExceptionIfDDBFails() {
        doThrow(InternalServerErrorException.class).when(mockDDB).putItem(any());

        final UserEntity entity = new UserEntity("sanjay");
        Assertions.assertThrows(InternalException.class,() -> entity.create());
    }

    class UserEntity extends DDBEntity {

        public UserEntity(final String primaryKeyValue) {
            super(primaryKeyValue);
        }

        @Override
        public Schema createSchema() {
            final Map<String, Types> attributeTypesMap = new HashMap<String, Types>() {{
                put("userId", new Types(ValueType.STRING, IndexType.PRIMARY_KEY));
                put("email", new Types(ValueType.STRING));
                put("name", new Types(ValueType.STRING));
                put("age", new Types(ValueType.NUMBER));
            }};

            return new Schema("users", attributeTypesMap);
        }

        @Override
        public AmazonDynamoDB createDDBClient() {
            return mockDDB;
        }
    }
}
