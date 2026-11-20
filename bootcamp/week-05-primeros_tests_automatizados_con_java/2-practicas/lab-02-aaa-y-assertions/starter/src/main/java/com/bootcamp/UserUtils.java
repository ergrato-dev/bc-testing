package com.bootcamp;

import java.util.List;

public class UserUtils {

    public static boolean isAdult(int age) {
        return age >= 18;
    }

    public static double calculateDiscount(double price, int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Invalid percent");
        }
        return price - (price * percent) / 100;
    }

    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    public static User createUser(String name, String email) {
        return new User(name.strip(), email.toLowerCase(), true);
    }

    public static User findUserByEmail(List<User> users, String email) {
        for (User user : users) {
            if (user.email().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
