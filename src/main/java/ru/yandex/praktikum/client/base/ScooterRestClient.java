package ru.yandex.praktikum.client.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ScooterRestClient {
protected static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/api/v1/";
    protected RequestSpecification getBaseReqSpec() {// этот метод заменяет ".header("Content-type", "application/json")" во всех тестах
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}

