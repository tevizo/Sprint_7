import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierSteps;
import ru.yandex.praktikum.model.Pojo.OrdersList;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrdersTest {
    private CourierSteps courierSteps;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
    }

    @Test
    public void getOrdersList() {
        OrdersList ordersList = courierSteps.orderList().body().as(OrdersList.class);
        assertThat(ordersList, notNullValue());

    }
}