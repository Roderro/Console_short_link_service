package org.example;

import org.example.IOConsole.AbstractPanel;
import org.example.IOConsole.AuthenticationPanel;
import org.example.IOConsole.PanelManager;
import org.example.Storage.Storage;

import org.example.models.User;

import java.util.Scanner;

/**
 * Класс Main является точкой входа в приложение.
 * Основное назначение - инициализация необходимых объектов и запуск процесса управления панелями.
 */
public class Main {
    public static void main(String[] args) {
        User user = User.createNotAuthenticatedUser();
        Storage storage = new Storage();
        Scanner scanner = new Scanner(System.in);
        AbstractPanel authenticationPanel = new AuthenticationPanel(user, scanner, storage);
        PanelManager panelManager = new PanelManager(authenticationPanel);
        panelManager.manage();
    }
}
