package IxLambdaBackend.storage.ddb;

import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDBReadAllStrategy {
    public static List<Map<String, AttributeValue>> execute(final Attribute primaryKey,
                                                      final String tableName,
                                                      final AmazonDynamoDB ddb) throws EntityNotFoundException, InternalException {

        final Map<String, Condition> keyConditions = new HashMap<>();

        final Condition primaryKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(primaryKey.getStringValue()));
        keyConditions.put(primaryKey.getName(), primaryKeyCondition);

        final QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditions(keyConditions);

        List<Map<String, AttributeValue>> items;
        try {
            items = ddb.query(queryRequest).getItems();
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }

        if (items.size() > 0) {
            return items;
        }

        String message = "Entity not found with primary key " + primaryKey.getDynamoDBAttributeValue();
        throw new EntityNotFoundException(message);
    }
}
