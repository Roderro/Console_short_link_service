package org.example.IOConsole;

import org.example.Storage.Storage;
import org.example.models.Link;
import org.example.models.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Главная панель приложения для взаимодействия с пользователем.
 * Позволяет создавать короткие ссылки, получать оригинальные ссылки,
 * изменять параметры коротких ссылок, просматривать все созданные ссылки и
 * выходить из сессии.
 */
public class MainPanel extends AbstractPanel {

    public MainPanel(User user, Scanner scanner, Storage storage) {
        super(user, scanner, storage);
    }

    @Override
    public void printPanel() {
        System.out.print("""
                1. Создать короткую ссылку по основной
                2. Получить основную ссылку по короткой
                3. Изменить параметры короткой ссылки
                4. Получить все ссылки
                5. Удалить ссылку
                6. Выйти из сессии
                
                Введите число:""");
    }


    @Override
    AbstractPanel action(int inputNumber) {
        if (!verificateAuthorizedUser()) {
            System.out.println("Вы не авторизированны");
            return new AuthenticationPanel(user, scanner, storage);
        }
        int availableTransition;
        Optional<Link> receivedLink;
        switch (inputNumber) {
            case 1:
                System.out.print("Введите основную ссылку: ");
                String mainLink = scanner.next();
                Link link;
                System.out.print("Введите лимит переходов: ");
                try {
                    availableTransition = scanner.nextInt();
                    if (availableTransition > 0) {
                        link = new Link(mainLink, availableTransition);
                    } else throw new InputMismatchException();
                } catch (InputMismatchException e) {
                    System.out.println("Введено не число или отрицательное число,установлено значение по умолчанию 10");
                    link = new Link(mainLink);
                }
                storage.addLink(user, link);
                System.out.println("Сгенерированная короткая ссылка: " + Link.getDomain() + link.getShortLink());
                break;
            case 2:
                System.out.print("Введите короткую ссылку: ");
                receivedLink = storage.getLink(user, scanner.next());
                if (receivedLink.isPresent()) {
                    String receivedMainLink = receivedLink.get().getMainLink();
                    System.out.println("Оригинальная ссылка: " + receivedMainLink);
                    try {
                        Desktop.getDesktop().browse(new URI(receivedMainLink));
                    } catch (IOException | URISyntaxException e) {
                        System.out.println("Не удается открыть вашу ссылку");
                    } finally {
                        receivedLink.get().clickOnLink();
                        if (!receivedLink.get().checkAvailableTransition()) {
                            storage.delLink(user, receivedLink.get().getShortLink());
                            System.out.println("Количество возможных переходов для ссылки закончилось");
                        }
                    }
                }
                System.out.println();
                break;
            case 3:
                System.out.print("Введите короткую ссылку: ");
                receivedLink = storage.getLink(user, scanner.next());
                if (receivedLink.isPresent()) {
                    System.out.print("Введите новый лимит переходов: ");
                    try {
                        availableTransition = scanner.nextInt();
                        if (availableTransition > 0) {
                            receivedLink.get().setAvailableTransition(availableTransition);
                        } else System.out.println("Лимит должен быть больше 0!");
                    } catch (InputMismatchException e) {
                        System.out.println("Введено не число");
                    }
                }
                System.out.println();
                break;
            case 4:
                ArrayList<Link> links = storage.getUserLinks(user);
                if (links.isEmpty()) System.out.println("Ссылок нет");
                else {
                    for (Link l : links) {
                        System.out.println(l);
                    }
                }
                break;
            case 5:
                System.out.print("Введите короткую ссылку: ");
                receivedLink = storage.getLink(user, scanner.next());
                if (receivedLink.isPresent()) {
                    storage.delLink(user, receivedLink.get().getShortLink());
                    System.out.println("Ссылка удалена");
                }
                break;
            case 6:
                User notAuthenticatedUser = User.createNotAuthenticatedUser();
                return new AuthenticationPanel(notAuthenticatedUser, scanner, storage);
        }
        return new MainPanel(user, scanner, storage);
    }

}

