//package com.apex.webserver.controller;
//
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/transactions")
//public class TransactionController {
//
//
//    @GetMapping("/api/accounts")
//    public List<AccountDTO> getAllAccounts() {
//        return accountRepo.findAll().stream()
//                .map(account -> AccountDTO.builder()
//                        .id(account.getId())
//                        .name(account.getName())
//                        .type(account.getType())
//                        .balance(account.getBalance())
//                        .debitEffect(account.isDebitIncrease() ? "↑" : "↓")
//                        .creditEffect(account.isCreditIncrease() ? "↑" : "↓")
//                        .build())
//                .collect(Collectors.toList());
//    }
//}
