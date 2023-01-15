package usertests;

import universalclasses.RandomGenerator;
import testcasessteps.UserActions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import testcasessteps.CreateUser;

public class UpdateDataUserTests {
    RandomGenerator randomGenerator = new RandomGenerator();
    CreateUser createUser = new CreateUser();
    UserActions userActions = new UserActions();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.getPassword();
    String name = randomGenerator.getName();

    @Test
    @DisplayName("Patch Data Authorization User")
    @Description("Проверка на внесение изменений в данные авторизованного пользователя")
    public void patchDataAuthorizationUserTest() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        userActions.patchUserData(mail, password, bearerToken);
        userActions.deleteUserNotKey();
    }

    @Test
    @DisplayName("Patch Data Not Authorization  User")
    @Description("Проверка на внесение изменений в данные неавторизованного пользователя")
    public void patchDataNotAuthorizationUserTest() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        userActions.patchDataNotAuthorizationUser(mail, name);
        userActions.deleteUser(bearerToken);
    }
}
