package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryApp {

    public static void main(String[] args) {
        List<Visitor> visitors = readVisitorsFromJson("src/main/resources/books.json");

        // Задание 1: Вывести список посетителей и их количество
        System.out.println("Список посетителей:");
        visitors.forEach(v -> System.out.println(v.getName() + " " + v.getSurname()));
        System.out.println("\nОбщее количество посетителей: " + visitors.size());

        // Задание 2: Вывести список и количество книг без повторений
        Set<Book> uniqueBooks = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .collect(Collectors.toSet());
        System.out.println("\n\nУникальные книги:");
        uniqueBooks.forEach(book -> System.out.println(book.getName() + " by " + book.getAuthor()));
        System.out.println("\nОбщее количество уникальных книг: " + uniqueBooks.size());

        // Задание 3: Отсортировать по году издания и вывести список книг
        System.out.println("\n\nКниги отсортированы по году издания:");
        uniqueBooks.stream()
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .forEach(book -> System.out.println(book.getName() + " (" + book.getPublishingYear() + ")"));

        // Задание 4: Проверить, есть ли книга автора "Jane Austen"
        boolean hasJaneAusten = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .anyMatch(book -> "Jane Austen".equals(book.getAuthor()));
        System.out.println("\n\nИзбранные книги Джейн Аустена: " + hasJaneAusten);

        // Задание 5: Максимальное число добавленных в избранное книг
        int maxFavoriteBooks = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .max()
                .orElse(0);
        System.out.println("\n\nМаксимальное количество любимых книг: " + maxFavoriteBooks);

        // Задание 6: Создание SMS-сообщений
        double averageBooks = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .average()
                .orElse(0);

        List<SmsMessage> smsMessages = visitors.stream()
                .filter(Visitor::isSubscribed)
                .map(visitor -> {
                    String message;
                    int bookCount = visitor.getFavoriteBooks().size();
                    if (bookCount > averageBooks) {
                        message = "ты книжный червь";
                    } else if (bookCount < averageBooks) {
                        message = "читай больше";
                    } else {
                        message = "пойдет";
                    }
                    return new SmsMessage(visitor.getPhone(), message);
                })
                .collect(Collectors.toList());

        // Выводим SMS-сообщения
        System.out.println("\n\nСМС сообщения:");
        smsMessages.forEach(sms -> System.out.println(sms.getPhone() + ": " + sms.getMessage()));
    }

    private static List<Visitor> readVisitorsFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return new Gson().fromJson(reader, new TypeToken<List<Visitor>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
