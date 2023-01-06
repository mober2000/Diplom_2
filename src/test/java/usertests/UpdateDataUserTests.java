package usertests;

import universalclasses.RandomGenerator;
import useractions.GetUpdateAndDeleteUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import loginuser.LoginUser;
import loginuser.LoginUserData;
import org.junit.Test;
import useractions.UserData;
import usercreate.CreateUser;
import usercreate.CreateUserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class UpdateDataUserTests {
    RandomGenerator randomGenerator = new RandomGenerator();
    CreateUser createUser = new CreateUser();
    LoginUser loginUser = new LoginUser();
    GetUpdateAndDeleteUser getUpdateAndDeleteUser = new GetUpdateAndDeleteUser();
    private final String emailYandex = randomGenerator.randomEmail() + "@yandex.ru";
    private final String password = randomGenerator.randomPassword();
    private final String name = randomGenerator.randomName();

    @Test
    @DisplayName("Patch Data Authorization User")
    @Description("Проверка на внесение изменений в данные авторизованного пользователя")
    public void patchDataAuthorizationUserTest() {
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);

        ValidatableResponse loginUserRequest = loginUser.loginUser(new LoginUserData(emailYandex, password));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailLoginResponse = loginUserRequest.extract().path("user.email");
        String nameLoginResponse = loginUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailLoginResponse);
        assertEquals(name, nameLoginResponse);

        String bearerToken = createUserRequest.extract().path("accessToken");
        ValidatableResponse patchUserDataRequest = getUpdateAndDeleteUser.patchDataUser(new UserData("ya23" + emailYandex,"Man " + name), bearerToken);
        patchUserDataRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailPatchResponse = patchUserDataRequest.extract().path("user.email");
        String namePatchResponse = patchUserDataRequest.extract().path("user.name");
        assertEquals("ya23" + emailYandex, mailPatchResponse);
        assertEquals("Man " + name, namePatchResponse);

        ValidatableResponse deleteUserRequest = getUpdateAndDeleteUser.deleteUserNotKey();
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }



    //Необходимо узнать должен ли изменяться клиент или нет если мы не авторизовались?

    @Test
    @DisplayName("Patch Data Not Authorization  User")
    @Description("Проверка на внесение изменений в данные неавторизованного пользователя")
    public void patchDataNotAuthorizationUserTest() {
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);


        ValidatableResponse patchUserDataRequest = getUpdateAndDeleteUser.patchDataUserNotKey(new UserData("ya23" + emailYandex,"Man " + name));
        patchUserDataRequest.statusCode(401).assertThat().body("success", equalTo(false));

        String bearerToken = createUserRequest.extract().path("accessToken");
        ValidatableResponse deleteUserRequest = getUpdateAndDeleteUser.deleteUserKey(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

}
