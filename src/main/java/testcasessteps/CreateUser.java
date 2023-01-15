package testcasessteps;

import api.Api;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.createuserdata.CreateUserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateUser{
    Api api = new Api();
    private String bearerTokenCreatedAccount;

    @Step("Проверяем создается ли пользователь в системе, и сравниваем значения полей почты, и имени из тела отвта, со значениями которые мы вписали в запрос")
    public void createCorrectUser(String emailYandex, String password, String name){
        ValidatableResponse createUserRequest = api.createUserResponse(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);
        this.bearerTokenCreatedAccount = createUserRequest.extract().path("accessToken");
    }

    @Step("Попытка повтрного создания пользователя")
    public void createUserAgain(String emailYandex, String password, String name){
        ValidatableResponse createUserRequest = api.createUserResponse(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        this.bearerTokenCreatedAccount = createUserRequest.extract().path("accessToken");

        ValidatableResponse createUserAgainRequest = api.createUserResponse(new CreateUserData(emailYandex, password, name));
        createUserAgainRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("User already exists"));
    }

    @Step("Попытка создания пользователя с одним из пустых полей или со всеми пустыми полями")
    public void createUserNullFields(String emailYandex, String password, String name){
        ValidatableResponse createUserNullFieldEmailRequest = api.createUserResponse(new CreateUserData("", password, name));
        createUserNullFieldEmailRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldPasswordRequest = api.createUserResponse(new CreateUserData(emailYandex, "", name));
        createUserNullFieldPasswordRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldNameRequest = api.createUserResponse(new CreateUserData(emailYandex, password, ""));
        createUserNullFieldNameRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));

        ValidatableResponse createUserNullFieldsRequest = api.createUserResponse(new CreateUserData("", "", ""));
        createUserNullFieldsRequest.statusCode(403).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Email, password and name are required fields"));
    }

    public String getBearerTokenCreatedAccount(){
        return bearerTokenCreatedAccount;
    }
}
