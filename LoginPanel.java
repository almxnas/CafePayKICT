package com.example.cafepaykict;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginPanel {
    
    private CafePayApp mainApp;
    
    private ComboBox<String> roleComboBox;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button clearButton;
    private Label loginMessage;

    public LoginPanel(CafePayApp mainApp) {
        this.mainApp = mainApp;
        initializeComponents();
    }

    private void initializeComponents() {
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("HR", "Employee");
        roleComboBox.setValue("Employee");

        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");
        clearButton = new Button("Clear");
        loginMessage = new Label();

        loginButton.setOnAction(e -> handleLogin());
        clearButton.setOnAction(e -> clearLoginFields());
    }

    public void show(Stage primaryStage) {
        Label welcomeLabel = new Label("Welcome to CafePay KICT");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(20));
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);

        loginGrid.add(welcomeLabel, 0, 0, 2, 1);
        loginGrid.add(new Label("Role:"), 0, 1);
        loginGrid.add(roleComboBox, 1, 1);
        loginGrid.add(new Label("Username:"), 0, 2);
        loginGrid.add(usernameField, 1, 2);
        loginGrid.add(new Label("Password:"), 0, 3);
        loginGrid.add(passwordField, 1, 3);
        loginGrid.add(loginButton, 0, 4);
        loginGrid.add(clearButton, 1, 4);
        loginGrid.add(loginMessage, 0, 5, 2, 1);

        primaryStage.setScene(new Scene(loginGrid, 350, 280));
        primaryStage.show();
    }

    private void clearLoginFields() {
        usernameField.clear();
        passwordField.clear();
        loginMessage.setText("");
    }

    private void handleLogin() {
        String role = roleComboBox.getValue();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        Employee loggedInEmployee = mainApp.getAuthenticator().login(username, password, role);

        if (loggedInEmployee != null) {
            mainApp.showDashboard(loggedInEmployee);
        } else {
            loginMessage.setText("Invalid username, password, or role.");
        }
    }
}
