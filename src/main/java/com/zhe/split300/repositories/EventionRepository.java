package com.zhe.split300.repositories;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Evention;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventionRepository extends JpaRepository<Evention, UUID> {

    Set<Evention> findByCompany(Company company);

    @EntityGraph(value = "Evention.withOperations", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT e FROM Evention e WHERE e.id = :uid")
    Optional<Evention> findByIdWithOperations(@Param("uid") UUID eventionId);

    @EntityGraph(value = "Evention.details", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT e FROM Evention e WHERE e.id = :uid")
    Evention findByIdToMakeCalculations(@Param("uid") UUID eventionId);

    @EntityGraph(value = "Evention.forEditPage", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT e FROM Evention e WHERE e.id = :uid")
    Evention findByIdToEditEvention(@Param("uid") UUID eventionId);
}
