package IxLambdaBackend.storage;

import IxLambdaBackend.UserEntity;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.value.ValueType;
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
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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
