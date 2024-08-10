package org.patro;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Before running this Java V2 code example, set up your development
 * environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 *
 * To place items into an Amazon DynamoDB table using the AWS SDK for Java V2,
 * its better practice to use the
 * Enhanced Client. See the EnhancedPutItem example.
 */
public class Handler {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void putItemInTable(DynamoDbClient ddb,
            String tableName,
            String key,
            String keyVal,
            String seqNo,
            String seqNoVal) 
    {

        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put(key, AttributeValue.builder().n(keyVal).build());
        itemValues.put(seqNo, AttributeValue.builder().n(seqNoVal).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            PutItemResponse response = ddb.putItem(request);
            System.out.println(tableName + " was successfully updated. The request id is "
                    + response.responseMetadata().requestId());

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static QueryResponse getItemFromTable(DynamoDbClient ddb,
            String tableName,
            String key,
            String keyVal) 
    {

        HashMap<String, Condition> conditions = new HashMap<String, Condition>();
        ComparisonOperator eq = ComparisonOperator.EQ;
        AttributeValue value = AttributeValue.builder().n(keyVal).build();
        Condition c = Condition.builder().comparisonOperator(eq).attributeValueList(value).build();
        conditions.put(key, c);

        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .keyConditions(conditions)
                .scanIndexForward(false)
                .consistentRead(true)
                .limit(1)
                .build();

        logger.info ("request: " + request.toString());

        try {
            QueryResponse response = ddb.query(request);
            return response;

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return null;

    }

    public static void sendWriteRequest(int seqNoVal) {
        logger.info("WriteRequest starts seqNo: " + seqNoVal);

        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        putItemInTable(ddb, Configuration.TABLE_NAME, Configuration.KEY, Configuration.KEY_VAL, Configuration.SEQ_NO, String.valueOf(seqNoVal));

        ddb.close();

        logger.info("WriteRequest ends");
    }

    public static int sendReadRequest() {
        logger.info("ReadRequest starts");

        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        QueryResponse response = getItemFromTable(ddb, Configuration.TABLE_NAME, Configuration.KEY, Configuration.KEY_VAL);
//        logger.info("ReadRequest response: " + response);

        List<Map<String, AttributeValue>> items = response.items();
//        logger.info("ReadRequest items: " + items);

        if (items.isEmpty()) {
            return 0;
        }

        Map<String, AttributeValue> map = items.get(0);
        logger.info("ReadRequest map: " + map);

        AttributeValue value = map.get(Configuration.SEQ_NO);
        logger.info ("ReadRequest value: " + value);

        String i = value.n();
        logger.info("ReadRequest i: " + i);

        int result = Integer.parseInt(i);
        ddb.close();

        logger.info("ReadRequest ends result: " + result);

        return result;
    }

    public static void trimTable() {
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        ScanRequest request = ScanRequest.builder().tableName(Configuration.TABLE_NAME).build();
        ScanResponse response;

        
        do {
            response = ddb.scan(request);
//            logger.info ("trimTable response: " + response);
            
            for (Map<String, AttributeValue> item : response.items()) {
                Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
                key.put(Configuration.KEY, item.get(Configuration.KEY));
                key.put(Configuration.SEQ_NO, item.get(Configuration.SEQ_NO));
                logger.info ("trimTable deleting item: " + item);
                DeleteItemRequest deleteRequest = DeleteItemRequest.builder().tableName(Configuration.TABLE_NAME).key(key).build();
                ddb.deleteItem(deleteRequest);

            }

        } while (response.count() > 0);
         
        ddb.close();


    }

}
