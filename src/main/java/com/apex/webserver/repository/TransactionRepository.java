//package com.apex.webserver.repository;
//
//import com.apex.webserver.model.entity.Transaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface  TransactionRepository extends JpaRepository<Transaction, Long> {
//    Optional<Transaction> findByTransactionId(String transactionId);
//    boolean existsByTransactionId(String transactionId);
//}
