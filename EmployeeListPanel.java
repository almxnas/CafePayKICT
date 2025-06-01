package com.example.cafepaykict;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.Optional;

public class EmployeeListPanel {

    private CafePayApp mainApp;
    private Employee currentUser;
    private boolean isHR;

    private ListView<Employee> employeeListView;
    private Button backButton;
    private Button deleteEmployeeBtn;
    private Button addEmployeeBtn;
    private Button clockInBtn;
    private Button clockOutBtn;

    private EmployeeDetailsPanel detailsPanel;

    public EmployeeListPanel(CafePayApp mainApp) {
        this.mainApp = mainApp;
        initializeComponents();
    }

    private void initializeComponents() {
        employeeListView = new ListView<>();
        backButton = new Button("Logout");
        deleteEmployeeBtn = new Button("Delete Employee");
        addEmployeeBtn = new Button("Add Employee");
        clockInBtn = new Button("Clock In");
        clockOutBtn = new Button("Clock Out");

        backButton.setOnAction(e -> mainApp.showLoginScene());
        deleteEmployeeBtn.setOnAction(e -> deleteEmployee());
        addEmployeeBtn.setOnAction(e -> showAddEmployeeDialog());
        clockInBtn.setOnAction(e -> clockIn());
        clockOutBtn.setOnAction(e -> clockOut());
    }

    public void setupForUser(Employee loggedInEmployee) {
        this.currentUser = loggedInEmployee;
        this.isHR = "HR".equalsIgnoreCase(loggedInEmployee.getRole());

        employeeListView.getItems().clear();
        if (isHR) {
            employeeListView.getItems().addAll(mainApp.getUserDataHandler().getEmployees());
        } else {
            employeeListView.getItems().add(loggedInEmployee);
        }
        employeeListView.getSelectionModel().selectFirst();

        deleteEmployeeBtn.setDisable(!isHR);
        addEmployeeBtn.setDisable(!isHR);
    }

    public void showDashboard(Stage primaryStage, EmployeeDetailsPanel detailsPanel) {
        this.detailsPanel = detailsPanel;

        VBox leftPane;
        if (isHR) {
            leftPane = new VBox(10, new Label("Employees:"), employeeListView,
                    addEmployeeBtn, deleteEmployeeBtn, backButton);
        } else {
            leftPane = new VBox(10, new Label("Employee:"), employeeListView,
                    clockInBtn, clockOutBtn, backButton);
        }

        employeeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                detailsPanel.showEmployeeDetails(newVal);
                detailsPanel.populateFields(newVal);
            }
        });

        BorderPane root = new BorderPane();
        root.setLeft(leftPane);
        root.setCenter(detailsPanel.getDetailsGrid());
        root.setBottom(detailsPanel.getEmployeeDetailsArea());
        BorderPane.setMargin(detailsPanel.getEmployeeDetailsArea(), new Insets(10));
        detailsPanel.getEmployeeDetailsArea().setPrefHeight(120);

        if (!employeeListView.getItems().isEmpty()) {
            Employee first = employeeListView.getItems().get(0);
            employeeListView.getSelectionModel().select(first);
            detailsPanel.showEmployeeDetails(first);
        }

        primaryStage.setScene(new Scene(root, 700, 450));
        primaryStage.show();
    }

    private void deleteEmployee() {
        Employee selected = employeeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Employee Selected", "Please select an employee to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete employee " + selected.getName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            mainApp.getUserDataHandler().removeEmployee(selected.getEmployeeId());
            employeeListView.getItems().remove(selected);
            mainApp.getUserDataHandler().saveUsersToFile("users.txt");
        }
    }

    private void showAddEmployeeDialog() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Add Employee");

        TextField idField = new TextField();
        TextField userField = new TextField();
        PasswordField passField = new PasswordField();
        TextField nameField = new TextField();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(new Label("Employee ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(userField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passField, 1, 2);
        grid.add(new Label("Name:"), 0, 3);
        grid.add(nameField, 1, 3);

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(ev -> {
            String empId = idField.getText().trim();
            String uname = userField.getText().trim();
            String pwd = passField.getText().trim();
            String nm = nameField.getText().trim();

            if (empId.isEmpty() || uname.isEmpty() || pwd.isEmpty() || nm.isEmpty()) {
                showAlert("Error", "All fields are required.");
                return;
            }

            if (mainApp.getUserDataHandler().findEmployeeById(empId) != null) {
                showAlert("Error", "Employee ID already exists.");
                return;
            }

            Employee newEmp = new Employee(empId, uname, pwd, nm, "Employee", "", "Hourly", 0.0, 0.0, 0.0);
            mainApp.getUserDataHandler().addEmployee(newEmp);
            employeeListView.getItems().add(newEmp);
            mainApp.getUserDataHandler().saveUsersToFile("users.txt");
            showAlert("Success", "New employee added.");
            popup.close();
        });

        VBox box = new VBox(10, grid, saveBtn);
        box.setPadding(new Insets(10));
        popup.setScene(new Scene(box));
        popup.showAndWait();
    }

    private void clockIn() {
        currentUser.setClockInTime(LocalTime.now().toString());
        currentUser.setClockOutTime(null);
        mainApp.getUserDataHandler().saveUsersToFile("users.txt");
        if (detailsPanel != null) detailsPanel.showEmployeeDetails(currentUser);
        showAlert("Clocked In", "Time: " + currentUser.getClockInTime());
    }

    private void clockOut() {
        if (currentUser.getClockInTime() == null) {
            showAlert("Error", "You must clock in before clocking out.");
            return;
        }
        currentUser.setClockOutTime(LocalTime.now().toString());
        double hoursWorked = currentUser.calculateWorkedHours();
        currentUser.setOvertimeHours(Math.max(0, hoursWorked - 40));
        mainApp.getUserDataHandler().saveUsersToFile("users.txt");
        if (detailsPanel != null) detailsPanel.showEmployeeDetails(currentUser);
        showAlert("Clocked Out", "Time: " + currentUser.getClockOutTime() + "\nHours Worked: " + hoursWorked);
    }

    public ListView<Employee> getEmployeeListView() {
        return employeeListView;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
