package com.foo.bar.quix.kata.service;

import com.foo.bar.quix.kata.service.impl.FooBarQuixServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FooBarQuixServiceTest {
    FooBarQuixService fooBarQuixService;

    @BeforeEach
    public void setUp(){
        fooBarQuixService = new FooBarQuixServiceImpl();
    }

    @Test
    void testDivisibleBy3() {
        assertEquals("FOO", fooBarQuixService.transform(9));
    }

    @Test
    void testContains5() {
        assertEquals("BARBAR", fooBarQuixService.transform(5));
    }

    @Test
    void testContains7() {
        assertEquals("QUIX", fooBarQuixService.transform(7));
    }

    @Test
    void testNoRule() {
        assertEquals("2", fooBarQuixService.transform(2));
    }
}
