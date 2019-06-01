package IxLambdaBackend.storage;

import IxLambdaBackend.UserEntity;
import IxLambdaBackend.exception.EntityAlreadyExistsException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

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
