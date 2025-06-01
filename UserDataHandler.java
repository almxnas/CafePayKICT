package com.example.cafepaykict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserDataHandler {

    private ArrayList<Employee> employees;

    public UserDataHandler() {
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee emp) {
        employees.add(emp);
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public Employee findEmployeeById(String id) {
        for (Employee e : employees) {
            if (e.getEmployeeId().equals(id)) {
                return e;
            }
        }
        return null;
    }
 
    public void loadUsersFromFile(String filename) {
        employees.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length != 10) {
                    System.out.println("Skipping invalid line (not enough fields): " + line);
                    continue;
                }

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                try {
                    String empId = parts[0];
                    String username = parts[1];
                    String password = parts[2];
                    String name = parts[3];
                    String role = parts[4];
                    String position = parts[5];
                    String wageType = parts[6];
                    double wage = Double.parseDouble(parts[7]);
                    double overtimeHours = Double.parseDouble(parts[8]);
                    double overtimeRate = Double.parseDouble(parts[9]);

                    Employee emp = new Employee(empId, username, password, name, role, position,
                            wageType, wage, overtimeHours, overtimeRate);
                    addEmployee(emp);
                } catch (NumberFormatException nfe) {
                    System.out.println("Skipping invalid line (number format error): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveUsersToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Employee e : employees) {
                String line = String.join(",", e.getEmployeeId(), e.getUsername(), e.getPassword(), e.getName(),
                        e.getRole(), e.getPosition(), e.getWageType(), String.valueOf(e.getWage()),
                        String.valueOf(e.getOvertimeHours()), String.valueOf(e.getOvertimeRate()));
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving users to file: " + e.getMessage());
        }
    }

    public boolean updateEmployee(Employee updatedEmp) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId().equals(updatedEmp.getEmployeeId())) {
                employees.set(i, updatedEmp);
                return true;
            }
        }
        return false;
    }

    public void removeEmployee(String empId) {
        employees.removeIf(e -> e.getEmployeeId().equals(empId));
    }
}
