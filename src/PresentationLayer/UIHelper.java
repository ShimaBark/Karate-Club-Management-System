package PresentationLayer;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
// Shared UI pieces used by all the pages (forms, buttons, alerts). // Change a style here once and it changes everywhere.
public class UIHelper {

    private static final String FIELD_STYLE =
            "-fx-background-color: #F7F5F5;" +
                    "-fx-border-color: #D9D9D9;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 13px;";

    private static final String LABEL_STYLE =
            "-fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #3A171E;";

    private static final String PRIMARY_BUTTON_STYLE =
            "-fx-background-color: #8B1538;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 7;" +
                    "-fx-cursor: hand;";

    private static final String SECONDARY_BUTTON_STYLE =
            "-fx-background-color: #F7F5F5;" +
                    "-fx-text-fill: #3A171E;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-color: #D9D9D9;" +
                    "-fx-border-radius: 7;" +
                    "-fx-background-radius: 7;" +
                    "-fx-cursor: hand;";


    public static Label createLabel(String txt) {
        Label label = new Label(txt);
        label.setStyle(LABEL_STYLE);
        return label;
    }

    public static TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setStyle(FIELD_STYLE);
        textField.setPrefWidth(300);
        textField.setPrefHeight(35);
        return textField;
    }
    public static TextField lockedField(String value) {
        TextField t = UIHelper.createTextField("");
        t.setText(value);
        t.setEditable(false);
        return t;
    }

    public static ComboBox<String> createComboBox(String prompt) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText(prompt);
        comboBox.setStyle(FIELD_STYLE);
        comboBox.setPrefWidth(300);
        comboBox.setPrefHeight(35);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        return comboBox;
    }

    public static DatePicker createDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle(FIELD_STYLE);
        // Styles the text field inside the DatePicker.
        datePicker.getEditor().setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 13px;");
        datePicker.setPrefHeight(35);
        datePicker.setMaxWidth(Double.MAX_VALUE);
        return datePicker;
    }

    // Label on top of any control (TextField, ComboBox, DatePicker...)
    public static VBox eachField(String labelText, Control control) {
        return new VBox(8, createLabel(labelText), control);
    }

// ==================== Buttons ====================

    public static Button createPrimaryButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(42);
        button.setStyle(PRIMARY_BUTTON_STYLE);
        return button;
    }

    public static Button createSecondaryButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(42);
        button.setStyle(SECONDARY_BUTTON_STYLE);
        return button;
    }


    public static void showWarning(Window owner, String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (owner != null)
            alert.initOwner(owner);
        alert.showAndWait();
    }

    public static void showError(Window owner, String title, String header, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (owner != null)
            alert.initOwner(owner);
        alert.showAndWait();
    }

    // Returns true if the user pressed YES.
    public static boolean confirm(Window owner, String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (owner != null)
            alert.initOwner(owner);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }
}