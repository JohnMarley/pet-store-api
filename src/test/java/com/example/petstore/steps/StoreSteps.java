package com.example.petstore.steps;

import com.example.petstore.config.TestContext;
import com.example.petstore.model.PetOrderResponseDto;
import com.example.petstore.testdataproviders.PetStoreDataProvider;
import com.example.petstore.utils.ResponseSpecificationUtils;
import com.example.petstore.utils.StoreUtils;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class StoreSteps extends CommonSteps {

    private final StoreUtils storeUtils;
    private final PetStoreDataProvider petStoreDataProvider;
    private static final String SCHEMA_PATH = String.join(ResponseSpecificationUtils.FILE_SEPARATOR, "jsonschemas", "petstore", "store");
    private static final String ORDER_FOR_PET_RESPONSE_DTO = "orderForPetDto";

    public StoreSteps(
            StoreUtils storeUtils,
            PetStoreDataProvider petStoreDataProvider,
            ResponseSpecificationUtils responseSpecificationUtils,
            TestContext testContext) {
        super(responseSpecificationUtils, testContext);
        this.storeUtils = storeUtils;
        this.petStoreDataProvider = petStoreDataProvider;
    }


    @Override
    public Response verifyResponseSchema(int responseCode, String schemaFileName) {
        return super.verifyResponseSchema(SCHEMA_PATH, schemaFileName, responseCode);
    }

    public StoreSteps getPetInventoriesByStatusResponse() {
        var response = storeUtils.getPetInventoriesByStatusResponse();
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps createOrderForPetRandomResponse() {
        var petOrderPayload = petStoreDataProvider.generatePetOrderRandomPayload();
        var response = storeUtils.createOrderForPetResponse(petOrderPayload);
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps getCreatedPurchaseByOrderIdResponse() {
        var orderForPetDto = (PetOrderResponseDto) getTestContext().getContext(ORDER_FOR_PET_RESPONSE_DTO);
        return getPurchaseByOrderIdResponse(orderForPetDto.getId().intValue());
    }

    public StoreSteps getPurchaseByOrderIdResponse(int orderId) {
        var response = storeUtils.getPurchaseByOrderIdResponse(orderId);
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps deletePurchaseForPetByIdResponse(int orderId) {
        var response = storeUtils.deletePurchaseByOrderIdResponse(orderId);
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps deleteCreatedPurchaseForPetResponse() {
        var orderForPetDto = (PetOrderResponseDto) getTestContext().getContext(ORDER_FOR_PET_RESPONSE_DTO);
        var response = storeUtils.deletePurchaseByOrderIdResponse(orderForPetDto.getId().intValue());
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps createOrderForPetRandom() {
        var petOrderPayload = petStoreDataProvider
                .generatePetOrderWithIdPayload(BigInteger.valueOf(RandomUtils.nextInt(1, 10)));
        var response = storeUtils.createOrderForPetResponse(petOrderPayload);
        checkResponseStatus(HttpStatus.SC_OK, response);
        var orderForPetDto = response.as(PetOrderResponseDto.class);
        getTestContext().setContext(ORDER_FOR_PET_RESPONSE_DTO, orderForPetDto);
        return this;
    }
}
