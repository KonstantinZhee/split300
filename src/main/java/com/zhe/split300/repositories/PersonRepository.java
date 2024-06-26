package com.zhe.split300.repositories;

import com.zhe.split300.models.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);

    Optional<Person> findByName(String name);

    List<Person> findByNameIgnoreCaseStartingWith(String name);

    List<Person> findByEmailIgnoreCaseStartingWith(String email);

    @EntityGraph(value = "Person.withCompanies", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT p FROM Person p WHERE p.id = :id")
    Person findOneWithCompanies(@Param("id") int personId);
}
