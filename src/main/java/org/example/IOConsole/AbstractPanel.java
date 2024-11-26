package org.example.IOConsole;

import org.example.Storage.Storage;
import org.example.models.User;

import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * Абстрактный базовый класс для определения пользовательских панелей в приложении.
 * Предоставляет общие методы и свойства для обработки взаимодействия с пользователем.
 */

public abstract class AbstractPanel {
    protected User user;
    protected Scanner scanner;
    protected Storage storage;


    public AbstractPanel(User user, Scanner scanner, Storage storage) {
        this.user = user;
        this.scanner = scanner;
        this.storage = storage;
    }

    public void printUserId() {
        String authenticated = "Не авторизован";
        String userId = user.getUserId();
        if (!user.getUserId().equals("-1")) {
            authenticated = userId;
        }
        String lineString = "-".repeat(authenticated.length() + 19);
        System.out.println(lineString);
        System.out.println("Ваш идентификатор: " + authenticated);
        System.out.println(lineString);
    }

    abstract void printPanel();

    public Optional<Integer> parseInputNumber() {
        String scan = scanner.next();
        try {
            return Optional.of(Integer.valueOf(scan));
        } catch (NumberFormatException e) {
            System.out.println("Введено не правильное число");
            return Optional.empty();
        }
    }

    public final boolean verificateAuthorizedUser() {
        return !Objects.equals(user.getUserId(), "-1");
    }

    abstract AbstractPanel action(int inputNumber);


}
