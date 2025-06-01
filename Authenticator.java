package com.example.cafepaykict;

import java.util.ArrayList;

public class Authenticator {

    private ArrayList<Employee> employees;

    public Authenticator(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public Employee login(String username, String password, String role) {
        for (Employee emp : employees) {
            if (emp.getUsername().equals(username) &&
                emp.getPassword().equals(password) &&
                emp.getRole().equalsIgnoreCase(role)) {
                return emp;
            }
        }
        return null;
    }
}
