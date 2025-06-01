package com.example.cafepaykict;

import java.time.Duration;
import java.time.LocalTime;

public class Employee extends User {
    private String employeeId;
    private String position;
    private String wageType;  // "weekly" or "hourly"
    private double wage;
    private double overtimeHours;
    private double overtimeRate;
    private String clockInTime;
    private String clockOutTime;

    public Employee(String employeeId, String username, String password, String name, String role, String position,
                    String wageType, double wage, double overtimeHours, double overtimeRate) {
        super(username, password, name, role);
        this.employeeId = employeeId;
        this.position = position;
        this.wageType = wageType;
        this.wage = wage;
        this.overtimeHours = overtimeHours;
        this.overtimeRate = overtimeRate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWageType() {
        return wageType;
    }

    public void setWageType(String wageType) {
        this.wageType = wageType;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(double overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public String getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public double calculatePay() {
        double basePay = 0;
        if ("weekly".equalsIgnoreCase(wageType)) {
            basePay = wage;
        } else if ("hourly".equalsIgnoreCase(wageType)) {
            basePay = wage * 40;  // assuming 40 hours/week
        }
        double overtimePay = overtimeHours * overtimeRate;
        return basePay + overtimePay;
    }

    public double calculateWorkedHours() {
        if (clockInTime != null && clockOutTime != null) {
            try {
                LocalTime in = LocalTime.parse(clockInTime);
                LocalTime out = LocalTime.parse(clockOutTime);
                Duration duration = Duration.between(in, out);
                return duration.toMinutes() / 60.0;
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return name + " (" + employeeId + ")";
    }
}
