package org.example.IOConsole;

import org.example.Storage.Storage;
import org.example.models.User;

import java.util.Optional;
import java.util.Scanner;

/**
 * Панель для закрытия приложения.
 *
 * Предоставляет функциональность завершения работы программы, сохранение данных и выход из приложения.
 */
public class CloseApplicationPanel extends AbstractPanel {


    public CloseApplicationPanel(User user, Scanner scanner, Storage storage) {
        super(user, scanner, storage);
    }

    @Override
    public final void printUserId() {
    }

    @Override
    void printPanel() {
        System.out.println("До скорых встреч");
    }

    @Override
    public final Optional<Integer> parseInputNumber() {
        return Optional.of(0);
    }

    @Override
    AbstractPanel action(int inputNumber) {
        storage.writeAllInFile();
        scanner.close();
        System.exit(inputNumber);
        return null;
    }
}
