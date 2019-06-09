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

import static org.junit.jupiter.api.Assertions.*;

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
