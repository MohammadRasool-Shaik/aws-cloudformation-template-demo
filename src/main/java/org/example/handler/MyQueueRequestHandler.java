package org.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class MyQueueRequestHandler implements RequestHandler<Map<String, Object>, String> {
    @Override
    public String handleRequest(Map<String, Object> stringObjectMap, Context context) {
        System.out.println("======================================================");
        stringObjectMap.forEach((key, value) -> {
            System.out.println(key + "==================================================" + value);
        });
        return "{ \"message\": \"hello world\"}";
    }
}
