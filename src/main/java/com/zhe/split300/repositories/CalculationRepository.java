package com.zhe.split300.repositories;

import com.zhe.split300.models.Calculation;
import com.zhe.split300.models.Evention;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, UUID> {

    @Transactional
    void deleteAllByEvention(Evention evention);

    @EntityGraph(value = "Calculations.updateCalculation", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Calculation c WHERE c.id = :uid")
    Calculation selectCalculationToUpdate(@Param("uid") UUID calculationId);
}
