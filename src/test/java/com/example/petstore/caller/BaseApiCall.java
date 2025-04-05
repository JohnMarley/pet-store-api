package com.example.petstore.caller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static org.apache.commons.collections4.MapUtils.emptyIfNull;


@Slf4j
@Component
public class BaseApiCall {

    private final String host;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public BaseApiCall(@Value("${api.petstore.server.url}") String host) {
        this.host = host;
    }


    public Response makeGetCall(String uri, Map<String, String> requestPathParametersMap) {
        RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation()
                .redirects()
                .follow(false);
        emptyIfNull(requestPathParametersMap).forEach(requestSpecification::pathParam);
        var response = requestSpecification
                .get(host + uri);
        logRequestAndResponseDetails(((QueryableRequestSpecification) requestSpecification).getURI(), Methods.GET, response);
        return response;
    }

    public Response makeGetCall(String uri) {
        return this.makeGetCall(uri, null);
    }

    public Response makePostCall(String uri, final Object payload) {
        RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation()
                .contentType(ContentType.JSON);
        var response = requestSpecification
                .body(from(payload))
                .post(host + uri);
        logRequestAndResponseDetails(((QueryableRequestSpecification) requestSpecification).getURI(), Methods.POST, payload, response);
        return response;
    }

    public Response makeDeleteCall(String uri, Map<String, String> requestPathParametersMap) {
        RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation();
        emptyIfNull(requestPathParametersMap).forEach(requestSpecification::pathParam);
        var response = requestSpecification
                .delete(host + uri);
        logRequestAndResponseDetails(((QueryableRequestSpecification) requestSpecification).getURI(), Methods.DELETE, response);
        return response;
    }

    private String from(Object payload) {
        var body = "";
        try {
            body = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return body;
    }

    /**
     * RestAssured logging like method().log().uri().log()
     * is not suitable for multithreading. It makes mess with logs.
     * Need to use logback-classic to log each request-response in single thread
     * like Thread-1 Mon 13:01:000 BaseApiClass:
     *
     * @param uri - request uri
     * @param method - request method
     * @param response - api call response
     */
    private void logRequestAndResponseDetails(String uri, Methods method, Response response) {
        logRequestAndResponseDetails(uri, method, null, response);
    }

    private void logRequestAndResponseDetails(String uri, Methods method, Object payload, Response response) {
        try {
            var requestLogMessage =
                    "\nRequest URI: " + uri +
                            "\nrequest method: " + method;
            if (Objects.nonNull(payload)) {
                requestLogMessage += "\nBody:\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            }
            var responseLogMessage =
                    "\n" + response.getStatusLine() +
                            "\n" + response.asPrettyString();
            log.info("{}{}", requestLogMessage, responseLogMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private enum Methods {
        POST,
        GET,
        PUT,
        PATCH,
        DELETE
    }
}
