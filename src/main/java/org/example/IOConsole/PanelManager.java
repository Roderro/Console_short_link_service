package org.example.IOConsole;

import java.util.Optional;


/**
* Класс PanelManager отвечает за управление жизненным циклом и взаимодействием различных пользовательских панелей посредством непрерывного цикла.
 * Отображение пользовательской информации, рендеринг содержимого панели и обработка пользовательского ввода.
 */
public class PanelManager {
    private AbstractPanel panel;


    public PanelManager(AbstractPanel panel) {
        this.panel = panel;
    }

    public void manage() {
        while (true) {
            panel.printUserId();
            panel.printPanel();
            Optional<Integer> inputInt = panel.parseInputNumber();
            if (inputInt.isEmpty()) continue;
            panel = panel.action(inputInt.get());
        }
    }
}
