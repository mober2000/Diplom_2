package universalclasses;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomGenerator {
    private final String emailYandex = setRandomEmail();
    private final String password = setRandomPassword();
    private final String name = setRandomName();

    public String setRandomEmail() {
        return RandomStringUtils.randomAlphanumeric(10).toLowerCase() + "@yandex.ru";
    }

    public String setRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public String setRandomName() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    public String getEmailYandex() {
        return emailYandex;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
