package com.zhe.split300.repositories;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByName(String name);

    List<Company> findByPersons(List<Person> persons);

    List<Company> findByOwner(Person owner);
}
