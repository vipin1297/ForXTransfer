package com.spiralforge.ForXTransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.ForXTransfer.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{


}
