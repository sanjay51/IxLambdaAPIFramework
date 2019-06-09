package IxLambdaBackend.storage;

import IxLambdaBackend.UserEntity;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.Metadata;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
