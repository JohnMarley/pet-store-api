package com.example.petstore.utils;

import com.example.petstore.config.Environment;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ResponseSpecificationUtils {

    @Getter
    private final long defaultResponseTime;
    public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    public ResponseSpecificationUtils(Environment environment) {
        defaultResponseTime = environment.getDefaultApiResponseTime();
    }


    public ResponseSpecification responseSpecification(String schemaPath, String schemaName, int statusCode) {
        return responseSpecification(schemaPath + FILE_SEPARATOR + schemaName, getDefaultResponseTime(), statusCode);
    }

    public ResponseSpecification responseSpecification(String schemaPath, Long responseTime, int statusCode) {
        return new ResponseSpecBuilder()
                .addResponseSpecification(bodySchemaSpecification(schemaPath))
                .addResponseSpecification(responseTimeSpecification(responseTime))
                .expectStatusCode(statusCode)
                .expectContentType(ContentType.JSON)
                .build();
    }

    private ResponseSpecification bodySchemaSpecification(String schemaPath) {
        try {
            return new ResponseSpecBuilder()
                    .expectBody(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath))
                    .build();
        } catch (Exception e) {
            log.error("json schema is not found: {}", schemaPath);
            throw new RuntimeException(e);
        }
    }

    private ResponseSpecification responseTimeSpecification(Long responseTime) {
        return new ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan(responseTime), TimeUnit.MILLISECONDS)
                .build();
    }
}
