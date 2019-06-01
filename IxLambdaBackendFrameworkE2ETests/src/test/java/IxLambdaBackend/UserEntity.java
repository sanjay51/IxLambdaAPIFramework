package IxLambdaBackend;

import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.AttributeType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import java.util.HashMap;
import java.util.Map;

public class UserEntity extends DDBEntity<UserEntity> {
    private AmazonDynamoDB ddb;

    public UserEntity(final String primaryKeyValue) {
        super(primaryKeyValue);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributeTypesMap = new HashMap<String, Types>() {{
            put("userId", new Types(AttributeType.PRIMARY_KEY, ValueType.STRING));
            put("email", new Types(AttributeType.REGULAR, ValueType.STRING));
        }};

        return new Schema("users_e2e_tests", attributeTypesMap);
    }

    @Override
    public AmazonDynamoDB getDDB() {
        if (this.ddb == null)
            this.ddb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();

        return this.ddb;
    }
}
