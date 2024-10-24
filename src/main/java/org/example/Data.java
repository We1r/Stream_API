package org.example;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
class Book {
    private String name;
    private String author;
    private int publishingYear;
    private String isbn;
    private String publisher;
}

@Data
class Visitor {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;

    @SerializedName("favoriteBooks")
    private List<Book> favoriteBooks;
}

@Data
class SmsMessage {
    private String phone;
    private String message;

    public SmsMessage(String phone, String message) {
        this.phone = phone;
        this.message = message;
    }
}