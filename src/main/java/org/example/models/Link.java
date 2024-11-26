package org.example.models;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Класс Link представляет собой короткую ссылку, генерируемую из основной ссылки. Он
 * содержит информацию об основной ссылке, сокращенной ссылке, количестве доступных
 * кликов, дате и времени создания, а также предоставляет методы для проверки актуальности
 * ссылки и доступности переходов.
 */
public class Link {

    private final String mainLink;
    private final String shortLink;
    private int availableTransition;
    private final LocalDateTime ldt;
    private final static String DOMAIN = "http://Tiny.url/";

    public LocalDateTime getLdt() {
        return ldt;
    }

    public String getMainLink() {
        return mainLink;
    }

    public void setAvailableTransition(int availableTransition) {
        this.availableTransition = availableTransition;
    }

    public String getShortLink() {
        return shortLink;
    }

    public static String getDomain() {
        return DOMAIN;
    }

    public int getAvailableTransition() {
        return availableTransition;
    }


    public Link(String mainLink, int availableTransition) {
        this.mainLink = mainLink;
        this.shortLink = shortenerLink();
        this.availableTransition = availableTransition;
        this.ldt = LocalDateTime.now();
    }

    public Link(String mainLink) {
        this(mainLink, 10);
    }


    private String shortenerLink() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public boolean checkFreshnessLink() {
        return LocalDateTime.now().isBefore(ldt.plusDays(1));
    }

    public boolean checkAvailableTransition() {
        return availableTransition > 0;
    }

    public void clickOnLink() {
        availableTransition--;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d:MM:uuuu HH:mm:ss");
        return "Основная ссылка = '" + mainLink + '\'' +
                ", Короткая ссылка = '" + DOMAIN + shortLink + '\'' +
                ", количество доступных переходов = " + availableTransition +
                ", время создания = " + formatter.format(ldt);
    }
}
