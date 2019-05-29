package IxLambdaBackend.storage.ddb;

import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;

import java.util.HashMap;
import java.util.Map;

public class DDBDeleteStrategy {
    public static void execute(final Attribute primaryKey,
                                                      final Attribute sortKey,
                                                      final String tableName,
                                                      final AmazonDynamoDB ddb) throws InternalException {
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put(primaryKey.getName(), primaryKey.getDynamoDBAttributeValue());

        if (sortKey != null)
            key.put(sortKey.getName(), sortKey.getDynamoDBAttributeValue());

        final DeleteItemRequest request = new DeleteItemRequest()
                .withKey(key)
                .withTableName(tableName);

        try {
            ddb.deleteItem(request);
        } catch (final Throwable e) {
            // TODO: log for monitoring
            System.out.println("[ERROR]" + e);
            throw new InternalException("An internal exception occurred. Please try again.");
        }
    }
}
