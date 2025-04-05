package com.example.petstore.caller;

import com.example.petstore.model.PetOrderPayloadDto;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Map;

@Component
public class StoreCall {

    private final BaseApiCall baseApiCall;

    private static final String GET_PET_INVENTORIES_BY_STATUS = "/store/inventory";
    private static final String CREATE_ORDER_FOR_PET = "/store/order";
    private static final String GET_PURCHASE_ORDER_BY_ID = "/store/order/{orderId}";
    private static final String DELETE_PURCHASE_ORDER_BY_ID = GET_PURCHASE_ORDER_BY_ID;

    public StoreCall(BaseApiCall baseApiCall) {
        this.baseApiCall = baseApiCall;
    }


    public Response getPetInventoriesByStatus() {
        return baseApiCall.makeGetCall(GET_PET_INVENTORIES_BY_STATUS);
    }

    public Response createOrderForPet(PetOrderPayloadDto petOrderPayloadDto) {
        return baseApiCall.makePostCall(CREATE_ORDER_FOR_PET, petOrderPayloadDto);
    }

    public Response getPurchaseByOrderId(BigInteger orderId) {
        return baseApiCall.makeGetCall(GET_PURCHASE_ORDER_BY_ID, Map.of("orderId", orderId.toString()));
    }

    public Response deletePurchaseByOrderId(int orderId) {
        return baseApiCall.makeDeleteCall(DELETE_PURCHASE_ORDER_BY_ID, Map.of("orderId", String.valueOf(orderId)));
    }
}
