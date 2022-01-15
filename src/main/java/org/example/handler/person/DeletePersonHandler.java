package org.example.handler.person;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;
import org.example.model.GenericResponse;
import org.example.model.PersonRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;

import java.util.HashMap;
import java.util.Map;

import static org.example.util.DynamoDBHelper.attributeStringValue;

public class DeletePersonHandler implements RequestHandler<PersonRequest, GenericResponse> {
    private DynamoDbClient dynamoDbClient;

    @Override
    public GenericResponse handleRequest(PersonRequest personRequest, Context context) {
        initDynamoDbClient();
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();

        keyToGet.put("personId", attributeStringValue(personRequest.getPersonId()));
        String tableName = System.getenv("TABLE_NAME");
        DeleteItemRequest deleteReq = DeleteItemRequest.builder().tableName(StringUtils.isNullOrEmpty(tableName) ? "Person" : tableName).key(keyToGet).build();
        DeleteItemResponse deleteItemResponse = dynamoDbClient.deleteItem(deleteReq);

        System.out.println("Status : " + deleteItemResponse.sdkHttpResponse().statusCode());

        System.out.println("Consumed Capacity : " + deleteItemResponse.sdkHttpResponse().isSuccessful());

        deleteItemResponse.attributes().forEach((key, value) -> System.out.println(key + " " + value));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GenericResponse(deleteItemResponse.sdkHttpResponse().statusText().get(), headers, 200);
    }

    private void initDynamoDbClient() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1)
                .build();
    }
}
