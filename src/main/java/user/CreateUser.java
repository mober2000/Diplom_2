package user;

import io.restassured.response.ValidatableResponse;

public class CreateUser extends RestClient{

    public ValidatableResponse createUser(CreateUserData createUserData) {
        return reqSpec
                .body(createUserData)
                .when()
                .post("auth/register")
                .then().log().all();
    }
}
