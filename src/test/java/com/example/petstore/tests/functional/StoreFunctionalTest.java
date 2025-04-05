package com.example.petstore.tests.functional;

import com.example.petstore.steps.StoreSteps;
import com.example.petstore.tests.BeforeTests;
import org.springframework.beans.factory.annotation.Autowired;

public class StoreFunctionalTest extends BeforeTests {

    @Autowired
    private StoreSteps storeSteps;
}
