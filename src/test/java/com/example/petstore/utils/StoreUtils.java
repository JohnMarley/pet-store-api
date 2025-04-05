package com.example.petstore.utils;

import com.example.petstore.caller.StoreCall;
import com.example.petstore.model.PetOrderPayloadDto;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
public class StoreUtils {

    private final StoreCall storeCall;

    public StoreUtils(StoreCall storeCall) {
        this.storeCall = storeCall;
    }


    public Response getPetInventoriesByStatusResponse() {
        return storeCall.getPetInventoriesByStatus();
    }

    public Response createOrderForPetResponse(PetOrderPayloadDto payload) {
        return storeCall.createOrderForPet(payload);
    }

    public Response getPurchaseByOrderIdResponse(int orderId) {
        return storeCall.getPurchaseByOrderId(orderId);
    }

    public Response deletePurchaseByOrderIdResponse(int orderId) {
        return storeCall.deletePurchaseByOrderId(orderId);
    }
}
