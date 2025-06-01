package com.example.cafepaykict;

public class Transaction {
    private String transactionId;
    private String employeeId;
    private String type; 
    private double amount;
    private String description;
    private String date;
    private String status;

    public Transaction(String transactionId, String employeeId, String type, double amount, String description, String date, String status) {
        this.transactionId = transactionId;
        this.employeeId = employeeId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
