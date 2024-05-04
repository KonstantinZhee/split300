package com.zhe.split300.repositories;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operation, UUID>  {
}
