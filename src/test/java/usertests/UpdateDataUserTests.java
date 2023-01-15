package usertests;

import universalclasses.RandomGenerator;
import user.useractions.UserActions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import user.loginuser.LoginUser;
import pojo.loginuserdata.LoginUserData;
import org.junit.Test;
import pojo.useractiondata.UserData;
import user.usercreate.CreateUser;
import pojo.createuserdata.CreateUserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class UpdateDataUserTests {
    RandomGenerator randomGenerator = new RandomGenerator();
    CreateUser createUser = new CreateUser();
    LoginUser loginUser = new LoginUser();
    UserActions userActions = new UserActions();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.getPassword();
    String name = randomGenerator.getName();

    @Test
    @DisplayName("Patch Data Authorization User")
    @Description("Проверка на внесение изменений в данные авторизованного пользователя")
    public void patchDataAuthorizationUserTest() {
        ValidatableResponse createUserRequest = createUser.createUserResponse(new CreateUserData(mail, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(mail, mailResponse);
        assertEquals(name, nameResponse);

        ValidatableResponse loginUserRequest = loginUser.loginUserRequest(new LoginUserData(mail, password));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailLoginResponse = loginUserRequest.extract().path("user.email");
        String nameLoginResponse = loginUserRequest.extract().path("user.name");
        assertEquals(mail, mailLoginResponse);
        assertEquals(name, nameLoginResponse);

        String bearerToken = createUserRequest.extract().path("accessToken");
        ValidatableResponse patchUserDataRequest = userActions.patchDataUserRequest(new UserData("ya23" + mail,"Man " + name), bearerToken);
        patchUserDataRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailPatchResponse = patchUserDataRequest.extract().path("user.email");
        String namePatchResponse = patchUserDataRequest.extract().path("user.name");
        assertEquals("ya23" + mail, mailPatchResponse);
        assertEquals("Man " + name, namePatchResponse);

        ValidatableResponse deleteUserRequest = userActions.deleteUserNotKeyRequest();
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }



    //Необходимо узнать должен ли изменяться клиент или нет если мы не авторизовались?

    @Test
    @DisplayName("Patch Data Not Authorization  User")
    @Description("Проверка на внесение изменений в данные неавторизованного пользователя")
    public void patchDataNotAuthorizationUserTest() {
        ValidatableResponse createUserRequest = createUser.createUserResponse(new CreateUserData(mail, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(mail, mailResponse);
        assertEquals(name, nameResponse);


        ValidatableResponse patchUserDataRequest = userActions.patchDataUserNotKeyRequest(new UserData("ya23" + mail,"Man " + name));
        patchUserDataRequest.statusCode(401).assertThat().body("success", equalTo(false));

        String bearerToken = createUserRequest.extract().path("accessToken");
        ValidatableResponse deleteUserRequest = userActions.deleteUserKeyRequest(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }



}
