package ru.andr.libraryjpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andr.libraryjpa.models.Book;
import ru.andr.libraryjpa.models.Person;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByOwner(Person owner);

    List<Book> findByTitleStartingWith(String title);
}
