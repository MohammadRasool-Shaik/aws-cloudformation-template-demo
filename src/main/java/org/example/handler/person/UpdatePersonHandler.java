package org.example.handler.person;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;
import org.example.model.GenericResponse;
import org.example.model.PersonRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.example.util.DynamoDBHelper.attributeNumberValueUpdate;
import static org.example.util.DynamoDBHelper.attributeStringValue;
import static org.example.util.DynamoDBHelper.attributeStringValueUpdate;

public class UpdatePersonHandler implements RequestHandler<PersonRequest, GenericResponse> {
    private DynamoDbClient dynamoDbClient;

    @Override
    public GenericResponse handleRequest(PersonRequest personRequest, Context context) {
        initDynamoDbClient();
        String tableName = System.getenv("TABLE_NAME");
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder().tableName(StringUtils.isNullOrEmpty(tableName) ? "Person" : tableName)
                .key(Collections.singletonMap("personId", attributeStringValue(personRequest.getPersonId()))).
                attributeUpdates(updatePersonInfo(personRequest)).build();


        UpdateItemResponse updateItemResponse = dynamoDbClient.updateItem(updateItemRequest);
        System.out.println("Status : " + updateItemResponse.sdkHttpResponse().statusCode());

        System.out.println("Consumed Capacity : " + updateItemResponse.sdkHttpResponse().isSuccessful());

        updateItemResponse.attributes().forEach((key, value) -> System.out.println(key + " " + value));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GenericResponse(updateItemResponse.sdkHttpResponse().statusText().get(), headers, 200);
    }

    private Map<String, AttributeValueUpdate> updatePersonInfo(PersonRequest personRequest) {
        Map<String, AttributeValueUpdate> itemValues = new HashMap<>();
        itemValues.put("personId", attributeStringValueUpdate(personRequest.getPersonId()));
        itemValues.put("name", attributeStringValueUpdate(personRequest.getName()));
        itemValues.put("age", attributeNumberValueUpdate(personRequest.getAge()));
        itemValues.put("gender", attributeStringValueUpdate(personRequest.getGender()));
        itemValues.put("address", attributeStringValueUpdate(personRequest.getAddress()));
        return itemValues;
    }


    private void initDynamoDbClient() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1)
                .build();
    }
}
