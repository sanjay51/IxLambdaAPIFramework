package IxLambdaBackend.storage;

import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.storage.exception.InvalidInputException;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.Metadata;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.value.ValueType;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateTest {
    private AmazonDynamoDB mockDDB;

    @BeforeAll
    void init() {
        mockDDB = mock(AmazonDynamoDB.class);
    }

    @Test
    void testHappyCase() {
        doReturn(null).when(mockDDB).putItem(any());

        final UserEntity entity = new UserEntity("sanjay");
        entity.setAttributeValue("email",
                new Attribute("email", new StringValue("asdf"), new Metadata(AttributeType.REGULAR)));
        Assertions.assertDoesNotThrow(() -> entity.update());
    }

    @Test
    void assertEntityAlreadyExistsExceptionIfEntityAlreadyExists() {
        doThrow(ConditionalCheckFailedException.class).when(mockDDB).putItem(any());

        final UserEntity entity = new UserEntity("sanjay");
        assertThrows(EntityNotFoundException.class,() -> entity.update());
    }

    @Test
    void assertInternalExceptionIfDDBFails() {
        doThrow(InternalServerErrorException.class).when(mockDDB).putItem(any());

        final UserEntity entity = new UserEntity("sanjay");
        assertThrows(InternalException.class,() -> entity.update());
    }

    @Test
    void assertInvalidInputExceptionIfAttributeNotInSchema() {
        final UserEntity entity = new UserEntity("sanjay");
        final Attribute badAttribute =
                new Attribute("email", new StringValue("asdf"), new Metadata(AttributeType.REGULAR));
        assertThrows(InvalidInputException.class, () ->
                entity.setAttributeValue("badAttribute", badAttribute));
    }

    class UserEntity extends DDBEntity {
        public UserEntity(final String primaryKeyValue) {
            super(primaryKeyValue);
        }

        @Override
        public Schema createSchema() {
            final Map<String, Types> attributeTypesMap = new HashMap<String, Types>() {{
                put("userId", new Types(AttributeType.PRIMARY_KEY, ValueType.STRING));
                put("email", new Types(AttributeType.REGULAR, ValueType.STRING));
                put("name", new Types(AttributeType.REGULAR, ValueType.STRING));
                put("age", new Types(AttributeType.REGULAR, ValueType.NUMBER));
            }};

            return new Schema("users", attributeTypesMap);
        }

        @Override
        public AmazonDynamoDB getDDB() {
            return mockDDB;
        }
    }
}
