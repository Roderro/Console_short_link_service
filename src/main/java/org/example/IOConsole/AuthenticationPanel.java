package org.example.IOConsole;

import org.example.Storage.Storage;
import org.example.models.User;

import java.util.Objects;
import java.util.Scanner;

/**
 * Панель аутентификации пользователя в приложении.
 * Позволяет пользователям создать новую сессию, войти в существующую сессию или выйти из приложения.
 */
public class AuthenticationPanel extends AbstractPanel {

    public AuthenticationPanel(User user, Scanner scanner, Storage storage) {
        super(user, scanner, storage);
    }

    public void printPanel() {
        System.out.print("""
               1. Создать новую сессию
               2. Войти в сессию
               3. Выйти из приложения
               \s
               Введите число:\s""");
    }


    public AbstractPanel action(int inputNum) {
        String userId;
        switch (inputNum) {
            case 3:
                return new CloseApplicationPanel(user, scanner, storage);
            case 1:
                user = new User();
                storage.addUser(user);
                return new MainPanel(user, scanner, storage);
            case 2:
                System.out.print("Введите ваш идентификатор: ");
                userId = scanner.next();
                if (!Objects.equals(userId, "-1") && storage.isUser(userId)) {
                    user = new User(userId);
                    return new MainPanel(user, scanner, storage);
                } else {
                    System.out.println("Данный идентификатор не существует!");
                    return new AuthenticationPanel(user, scanner, storage);
                }
            default:
                System.out.println("Что-то пошло не так попробуйте снова!");
                return new AuthenticationPanel(user, scanner, storage);
        }
    }
}
