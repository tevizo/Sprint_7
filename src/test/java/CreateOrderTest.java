import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierSteps;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.model.Order;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private CourierSteps courierSteps;
    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters // добавили аннотацию
    public static Object[][] getOrderData() {
        return new Object[][] {
                {new String[] {"GREY"}},
                {new String[] {"GREY", "BLACK"}},
                {new String[] {null}},
        };
    }

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
    }

    @Test
    public void createOrderParam() {
        Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
        courierSteps.createOrder(order).assertThat().statusCode(HTTP_CREATED).body("track", notNullValue());
    }
}
