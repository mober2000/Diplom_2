package user.usercreate;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.createuserdata.CreateUserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateUser extends RestClient{


    private String bearerTokenCreatedAccount;

    @Step("Запрос на регистрацию нового пользователя")
    public ValidatableResponse createUserResponse(CreateUserData createUserData) {
        return reqSpec
                .body(createUserData)
                .when()
                .post("auth/register")
                .then();
    }

    @Step("Проверяем создается ли пользователь в системе, и сравниваем значения полей почты, и имени из тела отвта, со значениями которые мы вписали в запрос")
    public void createCorrectUser(String emailYandex, String password, String name){
        ValidatableResponse createUserRequest = createUserResponse(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);
        this.bearerTokenCreatedAccount = createUserRequest.extract().path("accessToken");
    }

    @Step("Попытка повтрного создания пользователя")
    public void createUserAgain(String emailYandex, String password, String name){
        ValidatableResponse createUserRequest = createUserResponse(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        this.bearerTokenCreatedAccount = createUserRequest.extract().path("accessToken");

        ValidatableResponse createUserAgainRequest = createUserResponse(new CreateUserData(emailYandex, password, name));
        createUserAgainRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("User already exists"));
    }

    @Step("Попытка создания пользователя с одним из пустых полей или со всеми пустыми полями")
    public void createUserNullFields(String emailYandex, String password, String name){
        ValidatableResponse createUserNullFieldEmailRequest = createUserResponse(new CreateUserData("", password, name));
        createUserNullFieldEmailRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldPasswordRequest = createUserResponse(new CreateUserData(emailYandex, "", name));
        createUserNullFieldPasswordRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldNameRequest = createUserResponse(new CreateUserData(emailYandex, password, ""));
        createUserNullFieldNameRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldsRequest = createUserResponse(new CreateUserData("", "", ""));
        createUserNullFieldsRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));
    }

    public String getBearerTokenCreatedAccount(){
        return bearerTokenCreatedAccount;
    }
}
