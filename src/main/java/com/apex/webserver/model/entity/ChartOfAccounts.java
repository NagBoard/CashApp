package com.apex.webserver.model.entity;

import com.apex.webserver.model.enums.AccountType;
import jakarta.persistence.Entity;

import java.util.List;

/**
@Entity
public class ChartOfAccounts {
    // Pre-seeded data
    public static List<Account> getDefaultAccounts() {
        return List.of(
                // Assets
                new Account("1000", "Cash in Bank", AccountType.ASSET),
                new Account("1200", "Accounts Receivable", AccountType.ASSET),
                new Account("1500", "Equipment", AccountType.ASSET),

                // Liabilities
                new Account("2000", "Accounts Payable", AccountType.LIABILITY),
                new Account("2100", "Bank Loan", AccountType.LIABILITY),

                // Equity
                new Account("3000", "Owner's Equity", AccountType.EQUITY),

                // Revenue
                new Account("4000", "Sales Revenue", AccountType.REVENUE),

                // Expenses
                new Account("5000", "Rent Expense", AccountType.EXPENSE),
                new Account("5100", "Salary Expense", AccountType.EXPENSE),
                new Account("5200", "Utilities Expense", AccountType.EXPENSE)
        );
    }
}
*/