package com.example.cafepaykict;

import java.util.ArrayList;

public class TransactionDataHandler {

    private ArrayList<Transaction> transactions;

    public TransactionDataHandler() {
        this.transactions = new ArrayList<>();
    }

    // Add a new transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Get all transactions
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    // Find a transaction by its ID
    public Transaction findTransactionById(String id) {
        for (Transaction t : transactions) {
            if (t.getTransactionId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    // Get transactions filtered by type (e.g., "sale", "refund")
    public ArrayList<Transaction> getTransactionsByType(String type) {
        ArrayList<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase(type)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    // Edit/update a transaction by ID
    public boolean updateTransaction(String id, Transaction updatedTransaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionId().equals(id)) {
                transactions.set(i, updatedTransaction);
                return true;
            }
        }
        return false; // transaction not found
    
    }
}