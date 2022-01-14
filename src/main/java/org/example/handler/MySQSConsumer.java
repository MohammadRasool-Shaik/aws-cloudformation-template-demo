package org.example.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class MySQSConsumer implements RequestHandler<Object, Object> {
    @Override
    public Object handleRequest(Object o, Context context) {
        return null;
    }
}
