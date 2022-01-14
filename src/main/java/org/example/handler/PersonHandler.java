package org.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.example.model.GenericResponse;
import org.example.model.PersonRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.HashMap;
import java.util.Map;

public class PersonHandler implements RequestHandler<PersonRequest, GenericResponse> {
    private DynamoDbClient dynamoDbClient;


    @Override
    public GenericResponse handleRequest(PersonRequest personRequest, Context context) {
        initDynamoDbClient();
        PutItemResponse putItemResponse = persistPersonInfo(personRequest, dynamoDbClient);
        System.out.println("Status : " + putItemResponse.sdkHttpResponse().statusCode());

        System.out.println("Consumed Capacity : " + putItemResponse.sdkHttpResponse().isSuccessful());

        putItemResponse.attributes().forEach((key, value) -> System.out.println(key + " " + value));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GenericResponse(putItemResponse.sdkHttpResponse().statusText().get(), headers, 200);

    }

    public GenericResponse handleGetRequest(PersonRequest personRequest, Context context) {
        initDynamoDbClient();

        HashMap<String, AttributeValue> keyToGet = new HashMap<>();

        keyToGet.put("personId", AttributeValue.builder().s(personRequest.getPersonId()).build());
        // Create a GetItemRequest instance
        GetItemRequest request = GetItemRequest.builder().tableName("Person").key(keyToGet).build();

        // Invoke the DynamoDbAsyncClient object's getItem
        GetItemResponse item = dynamoDbClient.getItem(request);

        Map<String, AttributeValue> map = item.item();
        map.forEach((key, value) -> System.out.println(key + " " + value));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GenericResponse(item.sdkHttpResponse().statusText().get(), headers, 200);
    }

    private PutItemResponse persistPersonInfo(PersonRequest personRequest, DynamoDbClient dynamoDbClient) {
        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("personId", AttributeValue.builder().s(personRequest.getPersonId()).build());
        itemValues.put("name", AttributeValue.builder().s(personRequest.getName()).build());
        itemValues.put("age", AttributeValue.builder().n(personRequest.getAge().toString()).build());
        itemValues.put("gender", AttributeValue.builder().s(personRequest.getGender()).build());
        itemValues.put("address", AttributeValue.builder().s(personRequest.getAddress()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder().tableName("Person").item(itemValues).build();
        return dynamoDbClient.putItem(putItemRequest);
    }

    private void initDynamoDbClient() {
        this.dynamoDbClient = DynamoDbClient.builder()
//                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.AP_SOUTH_1)
                .build();
    }
}
