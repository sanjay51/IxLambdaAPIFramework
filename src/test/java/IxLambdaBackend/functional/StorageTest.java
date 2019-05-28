package IxLambdaBackend.functional;

import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.Metadata;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.attribute.AttributeType;
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
public class StorageTest {
    final GetItemResult result = new GetItemResult();
    static final AmazonDynamoDB mockDDB = Mockito.mock(AmazonDynamoDB.class);

    @BeforeAll
    public void init() {
        final Map<String, AttributeValue> item = new HashMap<>();

        item.put("name", new AttributeValue("sanjay"));
        item.put("address", new AttributeValue("earth"));

        result.setItem(item);
    }

    @Test
    public void testRead() throws Exception {
        doReturn(result).when(mockDDB).getItem(any());

        final Attribute primaryKey = new Attribute("email",
                new StringValue("x@y.com"),
                new Metadata(AttributeType.PRIMARY_KEY));

        final UserEntity userEntity = new UserEntity(primaryKey, null);

        userEntity.read();
        assertEquals(userEntity.getPayload().get("name").getStringValue(), "sanjay");
        assertEquals(userEntity.getPayload().get("address").getStringValue(), "earth");
    }

    @Test
    public void testReadExceptionCastToInternal() {
        doThrow(new InternalServerErrorException("internal exception")).when(mockDDB).getItem(any());

        final Attribute primaryKey = new Attribute("email",
                new StringValue("x@y.com"),
                new Metadata(AttributeType.PRIMARY_KEY));

        final UserEntity userEntity = new UserEntity(primaryKey, null);

        assertThrows(InternalException.class, () -> userEntity.read());
    }

    @Test
    public void testResourceNotFoundExceptionCastToEntityNotFound() {
        when(mockDDB.getItem(any())).thenThrow(new ResourceNotFoundException("not found"));

        final Attribute primaryKey = new Attribute("email",
                new StringValue("x@y.com"),
                new Metadata(AttributeType.PRIMARY_KEY));

        final UserEntity userEntity = new UserEntity(primaryKey, null);

        assertThrows(EntityNotFoundException.class, () -> userEntity.read());
    }

    static class UserEntity extends DDBEntity {
        private Schema schema;

        public UserEntity(final Attribute primaryKey, final Attribute sortKey) {
            this.setPrimaryKey(primaryKey);
            this.setSortKey(sortKey);
        }

        @Override
        public Schema getSchema() {
            if (this.schema == null) {
                final Map<String, Types> attributeTypeMap = new HashMap<String, Types>() {{
                    put("email", new Types(AttributeType.PRIMARY_KEY, ValueType.STRING));
                    put("address", new Types(AttributeType.REGULAR, ValueType.STRING));
                    put("name", new Types(AttributeType.REGULAR, ValueType.STRING));
                }};

                this.schema = new Schema("users", attributeTypeMap);
            }

            return this.schema;
        }

        @Override
        public AmazonDynamoDB getDDB() {
            return mockDDB;
        }
    }
}
