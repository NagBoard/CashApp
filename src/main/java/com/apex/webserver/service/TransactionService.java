//package com.apex.webserver.service;
//
//import com.apex.webserver.model.entity.Account;
//import com.apex.webserver.model.entity.Transaction;
//import com.apex.webserver.repository.AccountRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//
//@Service
//@Transactional
//public class TransactionService {
//
//    @Autowired
//    private AccountRepository accountRepo;
//
//    @Autowired
//    private AccountingRules rules;
//
//    public Transaction createTransaction(
//            Long leftAccountId,   // Instead of "from"
//            Long rightAccountId,  // Instead of "to"
//            BigDecimal amount,
//            String description) {
//
//                Account leftAccount = accountRepo.findById(leftAccountId);
//        Account rightAccount = accountRepo.findById(rightAccountId);
//
//        // Apply debit to left account
//        applyEntry(leftAccount, amount, EntryType.DEBIT);
//
//        // Apply credit to right account
//        applyEntry(rightAccount, amount, EntryType.CREDIT);
//
//        // Save transaction...
//    }
//
//    private void applyEntry(Account account, BigDecimal amount, EntryType type) {
//        if (type == EntryType.DEBIT) {
//            if (account.getType() == ASSET || account.getType() == EXPENSE) {
//                account.setBalance(account.getBalance().add(amount));  // Increase
//            } else {
//                account.setBalance(account.getBalance().subtract(amount));  // Decrease
//            }
//        } else { // CREDIT
//            if (account.getType() == LIABILITY || account.getType() == EQUITY || account.getType() == REVENUE) {
//                account.setBalance(account.getBalance().add(amount));  // Increase
//            } else {
//                account.setBalance(account.getBalance().subtract(amount));  // Decrease
//            }
//        }
//    }
//}
