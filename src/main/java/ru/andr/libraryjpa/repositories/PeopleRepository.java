package ru.andr.libraryjpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andr.libraryjpa.models.Person;

import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByFullName(String fullName);
}
