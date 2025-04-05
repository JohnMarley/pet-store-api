package com.example.petstore.tests;

import com.example.petstore.config.SpringConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(classes = {SpringConfig.class})
public class BeforeTests extends AbstractTestNGSpringContextTests {
}
