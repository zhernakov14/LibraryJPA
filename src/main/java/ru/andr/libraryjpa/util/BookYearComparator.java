package ru.andr.libraryjpa.util;

import ru.andr.libraryjpa.models.Book;

import java.util.Comparator;

public class BookYearComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.getYear() - o2.getYear();
    }
}
