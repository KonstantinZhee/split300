package com.zhe.split300.repositories;

import com.zhe.split300.models.Evention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventionRepository extends JpaRepository<Evention, UUID> {
}
