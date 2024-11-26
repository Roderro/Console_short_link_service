package org.example.Storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.models.Link;
import org.example.models.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс Storage отвечает за управление (добавление, удаления, обновление) ссылками пользователей, в памяти процесса
 * ссылки пользователей хранятся во вложенный Map, где ключ - ID пользователя, а значение - Map, где ключ - сокращенная ссылка,
 * а значение - объект Link. Также реализованна методы чтение и запись в файл JSON для сохранения информации между сессиями.
 * Они используют Gson для сериализации и десериализации JSON.
 */
public class Storage {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).setPrettyPrinting().create();
    private final Path userLinksFile = Path.of("userLinks.json");
    private Map<String, Map<String, Link>> usersLinks;

    public Storage(Map<String, Map<String, Link>> usersLinks) {
        this.usersLinks = usersLinks;
    }

    public Storage() {
        this.usersLinks = readAllinFile();
    }

    public void writeAllInFile() {
        try {
            Files.writeString(userLinksFile, gson.toJson(usersLinks),
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Map<String, Link>> readAllinFile() {
        try {
            Type type = new TypeToken<Map<String, Map<String, Link>>>() {
            }.getType();
            usersLinks = gson.fromJson(Files.readString(userLinksFile, StandardCharsets.UTF_8), type);
            return usersLinks != null ? usersLinks : new HashMap<>();
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + userLinksFile + ", " + e.getMessage());
        }
        return usersLinks;
    }

    public void addUser(User user) {
        usersLinks.put(user.getUserId(), new HashMap<>());
    }

    public boolean isUser(String userId) {
        return usersLinks.get(userId) != null;
    }

    public void addLink(User user, Link link) {
        usersLinks.computeIfAbsent(user.getUserId(), k -> new HashMap<>()).put(link.getShortLink(), link);
    }

    public Optional<Link> getLink(User user, String shortLinkWithDomain) {
        int beginIndex = Link.getDomain().length();
        if (shortLinkWithDomain.length() < beginIndex) {
            System.out.println("Введена не правильная ссылка, пример ссылки 'http://Tiny.url/e22b29', попробуйте заново!");
            return Optional.empty();
        }
        String shortLinkWithoutDomain = shortLinkWithDomain.substring(beginIndex);
        Link link = usersLinks.get(user.getUserId()).get(shortLinkWithoutDomain);
        if (link == null) {
            System.out.println("Ссылки не существует, попробуйте заново!");
            return Optional.empty();
        }
        if (!link.checkAvailableTransition()) {
            System.out.println("Количество возможных переходов закончилось!");
            delLink(user, shortLinkWithoutDomain);
            return Optional.empty();
        }
        if (!link.checkFreshnessLink()) {
            System.out.println("Время жизни ссылки закончилось!");
            delLink(user, shortLinkWithoutDomain);
            return Optional.empty();
        }
        return Optional.of(link);
    }

    public ArrayList<Link> getUserLinks(User user) {
        Collection<Link> links = usersLinks.get(user.getUserId()).values();
        ArrayList<Link> linkList = new ArrayList<>();
        List<Link> linksToRemove = new ArrayList<>();
        for (Link link : links) {
            if (!link.checkAvailableTransition()) {
                System.out.println("Количество возможных переходов закончилось для ссылки" + link.getShortLink());
                linksToRemove.add(link);
                continue;
            }
            if (!link.checkFreshnessLink()) {
                System.out.println("Время жизни ссылки " + link.getShortLink() + " закончилось!");
                linksToRemove.add(link);
                continue;
            }
            linkList.add(link);
        }
        for (Link link : linksToRemove) {
            delLink(user, link.getShortLink());
        }
        return linkList;
    }

    public void delLink(User user, String shortLink) {
        usersLinks.get(user.getUserId()).remove(shortLink);
    }
}
