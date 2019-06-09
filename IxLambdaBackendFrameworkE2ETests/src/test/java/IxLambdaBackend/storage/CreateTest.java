package IxLambdaBackend.storage;

import IxLambdaBackend.UserEntity;
import IxLambdaBackend.storage.exception.EntityAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateTest {

    @Test
    void testHappyCase() throws Exception {
        final UserEntity entity = new UserEntity("sanjay");
        entity.delete();
        Assertions.assertDoesNotThrow(() -> entity.create());
    }

    @Test
    void assertEntityAlreadyExistsExceptionIfEntityAlreadyExists() throws Exception {
        final UserEntity entity = new UserEntity("sanjay");
        entity.delete(); //delete if already exists
        entity.create();
        Assertions.assertThrows(EntityAlreadyExistsException.class,() -> entity.create());
    }
}
