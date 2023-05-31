package ru.yandex.praktikum.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.ScooterRestClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierCredentials;
import ru.yandex.praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class CourierSteps extends ScooterRestClient {
    private static final String COURIER_URI = BASE_URI + "courier/";
    private static final String ORDER_URI = BASE_URI + "orders";

    @Step("Create courier {createCourier}")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseReqSpec()) //пишем ".spec" вместо ".header" и обращаемся все время к getBaseReqSpec()
                .body(courier)
                .when()
                .post(COURIER_URI)
                .then();
    }

    @Step("Login courier {courierCredentials}")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_URI + "login/")
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()//при удалении в тело запроса ничего не передается (не пишем body)
                .delete(COURIER_URI + id)
                .then();
    }

    @Step("Create order {createOrder}")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Order list {orderList}")
    public Response orderList() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI);
    }
}
