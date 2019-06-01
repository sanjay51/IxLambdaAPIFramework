package IxLambdaBackend.storage;

import IxLambdaBackend.UserEntity;
import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.exception.InvalidInputException;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateTest {

    @BeforeAll
    void init() throws Exception {
        final UserEntity entity = new UserEntity("sanjay");
        entity.setAttributeValue("email",
                new Attribute("email", new StringValue("sanjay@sanjay.com"), new Metadata(AttributeType.REGULAR)));
        entity.delete();
        entity.create();
    }

    @Test
    void testHappyCase() throws Exception {
        final UserEntity entity = new UserEntity("sanjay");
        entity.setAttributeValue("email",
                new Attribute("email", new StringValue("sanjay@sanjay51.com"), new Metadata(AttributeType.REGULAR)));
        assertDoesNotThrow(() -> entity.update());

        entity.clear();
        entity.read();

        assertEquals("sanjay@sanjay51.com", entity.getPayload().get("email").getStringValue());
    }

    @Test
    void assertExceptionIfEntityDoesNotExist() throws Exception {
        final UserEntity entity = new UserEntity("mytempentity");
        entity.delete();

        entity.setAttributeValue("email",
                new Attribute("email", new StringValue("sanjay@sanjay51.com"), new Metadata(AttributeType.REGULAR)));
        assertThrows(EntityNotFoundException.class,() -> entity.update());
    }
}
