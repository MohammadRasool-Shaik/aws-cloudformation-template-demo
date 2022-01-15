package org.example.handler.person;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;
import org.example.model.GenericResponse;
import org.example.model.PersonRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.HashMap;
import java.util.Map;

import static org.example.util.DynamoDBHelper.attributeNumberValue;
import static org.example.util.DynamoDBHelper.attributeStringValue;
import static org.example.util.DynamoDBHelper.getPullItemRequest;

public class CreatePersonHandler implements RequestHandler<PersonRequest, GenericResponse> {
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

    private PutItemResponse persistPersonInfo(PersonRequest personRequest, DynamoDbClient dynamoDbClient) {
        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("personId", attributeStringValue(personRequest.getPersonId()));
        itemValues.put("name", attributeStringValue(personRequest.getName()));
        itemValues.put("age", attributeNumberValue(personRequest.getAge()));
        itemValues.put("gender", attributeStringValue(personRequest.getGender()));
        itemValues.put("address", attributeStringValue(personRequest.getAddress()));
        String tableName = System.getenv("TABLE_NAME");
        PutItemRequest putItemRequest = getPullItemRequest(itemValues, StringUtils.isNullOrEmpty(tableName) ? "Person" : tableName);
        return dynamoDbClient.putItem(putItemRequest);
    }

    private void initDynamoDbClient() {
        this.dynamoDbClient = DynamoDbClient.builder().region(Region.AP_SOUTH_1).build();
    }
}
