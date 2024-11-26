package org.example.models;

import java.util.UUID;

/**
 * Представляет пользователя в системе.
 * Пользователь однозначно идентифицируется своим идентификатором пользователя.
 */
public class User {
    private final String userId;

    public User() {
        this.userId = String.valueOf(UUID.randomUUID());
    }

    public User(String id) {
        this.userId = id;
    }

    public static User createNotAuthenticatedUser() {
        return new User("-1");
    }


    public String getUserId() {
        return userId;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
