package IxLambdaBackend.storage.ddb;

import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.schema.Schema;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class DDBUpdateStrategy {
    public static void execute(final Attribute primaryKey,
                               final Attribute sortKey,
                               final Map<String, Attribute> payload,
                               final Schema schema,
                               final AmazonDynamoDB ddb) throws EntityNotFoundException, InternalException {
        final Map<String, AttributeValue> item = new HashMap<>();
        item.put(primaryKey.getName(), primaryKey.getDynamoDBAttributeValue());
        if (sortKey != null) item.put(sortKey.getName(), sortKey.getDynamoDBAttributeValue());

        for (final Map.Entry<String, Attribute> attribute: payload.entrySet()) {
            item.put(attribute.getKey(), attribute.getValue().getDynamoDBAttributeValue());
        }

        // OVERWRITE ONLY
        String putCondition = String.format("attribute_exists(%s)", primaryKey.getName());
        if (sortKey != null) putCondition += String.format(" AND attribute_exists(%s)", sortKey.getName());

        final PutItemRequest request = new PutItemRequest()
                .withTableName(schema.getTableName())
                .withConditionExpression(putCondition)
                .withItem(item);

        try {
            ddb.putItem(request);
        } catch (ConditionalCheckFailedException e) {
            // TODO: log for monitoring
            System.out.println("Entity already exists: " + e);
            throw new EntityNotFoundException("Already exists");
        } catch (final Throwable e) {
            // TODO: log for monitoring
            System.out.println("[ERROR]" + e);
            throw new InternalException("An internal exception occurred. Please try again.");
        }
    }
}
