package IxLambdaBackend.storage;

import IxLambdaBackend.UserEntity;
import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.Metadata;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.value.ValueType;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReadTest {
    final UserEntity userEntity = new UserEntity("github.com/sanjay51");

    @BeforeAll
    public void init() throws Exception {
        userEntity.setAttributeValue("email",
                        new Attribute("email", new StringValue("gmail@gmail.com"),
                        new Metadata(AttributeType.REGULAR)));
        userEntity.delete();
        userEntity.create();
    }

    @Test
    public void testRead() throws Exception {
        userEntity.clear();
        userEntity.read();

        assertEquals(userEntity.getPayload().get("email").getStringValue(), "gmail@gmail.com");
    }

    @Test
    public void testResourceNotFoundExceptionCastToEntityNotFound() {
        final UserEntity userEntity = new UserEntity("x@y.com");

        assertThrows(EntityNotFoundException.class, () -> userEntity.read());
    }
}
