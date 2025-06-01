package com.example.cafepaykict;
import javafx.application.Application;
import javafx.stage.Stage;

public class CafePayApp extends Application {
    
    private Stage primaryStage;
    private Authenticator authenticator;
    private UserDataHandler userDataHandler;
    
    private LoginPanel loginPanel;
    private EmployeeListPanel employeeListPanel;
    private EmployeeDetailsPanel employeeDetailsPanel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Welcome to CafePay KICT");

        // Initialize data handlers
        userDataHandler = new UserDataHandler();
        userDataHandler.loadUsersFromFile("users.txt");
        authenticator = new Authenticator(userDataHandler.getEmployees());

        // Initialize panels
        loginPanel = new LoginPanel(this);
        employeeListPanel = new EmployeeListPanel(this);
        employeeDetailsPanel = new EmployeeDetailsPanel(this);

        // Show login scene
        showLoginScene();
    }

    public void showLoginScene() {
        loginPanel.show(primaryStage);
    }

    public void showDashboard(Employee loggedInEmployee) {
        primaryStage.setTitle("CafePay Dashboard - " + loggedInEmployee.getName());
        
        // Set up the dashboard with both panels
        employeeListPanel.setupForUser(loggedInEmployee);
        employeeDetailsPanel.setupForUser(loggedInEmployee);
        
        // Show the combined dashboard
        employeeListPanel.showDashboard(primaryStage, employeeDetailsPanel);
    }

    // Getters for panels to access shared resources
    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public UserDataHandler getUserDataHandler() {
        return userDataHandler;
    }

    public static void main(String[] args) {
        launch(args);
    }
}