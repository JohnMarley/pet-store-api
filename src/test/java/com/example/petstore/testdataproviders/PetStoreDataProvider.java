package com.example.petstore.testdataproviders;

import com.example.petstore.enums.OrderStatus;
import com.example.petstore.model.PetOrderPayloadDto;
import com.example.petstore.utils.EnumUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Objects;

@Component
public class PetStoreDataProvider {

    private final EnumUtils enumUtils;

    public PetStoreDataProvider(EnumUtils enumUtils) {
        this.enumUtils = enumUtils;
    }

    public PetOrderPayloadDto generatePetOrderWithIdPayload(BigInteger id) {
        var result = PetOrderPayloadDto.builder()
                .petId(BigInteger.valueOf(RandomUtils.nextInt()))
                .quantity(RandomUtils.nextInt())
                .shipDate(OffsetDateTime.now())
                .status(enumUtils.getRandomEnumValue(OrderStatus.class))
                .complete(true)
                .build();
        if (Objects.nonNull(id)) {
            result.setId(id);
        }
        return result;
    }

    public PetOrderPayloadDto generatePetOrderRandomPayload() {
        return generatePetOrderWithIdPayload(null);
    }
}
