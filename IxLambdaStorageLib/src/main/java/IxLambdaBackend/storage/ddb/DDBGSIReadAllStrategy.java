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

public class DDBGSIReadAllStrategy {
    public static List<Map<String, AttributeValue>> execute(final Attribute primaryKey,
                                                      final Attribute sortKey,
                                                      final String tableName,
                                                      final String indexName,
                                                      final AmazonDynamoDB ddb) throws EntityNotFoundException {

        final Map<String, Condition> keyConditions = new HashMap<>();

        final Condition primaryKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(primaryKey.getStringValue()));
        keyConditions.put(primaryKey.getName(), primaryKeyCondition);

        if (sortKey != null) {
            final Condition sortKeyCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(sortKey.getStringValue()));
            keyConditions.put(sortKey.getName(), sortKeyCondition);
        }

        final QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withIndexName(indexName)
                .withKeyConditions(keyConditions);

        final List<Map<String, AttributeValue>> items = ddb.query(queryRequest).getItems();

        if (items.size() > 0) {
            return items;
        }

        String message = "Entity not found with GSI `" + indexName +
                "` and primary key " + primaryKey.getDynamoDBAttributeValue();
        throw new EntityNotFoundException(message);
    }
}
