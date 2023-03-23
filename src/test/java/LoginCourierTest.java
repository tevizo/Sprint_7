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
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
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
    public void courierCanLogin() {
        Courier courier = CourierGenerator.getRandom();
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierId = courierSteps.login(CourierCredentials.from(courier)).assertThat().statusCode(HTTP_OK).body("id", notNullValue()).extract().path("id");
    }

    @Test
    public void courierLoginWithAllValues() {
        Courier courier = CourierGenerator.getRandom();
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierId = courierSteps.login(CourierCredentials.from(courier)).assertThat().statusCode(HTTP_OK).body("id", notNullValue()).extract().path("id");
    }
    @Test
    public void errorIfIncorrectPassword() {
        Courier courier = new Courier("Tesonwdveiuvb", "rwthwrth", "wrthwrth45");
        CourierCredentials courierCredentials = new CourierCredentials("Tesonwdveiuvb", "test");
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierSteps.login(courierCredentials).assertThat().statusCode(HTTP_NOT_FOUND).body("message", is("Учетная запись не найдена"));
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");

    }
    @Test
    public void errorIfLoginValueIsNull() {
        Courier courier = new Courier("ThisWillBeNull", "password", "wrthwrth45");
        CourierCredentials courierCredentials = new CourierCredentials(null, "password");
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierSteps.login(courierCredentials).assertThat().statusCode(HTTP_BAD_REQUEST).body("message", is("Недостаточно данных для входа"));
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
    }
    @Test
    public void errorIfNonExistingCourier() {
        Courier courier = new Courier("ThisWillBeNew", "password", "wrthwrth45");
        CourierCredentials courierCredentials = new CourierCredentials("ThisWillBeNew", "password");
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierId = courierSteps.login(courierCredentials).extract().path("id");
        courierSteps.delete(courierId);
        courierSteps.login(courierCredentials).assertThat().statusCode(HTTP_NOT_FOUND).and().body("message", is("Учетная запись не найдена"));;
    }

    @Test
    public void successRequestReturnsId() {
        Courier courier = new Courier("Testik01", "rwthw4rth", "wrthwrth45");
        CourierCredentials courierCredentials = new CourierCredentials("Testik01", "rwthw4rth");
        courierSteps.create(courier).assertThat().statusCode(HTTP_CREATED);
        courierId = courierSteps.login(courierCredentials).assertThat().statusCode(HTTP_OK).and().body("id", notNullValue()).extract().path("id");
    }

}
