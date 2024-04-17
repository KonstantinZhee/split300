package com.zhe.split300.repositories;

import com.zhe.split300.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);
    Optional<Person> findByName(String name);
    List<Person> findAllByQuery(String query);
}
