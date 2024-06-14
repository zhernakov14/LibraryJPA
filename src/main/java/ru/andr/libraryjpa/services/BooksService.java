package ru.andr.libraryjpa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andr.libraryjpa.models.Book;
import ru.andr.libraryjpa.models.Person;
import ru.andr.libraryjpa.repositories.BooksRepository;
import ru.andr.libraryjpa.util.BookYearComparator;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortedByYear) {
        if(sortedByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> pagination(int page, int booksPerPage, boolean sortedByYear) {
        if(sortedByYear) {
            List<Book> res = new ArrayList<>(booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent());
            BookYearComparator bookYearComparator = new BookYearComparator();
            res.sort(bookYearComparator);
            return res;
        }
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        booksRepository.save(updatedBook);
    }

    public List<Book> searchByTitle(String request) {
        return booksRepository.findByTitleStartingWith(request);
    }
    public Optional<Person> getBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getOwner);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakeAt(new Date());
                }
        );
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setExpired(false);
                }
        );
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }
}
