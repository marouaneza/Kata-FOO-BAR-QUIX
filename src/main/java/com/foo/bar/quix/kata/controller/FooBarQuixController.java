package com.foo.bar.quix.kata.controller;

import com.foo.bar.quix.kata.service.FooBarQuixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transform")
public class FooBarQuixController {

    private final FooBarQuixService fooBarQuixService;

    public FooBarQuixController(FooBarQuixService fooBarQuixService) {
        this.fooBarQuixService = fooBarQuixService;
    }

    @GetMapping("/{number}")
    public ResponseEntity<String> transform(@PathVariable int number){
        return ResponseEntity.ok(fooBarQuixService.transform(number));
    }
}
