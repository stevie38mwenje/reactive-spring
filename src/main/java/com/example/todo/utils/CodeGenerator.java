package com.example.todo.utils;

import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeGenerator {
    public String generateRandomAlphanumeric() {
        return RandomStringUtils.randomAlphanumeric(4).toUpperCase();
    }
}
