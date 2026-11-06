package PresentationLayer;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import BusinessLayer.User;
import BusinessLayer.Session;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
public class LoginPage {

    public static StackPane showLoginPage() {


        VBox loginBox = new VBox(12);
        Label title = createLabel("Welcome Back");
        Label description = new Label("Sign in to your account");
        Label usernameLabel=createLabel("Username");
        TextField usernameField = createTextField("Enter username");
        Label passwordLabel = createLabel("Password");
        PasswordField passwordField = new PasswordField();
        Button logButton = loginButton(usernameField,passwordField);
        loginBox.setMaxWidth(420);
        loginBox.setMaxHeight(420);
        loginBox.setPadding(new Insets(40));

        loginBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0, 0, 5);"
        );
        description.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #8A8A8A;"
        );

        passwordField.setPromptText(
                "Enter Password"
        );

        passwordField.setPrefHeight(42);
        passwordField.setMaxWidth(Double.MAX_VALUE);

        passwordField.setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-border-color: #D9D9D9;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 0 12;"
        );

        // ==================== Add Elements ====================

        loginBox.getChildren().addAll(
                title,
                description,
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                logButton
        );

        // login page root
        StackPane loginPage =
                new StackPane(loginBox);

        loginPage.setPadding(
                new Insets(40)
        );

        loginPage.setStyle(
                "-fx-background-color: #F7F5F5;"
        );

        return loginPage;
    }

   private static Button loginButton(TextField usernameField,TextField passwordField)
    {
        Button button =
                new Button("Sign In");

        button.setPrefHeight(44);
        button.setMaxWidth(Double.MAX_VALUE);

        button.setStyle(
                "-fx-background-color: #8B1538;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 7;" +
                        "-fx-cursor: hand;"
        );
        button.setOnAction(e -> {

            User user = User.login(
                    usernameField.getText(),
                    passwordField.getText()
            );

            if (user != null) {

                Session.setCurrentUser(user);
                Stage stage =
                        (Stage) button
                                .getScene()
                                .getWindow();


                // Replaces the Login page with the Main page.
                stage.getScene().setRoot(
                        MainPage.createMainRoot(stage)
                );

            } else {

                Alert alert =
                        new Alert(Alert.AlertType.ERROR);
                alert.initOwner(button.getScene().getWindow());

                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Login");

                alert.setContentText(
                        "Incorrect username or password."
                );

                alert.showAndWait();
            }
        });
        return button;

    }
    private static Label createLabel(String txt)
    {
        Label label =
                new Label(txt);

        label.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );
        return label;

    }

    private static TextField createTextField(String prompt)
    {
        TextField textField =
                new TextField();

        textField.setPromptText(
                prompt
        );

        textField.setPrefHeight(42);
        textField.setMaxWidth(Double.MAX_VALUE);

        textField.setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-border-color: #D9D9D9;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 0 12;"
        );
        return textField;
    }
}