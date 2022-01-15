package org.example.util;

import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;

public class DynamoDBHelper {
    public static AttributeValueUpdate attributeStringValueUpdate(String value) {
        return AttributeValueUpdate.builder().value(attributeStringValue(value)).action(AttributeAction.PUT).build();
    }

    public static AttributeValueUpdate attributeNumberValueUpdate(Integer value) {
        return AttributeValueUpdate.builder().value(attributeNumberValue(value)).action(AttributeAction.PUT).build();
    }

    public static AttributeValue attributeStringValue(String value) {
        return AttributeValue.builder().s(value).build();
    }

    public static AttributeValue attributeNumberValue(Integer value) {
        return AttributeValue.builder().n(value.toString()).build();
    }

    public static PutItemRequest getPullItemRequest(HashMap<String, AttributeValue> itemValues, String tableName) {
        return PutItemRequest.builder().tableName(tableName).item(itemValues).build();
    }
}
