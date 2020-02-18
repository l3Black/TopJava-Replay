package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final User USER = new User(1, "user", "user@yandex.ru", "12345", Role.ROLE_USER);
    public static final User ADMIN = new User(2, "admin", "admin@gmail.com", "222222", Role.ROLE_ADMIN);
    public static final List<User> USERS = Arrays.asList(USER, ADMIN);
}
