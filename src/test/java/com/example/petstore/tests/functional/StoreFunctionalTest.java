package com.example.petstore.tests.functional;

import com.example.petstore.steps.StoreSteps;
import com.example.petstore.tests.BeforeTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigInteger;

public class StoreFunctionalTest extends BeforeTests {

    @Autowired
    private StoreSteps storeSteps;


    @Test(description = "Verify that pet order can be placed successfully")
    void verifyThatCreatePetOrderCreatedSuccessfully() {
        storeSteps
                .createOrderForPetRandom()
                .verifyThatPetOrderIsSuccessfullyPlaced();
    }

    @Test(description = "Verify that pet order can be placed successfully with missed 'id' " +
            "of the order in payload and 'id' is generated")
    void verifyThatIdIsGeneratedIfMissedInPayload() {
        storeSteps
                .createOrderForPetWithMissedIdRandom()
                .verifyThatPetOrderIsSuccessfullyPlacedWithGeneratedId();
    }

    @Test(description = "Verify that order can be fetched successfully by id")
    void verifyThatGetOrderByIdIsFetchedSuccessfully() {
        storeSteps
                .createOrderForPetRandom()
                .getCreatedPurchaseByOrderId()
                .verifyThatPetOrderIsSuccessfullyFetched();
    }

    @DataProvider
    Object[][] getRangeOfInvalidPerOrderIdArray() {
        return new Object[][] {{BigInteger.valueOf(-1)}, {BigInteger.valueOf(0)}, {BigInteger.valueOf(11)}};
    }

//    Please note that this test is failed due to defect, since under the swagger documentation: Valid id range is >= 1 and <= 10
    @Test(dataProvider = "getRangeOfInvalidPerOrderIdArray",
            description = "Verify that for invalid integer IDs with value < 1 and > 10 return 400 response status")
    void verifyThatGetOrderByInvalidIdReturns400(BigInteger id) {
        storeSteps
                .getPurchaseByOrderIdResponse(id)
                .verifyThatOrderIsNotFetchedResponse400(id);
    }

//    Please note that this test is failed due to defect, since 'code' is always 1 and not reflect actual response status code
    @Test(description = "Verify that order is deleted by call DELETE /store/order/{orderId}")
    void verifyThatOrderIsDeletedSuccessfully() {
        storeSteps
                .createOrderForPetRandom()
                .deleteCreatedPurchaseForPet()
                .getCreatedPurchaseByOrderIdResponse()
                .verifyThatPetOrderIsDeleted();
    }
}
