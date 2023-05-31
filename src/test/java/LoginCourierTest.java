import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
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
import static org.hamcrest.MatcherAssert.assertThat;

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
    @DisplayName("Courier can login")
    public void courierCanLogin() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierSteps.create(courier);

        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
        boolean isValueTrue = createResponse.extract().path("ok");

        assertThat("Creating Failed", isValueTrue);
        assertThat(courierId, is(notNullValue()));
    }

    @Test
    @DisplayName("Courier login with all values")
    public void courierLoginWithAllValues() {
        Courier courier = new Courier("Tesonwdveiuvbf", "rwthwrth", "wrthwrth45");
        CourierCredentials courierCredentials = new CourierCredentials("Tesonwdveiuvbf", "rwthwrth");
        ValidatableResponse createResponse = courierSteps.create(courier);

        courierId = courierSteps.login(courierCredentials).extract().path("id");
        boolean isValueTrue = createResponse.extract().path("ok");

        assertThat("Creating Failed", isValueTrue);
        assertThat(courierId, is(notNullValue()));
    }

    @Test
    @DisplayName("Error if incorrect password")
    public void errorIfIncorrectPassword() {
        Courier courier = new Courier("Tesonwdveiuvbm", "rwthwrth", "wrthwrth45");
        CourierCredentials courierCredentials = new CourierCredentials("Tesonwdveiuvbm", "test");
        ValidatableResponse createResponse = courierSteps.create(courier);
        ValidatableResponse loginResponse = courierSteps.login(courierCredentials);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");

        boolean isValueTrue = createResponse.extract().path("ok");
        String messageLoginResponse = loginResponse.extract().path("message").toString();
        int statusCode = loginResponse.extract().statusCode();

        assertThat("Creating Failed", isValueTrue);
        assertThat(messageLoginResponse, is("Учетная запись не найдена"));
        assertThat(statusCode, is(HTTP_NOT_FOUND));
    }

    @Test
    @DisplayName("Error if login value is null")
    public void errorIfLoginValueIsNull() {
        Courier courier = new Courier("ThisWillBeNull", "password", "wrthwrth45");
        CourierCredentials courierCredentials = new CourierCredentials(null, "password");
        ValidatableResponse createResponse = courierSteps.create(courier);
        ValidatableResponse loginResponse = courierSteps.login(courierCredentials);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");

        boolean isValueTrue = createResponse.extract().path("ok");
        String messageLoginResponse = loginResponse.extract().path("message").toString();
        int statusCode = loginResponse.extract().statusCode();

        assertThat("Creating Failed", isValueTrue);
        assertThat(messageLoginResponse, is("Недостаточно данных для входа"));
        assertThat(statusCode, is(HTTP_BAD_REQUEST));
    }

    @Test
    @DisplayName("Error if non existing courier")
    public void errorIfNonExistingCourier() {
        Courier courier = new Courier("ThisWillBeNull1", "password", "wrthwrth45");
        CourierCredentials loginCourierCred = new CourierCredentials("ThisWillBeNull1", "password");
        ValidatableResponse createResponse = courierSteps.create(courier);
        ValidatableResponse loginResponse = courierSteps.login(loginCourierCred);
        courierId = courierSteps.login(loginCourierCred).extract().path("id");
        ValidatableResponse deleteResponse = courierSteps.delete(courierId);
        ValidatableResponse loginNonExistingResponse = courierSteps.login(loginCourierCred);

        boolean isValueTrue = createResponse.extract().path("ok");
        int statusCodeLogin = loginResponse.extract().statusCode();
        int statusCodeDelete = deleteResponse.extract().statusCode();
        int statusCodeLoginFailed = loginNonExistingResponse.extract().statusCode();
        String messageNonExistingLogin = loginNonExistingResponse.extract().path("message").toString();

        assertThat("Creating Failed", isValueTrue);
        assertThat(statusCodeLogin, is(HTTP_OK));
        assertThat(statusCodeDelete, is(HTTP_OK));
        assertThat(statusCodeLoginFailed, is(HTTP_NOT_FOUND));
        assertThat(messageNonExistingLogin, is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Success request returns id")
    public void successRequestReturnsId() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierSteps.create(courier);
        ValidatableResponse loginResponse = courierSteps.login(CourierCredentials.from(courier));

        courierId = loginResponse.extract().path("id");
        boolean isValueTrue = createResponse.extract().path("ok");
        int statusCode = loginResponse.extract().statusCode();

        assertThat("Creating Failed", isValueTrue);
        assertThat(statusCode, is(HTTP_OK));
        assertThat(courierId, is(notNullValue()));
    }
}
