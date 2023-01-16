package testcases;

import api.Api;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.useractiondata.UserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class UserActions {
    Api api = new Api();

    @Step("Удаление пользователя с предварительной авторизацией")
    public void deleteUser(String bearerToken) {
        ValidatableResponse deleteUserRequest = api.deleteUserKeyRequest(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

    @Step("Удаление пользователя без предварительной авторизации")
    public void deleteUserNotKey() {
        ValidatableResponse deleteUserRequest = api.deleteUserNotKeyRequest();
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

    @Step("Изменение данных авторизованного пользователя")
    public void patchUserData(String mail, String name, String bearerToken) {
        ValidatableResponse patchUserDataRequest = api.patchDataUserRequest(new UserData("ya23" + mail, "Man" + name), bearerToken);
        patchUserDataRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailPatchResponse = patchUserDataRequest.extract().path("user.email");
        String namePatchResponse = patchUserDataRequest.extract().path("user.name");
        assertEquals("ya23" + mail, mailPatchResponse);
        assertEquals("Man" + name, namePatchResponse);
    }

    @Step("Изменение данных неавторизованного пользователя")
    public void patchDataNotAuthorizationUser(String mail, String name) {
        ValidatableResponse patchUserDataRequest = api.patchDataUserNotKeyRequest(new UserData("ya23" + mail, "Man " + name));
        patchUserDataRequest.statusCode(401).assertThat().body("success", equalTo(false));
    }
}
