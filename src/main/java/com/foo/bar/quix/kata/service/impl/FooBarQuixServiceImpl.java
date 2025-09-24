package com.foo.bar.quix.kata.service.impl;

import com.foo.bar.quix.kata.service.FooBarQuixService;
import org.springframework.stereotype.Service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FooBarQuixServiceImpl implements FooBarQuixService {

    private static final Logger log = LoggerFactory.getLogger(FooBarQuixServiceImpl.class);

    private static final Map<Character, String> rulesContains = Map.of(
            '3',"FOO",
            '5',"BAR",
            '7',"QUIX"
    );
    @Override
    public String transform(int number) {

        log.info("Start of transforming {}", number);
        StringBuilder result = new StringBuilder();

        //Divisible
        if(number % 3 == 0) result.append("FOO");
        if(number % 5 == 0) result.append("BAR");

        //Contient
        String str = String.valueOf(number);
        str.chars()
                .mapToObj(c -> (char) c)
                .filter(rulesContains::containsKey)
                .forEach(c -> result.append(rulesContains.get(c)));

        return result.length() == 0 ? str : result.toString();
    }
}
