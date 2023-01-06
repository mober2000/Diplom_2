package user.useractions;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class GetUpdateAndDeleteUser extends RestClient{
    @Step("Удаление пользователя с ключом авторизации")
    public ValidatableResponse deleteUserKey(String bearer) {
        return reqSpec
                .header("Authorization", bearer)
                .when()
                .delete("auth/user")
                .then().log().all();
    }

    @Step("Удаление пользователя без ключа авторизации")
    public ValidatableResponse deleteUserNotKey() {
        return reqSpec
                .delete("auth/user")
                .then().log().all();
    }

    @Step("Получение данных о пользователе")
    public ValidatableResponse getDataUser(String bearer) {
        return reqSpec
                .header("Authorization", bearer)
                .when()
                .get("auth/user")
                .then().log().all();
    }

    @Step("Внесение изменений в данные авторизованного пользователя")
    public ValidatableResponse patchDataUser(UserData userData, String bearer) {
        return reqSpec
                .body(userData)
                .header("Authorization", bearer)
                .when()
                .patch("auth/user")
                .then().log().all();
    }

    @Step("Внесение изменений в данные неавторизованного пользователя")
    public ValidatableResponse patchDataUserNotKey(UserData userData) {
        return reqSpec
                .body(userData)
                .when()
                .patch("auth/user")
                .then().log().all();
    }

//    @Step("Внесение изменений в данные пользователя")
//    public ValidatableResponse updateToken(String bearer) {
//        return reqSpec
//                .body(userData)
//                .header("Authorization", bearer)
//                .when()
//                .patch("auth/user")
//                .then().log().all();
//    }
}
