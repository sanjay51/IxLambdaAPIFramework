package IxLambdaBackend.storage.ddb;

import IxLambdaBackend.storage.Page;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDBReadAllStrategy {
    public static QueryResult execute(final Attribute primaryKey, final String tableName, final AmazonDynamoDB ddb,
                               final Map<String, AttributeValue> paginationHandle, final int pageSize)
            throws EntityNotFoundException, InternalException {

        final Map<String, Condition> keyConditions = new HashMap<>();

        final Condition primaryKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(primaryKey.getStringValue()));
        keyConditions.put(primaryKey.getName(), primaryKeyCondition);

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditions(keyConditions);

        if (pageSize > 0) queryRequest = queryRequest.withLimit(pageSize);
        if (paginationHandle != null) queryRequest = queryRequest.withExclusiveStartKey(paginationHandle);

        final QueryResult result;
        try {
            result = ddb.query(queryRequest);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }

        if (result.getItems().size() > 0) {
            return result;
        }

        final String message = "Entity not found with primary key " + primaryKey.getDynamoDBAttributeValue();
        throw new EntityNotFoundException(message);
    }
}
