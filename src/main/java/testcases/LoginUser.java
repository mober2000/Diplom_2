package testcases;

import api.Api;
import api.RestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.loginuserdata.LoginUserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginUser extends RestClient {
    Api api = new Api();

    @Step("Авторизация пользователя")
    public void loginUser(String mail, String password, String name) {
        ValidatableResponse loginUserRequest = api.loginUserRequest(new LoginUserData(mail, password));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailLoginResponse = loginUserRequest.extract().path("user.email");
        String nameLoginResponse = loginUserRequest.extract().path("user.name");
        assertEquals(mail, mailLoginResponse);
        assertEquals(name, nameLoginResponse);
    }

    @Step("Авторизация пользователя с неверными логином и паролем")
    public void loginUnableFields(String mail, String password) {
        ValidatableResponse loginUnableEmailFiledRequest = api.loginUserRequest(new LoginUserData("jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341", password));
        loginUnableEmailFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginUnablePasswordFiledRequest = api.loginUserRequest(new LoginUserData(mail, "jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341"));
        loginUnablePasswordFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginUnableEmailAndPasswordFieldsRequest = api.loginUserRequest(new LoginUserData("jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341", "jowjugwnjipwregnjpiwrejpngnjwriegnjiwerwerjwr32423412341341"));
        loginUnableEmailAndPasswordFieldsRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));
    }

    @Description("Авторизация пользователя с пустыми полями логина и пароля")
    public void loginNullFields(String mail, String password) {
        ValidatableResponse loginNullEmailFiledRequest = api.loginUserRequest(new LoginUserData("", password));
        loginNullEmailFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginNullPasswordFiledRequest = api.loginUserRequest(new LoginUserData(mail, ""));
        loginNullPasswordFiledRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));

        ValidatableResponse loginNullEmailAndPasswordFieldsRequest = api.loginUserRequest(new LoginUserData("", ""));
        loginNullEmailAndPasswordFieldsRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("email or password are incorrect"));
    }
}
