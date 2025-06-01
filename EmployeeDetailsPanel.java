package com.example.cafepaykict;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.Duration;
import java.time.LocalTime;

public class EmployeeDetailsPanel {
    
    private CafePayApp mainApp;
    private Employee currentUser;
    private boolean isHR;
    
    private TextArea employeeDetailsArea;
    private TextField positionField;
    private ComboBox<String> wageTypeCombo;
    private TextField wageField;
    private TextField overtimeHoursField;
    private TextField overtimeRateField;
    private Button calculatePayBtn;
    private Label payResultLabel;
    private GridPane detailsGrid;

    public EmployeeDetailsPanel(CafePayApp mainApp) {
        this.mainApp = mainApp;
        initializeComponents();
        createDetailsGrid();
    }

    private void initializeComponents() {
        employeeDetailsArea = new TextArea();
        employeeDetailsArea.setEditable(false);

        positionField = new TextField();
        wageTypeCombo = new ComboBox<>();
        wageTypeCombo.getItems().addAll("Hourly", "Weekly");
        wageField = new TextField();
        overtimeHoursField = new TextField();
        overtimeRateField = new TextField();

        calculatePayBtn = new Button("Calculate Pay");
        payResultLabel = new Label();

        // Set event handler
        calculatePayBtn.setOnAction(e -> calculatePay());
    }

    private void createDetailsGrid() {
        detailsGrid = new GridPane();
        detailsGrid.setPadding(new Insets(10));
        detailsGrid.setVgap(10);
        detailsGrid.setHgap(10);
        
        detailsGrid.add(new Label("Position:"), 0, 0);
        detailsGrid.add(positionField, 1, 0);
        detailsGrid.add(new Label("Wage Type:"), 0, 1);
        detailsGrid.add(wageTypeCombo, 1, 1);
        detailsGrid.add(new Label("Wage:"), 0, 2);
        detailsGrid.add(wageField, 1, 2);
        detailsGrid.add(new Label("Overtime Hours:"), 0, 3);
        detailsGrid.add(overtimeHoursField, 1, 3);
        detailsGrid.add(new Label("Overtime Rate:"), 0, 4);
        detailsGrid.add(overtimeRateField, 1, 4);
        detailsGrid.add(calculatePayBtn, 0, 5);
        detailsGrid.add(payResultLabel, 1, 5);
    }

    public void setupForUser(Employee loggedInEmployee) {
        this.currentUser = loggedInEmployee;
        this.isHR = "HR".equalsIgnoreCase(loggedInEmployee.getRole());
        
        // Set field editability based on role
        positionField.setEditable(isHR);
        wageTypeCombo.setDisable(!isHR);
        wageField.setEditable(isHR);
        overtimeHoursField.setEditable(isHR);
        overtimeRateField.setEditable(isHR);
        calculatePayBtn.setDisable(!isHR);
    }

    public void populateFields(Employee employee) {
        positionField.setText(employee.getPosition());
        wageTypeCombo.setValue(employee.getWageType());
        wageField.setText(String.valueOf(employee.getWage()));
        overtimeHoursField.setText(String.valueOf(employee.getOvertimeHours()));
        overtimeRateField.setText(String.valueOf(employee.getOvertimeRate()));
        payResultLabel.setText("");
    }

    private void calculatePay() {
        // Get the currently selected employee from the list panel
        Employee selected = mainApp.getUserDataHandler().getEmployees().stream()
            .filter(emp -> employeeDetailsArea.getText().contains("ID: " + emp.getEmployeeId()))
            .findFirst()
            .orElse(null);
            
        if (selected == null) {
            // Fallback: try to get from the employee list view if available
            showAlert("No Employee Selected", "Please select an employee.");
            return;
        }
        
        try {
            selected.setPosition(positionField.getText().trim());
            selected.setWageType(wageTypeCombo.getValue());
            selected.setWage(Double.parseDouble(wageField.getText().trim()));
            selected.setOvertimeHours(Double.parseDouble(overtimeHoursField.getText().trim()));
            selected.setOvertimeRate(Double.parseDouble(overtimeRateField.getText().trim()));

            mainApp.getUserDataHandler().saveUsersToFile("users.txt");

            double pay = selected.calculatePay();
            payResultLabel.setText(String.format("Total Pay: RM%.2f", pay));
            showEmployeeDetails(selected);
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Please enter valid numbers.");
        }
    }

    public void showEmployeeDetails(Employee emp) {
        String clockIn = emp.getClockInTime() == null ? "N/A" : emp.getClockInTime();
        String clockOut = emp.getClockOutTime() == null ? "N/A" : emp.getClockOutTime();

        String totalHoursStr = "N/A";
        try {
            if (!clockIn.equals("N/A") && !clockOut.equals("N/A")) {
                LocalTime in = LocalTime.parse(clockIn);
                LocalTime out = LocalTime.parse(clockOut);
                Duration duration = Duration.between(in, out);
                long hours = duration.toHours();
                long minutes = duration.toMinutesPart();
                totalHoursStr = hours + " hrs " + minutes + " mins";
            }
        } catch (Exception e) {
            totalHoursStr = "Invalid time data";
        }

        String details = "ID: " + emp.getEmployeeId() + "\n"
                + "Name: " + emp.getName() + "\n"
                + "Position: " + emp.getPosition() + "\n"
                + "Wage Type: " + emp.getWageType() + "\n"
                + "Wage: RM" + emp.getWage() + "\n"
                + "Overtime Hours: " + emp.getOvertimeHours() + "\n"
                + "Overtime Rate: RM" + emp.getOvertimeRate() + "\n"
                + "Clock In: " + clockIn + "\n"
                + "Clock Out: " + clockOut + "\n"
                + "Total Hours Worked: " + totalHoursStr;
        employeeDetailsArea.setText(details);
    }

    public GridPane getDetailsGrid() {
        return detailsGrid;
    }

    public TextArea getEmployeeDetailsArea() {
        return employeeDetailsArea;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}