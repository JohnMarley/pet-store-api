package com.example.petstore.steps;

import com.example.petstore.config.TestContext;
import com.example.petstore.model.General404ResponseDto;
import com.example.petstore.model.PetOrderPayloadDto;
import com.example.petstore.model.PetOrderResponseDto;
import com.example.petstore.testdataproviders.PetStoreDataProvider;
import com.example.petstore.utils.ResponseSpecificationUtils;
import com.example.petstore.utils.StoreUtils;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class StoreSteps extends CommonSteps {

    private final StoreUtils storeUtils;
    private final PetStoreDataProvider petStoreDataProvider;
    private static final String SCHEMA_PATH = String.join(ResponseSpecificationUtils.FILE_SEPARATOR, "jsonschemas", "petstore", "store");
    private static final String ORDER_FOR_PET_PAYLOAD_DTO = "orderForPetPayloadDto";
    private static final String ORDER_FOR_PET_CREATED_RESPONSE_DTO = "orderForPetCreatedResponseDto";
    private static final String ORDER_FOR_PET_GET_RESPONSE_DTO = "orderForPetGetResponseDto";

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
        var orderForPetDto = getCreatedPetOrder();
        return getPurchaseByOrderIdResponse(orderForPetDto.getId());
    }

    public StoreSteps getPurchaseByOrderIdResponse(BigInteger orderId) {
        var response = storeUtils.getPurchaseByOrderIdResponse(orderId);
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps getCreatedPurchaseByOrderId() {
        var orderForPetDto = getCreatedPetOrder();
        getCreatedPurchaseByOrderIdResponse();
        var response = getTestContext().getLatestResponse();
        checkResponseStatus(HttpStatus.SC_OK, response, "created PurchaseOrder with id=" + orderForPetDto.getId() + " not found");
        var petOrderDto = response.as(PetOrderResponseDto.class);
        getTestContext().setContext(ORDER_FOR_PET_GET_RESPONSE_DTO, petOrderDto);
        return this;
    }

    public StoreSteps deletePurchaseForPetByIdResponse(int orderId) {
        var response = storeUtils.deletePurchaseByOrderIdResponse(orderId);
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps deleteCreatedPurchaseForPet() {
        var orderForPetDto = getCreatedPetOrder();
        deleteCreatedPurchaseForPetResponse();
        var response = getTestContext().getLatestResponse();
        checkResponseStatus(HttpStatus.SC_OK, response, "previously created perOrder with id=" + orderForPetDto.getId() + " is not found");
        return this;
    }

    public StoreSteps deleteCreatedPurchaseForPetResponse() {
        var orderForPetDto = getCreatedPetOrder();
        var response = storeUtils.deletePurchaseByOrderIdResponse(orderForPetDto.getId().intValue());
        getTestContext().setLatestResponse(response);
        return this;
    }

    public StoreSteps createOrderForPetRandom() {
        var petOrderPayload = petStoreDataProvider
                .generatePetOrderWithIdPayload(BigInteger.valueOf(RandomUtils.nextInt(1, 10)));
        createOrderForPet(petOrderPayload);
        return this;
    }

    public StoreSteps createOrderForPetWithMissedIdRandom() {
        var petOrderPayload = petStoreDataProvider.generatePetOrderRandomPayload();
        createOrderForPet(petOrderPayload);
        return this;
    }

    public StoreSteps verifyThatPetOrderIsSuccessfullyPlaced() {
        var petOrderPayload = getPetOrderPayload();
        var orderForPetResponseDto = getCreatedPetOrder();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderForPetResponseDto.getId())
                    .as("Pet order 'id' does not match expected")
                    .isEqualTo(petOrderPayload.getId());
            softAssertions.assertThat(orderForPetResponseDto.getPetId())
                    .as("Pet order 'petId' does not match expected")
                    .isEqualTo(petOrderPayload.getPetId());
            softAssertions.assertThat(orderForPetResponseDto.getQuantity())
                    .as("Pet order 'quantity' does not match expected")
                    .isEqualTo(petOrderPayload.getQuantity());
            softAssertions.assertThat(orderForPetResponseDto.getShipDate())
                    .as("Pet order 'shipDate' does not match expected")
                    .isCloseTo(petOrderPayload.getShipDate(), Assertions.within(1, ChronoUnit.MILLIS));
            softAssertions.assertThat(orderForPetResponseDto.getStatus())
                    .as("Pet order 'status' does not match expected")
                    .isEqualTo(petOrderPayload.getStatus());
            softAssertions.assertThat(orderForPetResponseDto.isComplete())
                    .as("Pet order 'complete' does not match expected")
                    .isEqualTo(petOrderPayload.isComplete());
        });
        return this;
    }

    public StoreSteps verifyThatPetOrderIsSuccessfullyPlacedWithGeneratedId() {
        var petOrderPayload = getPetOrderPayload();
        var orderForPetResponseDto = getCreatedPetOrder();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderForPetResponseDto.getId())
                    .as("Pet order 'id' is generated")
                    .isNotNull();
            softAssertions.assertThat(orderForPetResponseDto.getPetId())
                    .as("Pet order 'petId' does not match expected")
                    .isEqualTo(petOrderPayload.getPetId());
            softAssertions.assertThat(orderForPetResponseDto.getQuantity())
                    .as("Pet order 'quantity' does not match expected for placedOrderId=%s", petOrderPayload.getId())
                    .isEqualTo(petOrderPayload.getQuantity());
            softAssertions.assertThat(orderForPetResponseDto.getShipDate())
                    .as("Pet order 'shipDate' does not match expected for placedOrderId=%s", petOrderPayload.getId())
                    .isCloseTo(petOrderPayload.getShipDate(), Assertions.within(1, ChronoUnit.MILLIS));
            softAssertions.assertThat(orderForPetResponseDto.getStatus())
                    .as("Pet order 'status' does not match expected for placedOrderId=%s", petOrderPayload.getId())
                    .isEqualTo(petOrderPayload.getStatus());
            softAssertions.assertThat(orderForPetResponseDto.isComplete())
                    .as("Pet order 'complete' does not match expected for placedOrderId=%s", petOrderPayload.getId())
                    .isEqualTo(petOrderPayload.isComplete());
        });
        return this;
    }

    public StoreSteps verifyThatPetOrderIsSuccessfullyFetched() {
        var fetchedPetOrder = (PetOrderResponseDto) getTestContext().getContext(ORDER_FOR_PET_GET_RESPONSE_DTO);
        var placedPetOrder = getCreatedPetOrder();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(fetchedPetOrder.getId())
                    .as("Pet order 'id' does not match expected")
                    .isEqualTo(placedPetOrder.getId());
            softAssertions.assertThat(fetchedPetOrder.getPetId())
                    .as("Pet order 'petId' does not match expected for placedOrderId=%s", placedPetOrder.getId())
                    .isEqualTo(placedPetOrder.getPetId());
            softAssertions.assertThat(fetchedPetOrder.getQuantity())
                    .as("Pet order 'quantity' does not match expected for placedOrderId=%s", placedPetOrder.getId())
                    .isEqualTo(placedPetOrder.getQuantity());
            softAssertions.assertThat(fetchedPetOrder.getShipDate())
                    .as("Pet order 'shipDate' does not match expected for placedOrderId=%s", placedPetOrder.getId())
                    .isCloseTo(placedPetOrder.getShipDate(), Assertions.within(1, ChronoUnit.MILLIS));
            softAssertions.assertThat(fetchedPetOrder.getStatus())
                    .as("Pet order 'status' does not match expected for placedOrderId=%s", placedPetOrder.getId())
                    .isEqualTo(placedPetOrder.getStatus());
            softAssertions.assertThat(fetchedPetOrder.isComplete())
                    .as("Pet order 'complete' does not match expected for placedOrderId=%s", placedPetOrder.getId())
                    .isEqualTo(placedPetOrder.isComplete());
        });
        return this;
    }

    public StoreSteps verifyThatOrderIsNotFetchedResponse400(BigInteger id) {
        Assertions.assertThat(getTestContext().getLatestResponse().getStatusCode())
                .as("Valid id range is >= 1 and <= 10. PetOrder should not be fetched by invalid id=%s", id)
                .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        return this;
    }

    public StoreSteps verifyThatPetOrderIsDeleted() {
        var orderForPetDto = getCreatedPetOrder();
        var response = getTestContext().getLatestResponse();
        checkResponseStatus(HttpStatus.SC_NOT_FOUND, response, "Deleted order with id=" + orderForPetDto.getId() + " is available");
        var responseDto = response.as(General404ResponseDto.class);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(responseDto.getCode())
                    .as("Status code does not match expected")
                    .isEqualTo(HttpStatus.SC_NOT_FOUND);
            softAssertions.assertThat(responseDto.getType())
                    .as("Type does not match expected")
                    .isEqualTo("error");
            softAssertions.assertThat(responseDto.getMessage())
                    .as("Message does not match expected")
                    .isEqualTo("Order not found");
        });
        return this;
    }

    /**
     * Fetches existing petOrderDto from testContext
     * @return PetOrderResponseDto object
     * @throws IllegalArgumentException if no petOrderDto object exist in testContext
     */
    private PetOrderResponseDto getCreatedPetOrder() {
        var orderForPetDto = (PetOrderResponseDto) getTestContext().getContext(ORDER_FOR_PET_CREATED_RESPONSE_DTO);
        if (Objects.isNull(orderForPetDto)) {
            throw new IllegalArgumentException("orderForPetDto is null. Create an pet order first");
        }
        return orderForPetDto;
    }

    /**
     * Fetches existing petOrderPayload from testContext
     * @return PetOrderPayloadDto object
     * @throws IllegalArgumentException if no petOrderPayload object exist in testContext
     */
    private PetOrderPayloadDto getPetOrderPayload() {
        var petOrderPayload = (PetOrderPayloadDto) getTestContext().getContext(ORDER_FOR_PET_PAYLOAD_DTO);
        if (Objects.isNull(petOrderPayload)) {
            throw new IllegalArgumentException("petOrderPayload is null. Create an pet order first");
        }
        return petOrderPayload;
    }

    private StoreSteps createOrderForPet(PetOrderPayloadDto petOrderPayload) {
        var response = storeUtils.createOrderForPetResponse(petOrderPayload);
        checkResponseStatus(HttpStatus.SC_OK, response);
        var orderForPetDto = response.as(PetOrderResponseDto.class);
        getTestContext().setContext(ORDER_FOR_PET_PAYLOAD_DTO, petOrderPayload);
        getTestContext().setContext(ORDER_FOR_PET_CREATED_RESPONSE_DTO, orderForPetDto);
        return this;
    }
}
