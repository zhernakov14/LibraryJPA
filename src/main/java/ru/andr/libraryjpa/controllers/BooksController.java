package ru.andr.libraryjpa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.andr.libraryjpa.models.Book;
import ru.andr.libraryjpa.models.Person;
import ru.andr.libraryjpa.services.BooksService;
import ru.andr.libraryjpa.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sorted_by_year", required = false) boolean sortedByYear,
                        Model model) {
        if(page == null || booksPerPage == null)
            model.addAttribute("books", booksService.findAll(sortedByYear));
        else
            model.addAttribute("books", booksService.pagination(page, booksPerPage, sortedByYear));
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person")Person person) {
        model.addAttribute("book", booksService.findOne(id));

        Optional<Person> owner = booksService.getBookOwner(id);

        if(owner.isPresent())
            model.addAttribute("owner", owner.get());
        else
            model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") Book book) {
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") Book book) {
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id, @ModelAttribute("book") Book book) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("request") String request) {
        model.addAttribute("books", booksService.searchByTitle(request));
        return "books/search";
    }
}
