package org.example.handler.person;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.example.model.GenericResponse;
import org.example.model.PersonRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

public class GetPersonsHandler implements RequestHandler<PersonRequest, GenericResponse> {
    private DynamoDbClient dynamoDbClient;

    @Override
    public GenericResponse handleRequest(PersonRequest personRequest, Context context) {

        initDynamoDbClient();
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();

        // Create a GetItemRequest instance
        String tableName = System.getenv("TABLE_NAME");
        GetItemRequest request = GetItemRequest.builder().tableName(tableName).build();

        // Invoke the DynamoDbAsyncClient object's getItem
        GetItemResponse item = dynamoDbClient.getItem(request);

        Map<String, AttributeValue> map = item.item();
        map.forEach((key, value) -> System.out.println(key + " " + value));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GenericResponse(item.sdkHttpResponse().statusText().get(), headers, 200);
    }

    private void initDynamoDbClient() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1)
                .build();
    }
}
