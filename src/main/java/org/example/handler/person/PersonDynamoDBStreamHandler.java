package org.example.handler.person;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class PersonDynamoDBStreamHandler implements RequestHandler<Map<String, Object>, String> {
    @Override
    public String handleRequest(Map<String, Object> stringObjectMap, Context context) {
        stringObjectMap.forEach((key, value) -> {
            System.out.println(key + "=================================" + value);
        });
        return null;
    }
}
