package com.example.petstore.utils;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

@Component
public class EnumUtils {

    public <T extends Enum<?>> T getRandomEnumValue(Class<T> clazz) {
        var random = RandomUtils.nextInt(0, clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[random];
    }
}
