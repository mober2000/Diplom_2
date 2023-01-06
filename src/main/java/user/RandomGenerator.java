package user;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomGenerator {

    public String randomEmail() {
        return RandomStringUtils.randomAlphanumeric(10).toLowerCase();
    }

    public String randomPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public String randomName() {
        return RandomStringUtils.randomAlphanumeric(12);
    }


}
