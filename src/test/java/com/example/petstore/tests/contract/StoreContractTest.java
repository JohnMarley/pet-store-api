package com.example.petstore.tests.contract;

import com.example.petstore.steps.StoreSteps;
import com.example.petstore.tests.BeforeTests;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class StoreContractTest extends BeforeTests {

    @Autowired
    private StoreSteps storeSteps;

    @Test(description = "Verify GET /store/inventory api contract 200 response")
    void verifyGetPetInventoriesByStatusSchema200() {
        storeSteps
                .getPetInventoriesByStatusResponse()
                .verifyResponseSchema(HttpStatus.SC_OK, "getStoreInventoryByStatusGET200.json");
    }

    @Test(description = "Verify POST /store/order api contract 200 response")
    void verifyCreatePetOrderSchema201() {
        storeSteps
                .createOrderForPetRandomResponse()
                .verifyResponseSchema(HttpStatus.SC_OK, "createPetOrderPOST200.json");
    }

    @Test(description = "Verify GET /store/order/{orderId} api contract 200 response")
    void verifyGetOrderByIdSchema200() {
        storeSteps
                .createOrderForPetRandom()
                .getCreatedPurchaseByOrderIdResponse()
                .verifyResponseSchema(HttpStatus.SC_OK, "getOrderByIdGET200.json");
    }

    @Test(description = "Verify GET /store/order/{orderId} api contract 404 response. " +
            "In the swagger documentation 404 response is not documented, " +
            "so test was written based on actually implemented 404 response schema")
    void verifyGetOrderByIdSchema404() {
        storeSteps
                .getPurchaseByOrderIdResponse(142)
                .verifyResponseSchema(HttpStatus.SC_NOT_FOUND, "getOrderByIdGET404.json");
    }

    @Test(description = "Verify DELETE /store/order/{orderId} api contract 200 response. " +
            "In the swagger documentation 200 response is not documented, " +
            "so test was written based on actually implemented 200 response schema")
    void verifyDeleteOrderByIdSchema200() {
        storeSteps
                .createOrderForPetRandom()
                .deleteCreatedPurchaseForPetResponse()
                .verifyResponseSchema(HttpStatus.SC_OK, "deleteOrderByIdDELETE200.json");
    }

    @Test(description = "Verify DELETE /store/order/{orderId} api contract 404 response")
    void verifyDeleteOrderByIdSchema404() {
        storeSteps
                .deletePurchaseForPetByIdResponse(130)
                .verifyResponseSchema(HttpStatus.SC_NOT_FOUND, "deleteOrderByIdDELETE404.json");
    }
}
