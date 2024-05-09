package com.zhe.split300.repositories;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Evention;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventionRepository extends JpaRepository<Evention, UUID> {

    Set<Evention> findByCompany(Company company);

    @EntityGraph(value = "Evention.details", type = EntityGraph.EntityGraphType.LOAD)
    default Optional<Evention> findByIdWithAllFields(UUID eventionId) {
        return findById(eventionId);
    }
}
