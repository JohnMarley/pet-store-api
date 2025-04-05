package com.example.petstore.config;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestContext {

    private final ThreadLocal<Map<String, Object>> mapData = ThreadLocal.withInitial(HashMap::new);
    private static final String RESPONSE = "response";

    public void setLatestResponse(Response response) {
        mapData.get().put(RESPONSE, response);
    }

    public Response getLatestResponse() {
        return (Response) mapData.get().get(RESPONSE);
    }

    public void setContext(String key, Object value) {
        mapData.get().put(key, value);
    }

    public Object getContext(String key) {
        return mapData.get().get(key);
    }
}
