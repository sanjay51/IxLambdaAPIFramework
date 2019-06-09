package IxLambdaBackend.storage;

import IxLambdaBackend.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteTest {

    @Test
    public void testDelete() throws Exception {
        final UserEntity userEntity = new UserEntity("github.com/sanjay51" + new Random().nextInt(1000));
        userEntity.create();
        assertDoesNotThrow(() -> userEntity.delete());
    }

    @Test
    public void assertDoubleDeleteDoesNotThrowException() throws Exception {
        final UserEntity userEntity = new UserEntity("github.com/sanjay51");
        userEntity.delete();
        assertDoesNotThrow(() -> userEntity.delete());
    }
}
