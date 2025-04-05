package com.example.petstore.steps;

import com.example.petstore.config.TestContext;
import com.example.petstore.utils.ResponseSpecificationUtils;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;
import org.assertj.core.api.Assertions;


public abstract class CommonSteps {

    private final ResponseSpecificationUtils responseSpecificationUtils;
    @Getter
    private final TestContext testContext;
    ;

    protected CommonSteps(ResponseSpecificationUtils responseSpecificationUtils, TestContext testContext) {
        this.responseSpecificationUtils = responseSpecificationUtils;
        this.testContext = testContext;
    }

    protected Response verifyResponseSchema(String schemaPath, String schemaName, int responseCode) {
        return verifyResponseSchema(responseSpecificationUtils.responseSpecification(schemaPath, schemaName, responseCode));
    }

    private Response verifyResponseSchema(ResponseSpecification responseSpecification) {
        getTestContext()
                .getLatestResponse()
                .then()
                .log()
                .ifValidationFails()
                .spec(responseSpecification);
        return getTestContext().getLatestResponse();
    }

    public abstract Response verifyResponseSchema(int responseCode, String schemaFileName);

    public void checkResponseStatus(int expectedResponseStatus, Response response) {
        Assertions.assertThat(response.statusCode())
                .as("Response body: \n" + response.getBody().asString())
                .isEqualTo(expectedResponseStatus);
    }
}
