package IxDeclarativeAPI.functional;

import IxDeclarativeAPI.storage.DDBEntity;
import IxDeclarativeAPI.storage.Schema;
import IxDeclarativeAPI.storage.attribute.Attribute;
import IxDeclarativeAPI.storage.attribute.Metadata;
import IxDeclarativeAPI.storage.attribute.Type;
import IxDeclarativeAPI.storage.attribute.Value;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class StorageTest {
    static final AmazonDynamoDB mockDDB = Mockito.mock(AmazonDynamoDB.class);

    @Test
    public void testBasicCase() {
        final Attribute primaryKey = new Attribute("email",
                new Value<>("x@y.com"),
                new Metadata(Type.PRIMARY_KEY));

        final UserEntity userEntity = new UserEntity(primaryKey, null);

        //userEntity.read();
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
                final Map<String, Type> attributeTypeMap = new HashMap<String, Type>() {{
                    put("email", Type.PRIMARY_KEY);
                    put("password", Type.REGULAR);
                    put("name", Type.REGULAR);
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
