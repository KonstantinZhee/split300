package com.zhe.split300.repositories;

import com.zhe.split300.models.OperationBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperationBalanceRepository extends JpaRepository<OperationBalance, UUID> {
}
