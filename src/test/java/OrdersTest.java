import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierSteps;
import ru.yandex.praktikum.model.pojo.OrdersList;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrdersTest {
    private CourierSteps courierSteps;
    private OrdersList ordersList;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
        ordersList = new OrdersList();
    }

    @Test
    @DisplayName("Get orders list")
    public void getOrdersList() {
        Response orderListResponse = courierSteps.orderList();
        ordersList = orderListResponse.getBody().as(OrdersList.class);

        assertThat(ordersList, notNullValue());
    }
}