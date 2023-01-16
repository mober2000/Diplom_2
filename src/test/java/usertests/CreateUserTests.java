package usertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import universalclasses.RandomGenerator;
import testcases.UserActions;
import testcases.CreateUser;

public class CreateUserTests {
    RandomGenerator randomGenerator = new RandomGenerator();
    CreateUser createUser = new CreateUser();
    UserActions userActions = new UserActions();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.getPassword();
    String name = randomGenerator.getName();

    @Test
    @DisplayName("Create User")
    @Description("Проверяем создается ли новый пользователь")
    public void createUser() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        userActions.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Create User Again")
    @Description("Проверяем не создается ли уже существующий пользователь")
    public void createUserAgain() {
        createUser.createUserAgain(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        userActions.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Create User Null Fields Registration")
    @Description("Проверяем не создается ли пользователь c пустыми полями почты, пароля и имени")
    public void createUserNullFields() {
        createUser.createUserNullFields(mail, password, name);
    }
}
