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
import static org.hamcrest.MatcherAssert.assertThat;

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
    @DisplayName("Courier can be created")
    public void courierCanBeCreated() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierSteps.create(courier);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");//строчка будет использована для передачи id в метод delete
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        assertThat(statusCode, is(HTTP_CREATED));
        assertThat("Creating failed", isCourierCreated);
    }

    @Test
    @DisplayName("Courier cannot be created twice")
    public void courierCannotBeCreatedTwice() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierSteps.create(courier);
        ValidatableResponse createResponseSecondCreating = courierSteps.create(courier);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        int statusCodeDuplicate = createResponseSecondCreating.extract().statusCode();
        String createdTwiceMessage = createResponseSecondCreating.extract().path("message").toString();
        //проверка, что новый курьер создался
        assertThat(statusCode, is(HTTP_CREATED));
        assertThat("Creating failed", isCourierCreated);
        //проверка, что курьер с теми же кредами не создался
        assertThat(statusCodeDuplicate, is(HTTP_CONFLICT));
        assertThat(createdTwiceMessage, is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Courier is created with all values")
    public void courierIsCreatedWithAllValues() {
        Courier courier = new Courier("ninjatest", "1234", "saske");
        ValidatableResponse createResponse = courierSteps.create(courier);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");//строчка будет использована для передачи id в метод delete
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        assertThat(statusCode, is(HTTP_CREATED));
        assertThat("Creating failed", isCourierCreated);
    }

    @Test
    @DisplayName("Request returns correct statusCode")
    public void requestReturnsCorrectStatusCode() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierSteps.create(courier);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
        int statusCode = createResponse.extract().statusCode();

        assertThat(statusCode, is(HTTP_CREATED));
    }

    @Test
    @DisplayName("Success request returns correct value")
    public void successRequestReturnsCorrectValue() {
        Courier courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierSteps.create(courier);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
        boolean isValueTrue = createResponse.extract().path("ok");

        assertThat("Creating failed", isValueTrue);
    }

    @Test
    @DisplayName("Courier cannot be created without password")
    public void courierCannotBeCreatedWithoutPassword() {
        Courier courier = new Courier("testoviylogin15.", null, "wrthwrth45");
        ValidatableResponse createResponse = courierSteps.create(courier);
        int statusCode = createResponse.extract().statusCode();
        String messageWithoutPassword = createResponse.extract().path("message").toString();

        assertThat(statusCode, is(HTTP_BAD_REQUEST));
        assertThat(messageWithoutPassword, is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Courier error when login is already used")
    public void courierErrorWhenLoginIsAlreadyUsed() {
        Courier courier = new Courier("LoginToBeUsed22", "test", "Test");
        Courier courierSameLogin = new Courier("LoginToBeUsed22", "test2", "Test2");
        ValidatableResponse createResponse = courierSteps.create(courier);
        ValidatableResponse createResponseSameLogin = courierSteps.create(courierSameLogin);
        courierId = courierSteps.login(CourierCredentials.from(courier)).extract().path("id");
        int statusCode = createResponse.extract().statusCode();
        int statusCodeSameLogin = createResponseSameLogin.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        String messageCourierNotCreated = createResponseSameLogin.extract().path("message").toString();
        //проверяем, что новый курьер создался
        assertThat(statusCode, is(HTTP_CREATED));
        assertThat("Creating failed", isCourierCreated);
        //проверяем, что курьер с тем же логином не создался
        assertThat(statusCodeSameLogin, is(HTTP_CONFLICT));
        assertThat(messageCourierNotCreated, is("Этот логин уже используется. Попробуйте другой."));
    }
}