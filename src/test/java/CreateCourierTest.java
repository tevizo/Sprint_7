import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierSteps;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierCredentials;
import ru.yandex.praktikum.model.CourierGenerator;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateCourierTest {

    private CourierSteps courierSteps;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
    }

    @After
    public void clearData() {
        courierSteps.delete(courierId);
    }

    @Test
    public void courierCanBeCreated() {
        Courier courier = CourierGenerator.getRandom();
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");//строчка будет использована для передачи id в метод delete
    }

    @Test
    public void courierCannotBeCreatedTwice() {
        Courier courier = CourierGenerator.getRandom();
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierSteps.create(courier).assertThat().statusCode(HTTP_CONFLICT);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void courierIsNotCreatedWithAllValues() {
        Courier courier = new Courier("ninjatest", "1234", "saske");
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void requestReturnsCorrectStatusCode() {
        Courier courier = CourierGenerator.getRandom();
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void successRequestReturnsCorrectValue() {
        Courier courier = CourierGenerator.getRandom();
        courierSteps.create(courier).assertThat().body("ok", is(true));
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void courierCannotBeCreatedWithoutPassword() {
        Courier courier = new Courier(null, "rwthwrth", "wrthwrth45");
        courierSteps.create(courier).assertThat().statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    public void courierErrorWhenLoginIsAlreadyUsed() {
        Courier courier = new Courier("LoginToBeUsed12we34586", "test", "Test");
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierSteps.create(new Courier("LoginToBeUsed12we34586", "test2", "Test2")).assertThat().statusCode(HTTP_CONFLICT).and().body("message", is("Этот логин уже используется. Попробуйте другой."));
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
    }
}