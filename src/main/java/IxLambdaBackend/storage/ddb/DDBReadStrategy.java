package IxLambdaBackend.storage.ddb;

import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class DDBReadStrategy {
    public static Map<String, AttributeValue> execute(final Attribute primaryKey,
                                                      final Attribute sortKey,
                                                      final String tableName,
                                                      final AmazonDynamoDB ddb) throws EntityNotFoundException, InternalException {
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put(primaryKey.getName(), new AttributeValue(primaryKey.getStringValue()));

        if (sortKey != null)
            key.put(sortKey.getName(), new AttributeValue(sortKey.getStringValue()));

        final GetItemRequest request = new GetItemRequest()
                .withKey(key)
                .withTableName(tableName);

        // retrieve
        final Map<String, AttributeValue> response;
        try {
            response = ddb.getItem(request).getItem();
        } catch (final ResourceNotFoundException e) {
            String message = "Entity not found with id: " + primaryKey.getStringValue();
            if (sortKey != null) message += ":" + sortKey.getStringValue();

            throw new EntityNotFoundException(message);
        } catch (final Throwable e) {
            // TODO: log for monitoring
            System.out.println("[ERROR]" + e);
            throw new InternalException("An internal exception occurred. Please try again.");
        }

        return response;
    }
}
