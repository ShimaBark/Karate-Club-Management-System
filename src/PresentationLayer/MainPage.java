package PresentationLayer;

import BusinessLayer.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainPage {

    private static final TextField searchField = new TextField();

    private static final String NORMAL_BUTTON_STYLE =
            "-fx-background-color: transparent;" +
                    "-fx-text-fill: #F2DDE3;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 16px;" +
                    "-fx-alignment: CENTER-LEFT;" +
                    "-fx-padding: 0 15 0 15;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;";

    private static final String ACTIVE_BUTTON_STYLE =
            "-fx-background-color: #96344C;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-alignment: CENTER-LEFT;" +
                    "-fx-padding: 0 15 0 15;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;";

    public static TextField getSearchField() {
        return searchField;
    }

    public static BorderPane createMainRoot(Stage stage) {

        BorderPane root = new BorderPane();

        VBox sideBar = new VBox();
        sideBar.setSpacing(10);
        sideBar.setPadding(new Insets(20, 15, 25, 15));

        VBox contentArea = new VBox();

        contentArea.getChildren().setAll(
                DashboardPage.showDashboardPage()
        );

        searchField.setPromptText("Search...");

        VBox clubHeader = createClubHeader();

        Button dashboardButton =
                pageButton(
                        "Dashboard",
                        "Home",
                        34
                );

        Button membersButton =
                pageButton(
                        "Members",
                        "memberImage",
                        34
                );

        Button instructorsButton =
                pageButton(
                        "Instructors",
                        "Instructor",
                        34
                );

        Button usersButton =
                pageButton(
                        "Users",
                        "User",
                        44
                );

        Button logout = new Button("Log out");

        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setPrefHeight(60);

        logout.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #F2DDE3;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-alignment: CENTER-LEFT;" +
                        "-fx-padding: 0 15 0 15;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        Button[] navigationButtons = {
                dashboardButton,
                membersButton,
                instructorsButton,
                usersButton
        };

        setActiveButton(
                dashboardButton,
                navigationButtons
        );

        dashboardButton.setOnAction(e -> {

            contentArea.getChildren().setAll(
                    DashboardPage.showDashboardPage()
            );

            searchField.setPromptText(
                    "Search..."
            );

            setActiveButton(
                    dashboardButton,
                    navigationButtons
            );
        });

        membersButton.setOnAction(e -> {

            if (Session
                    .getCurrentUser()
                    .canShowMembers()) {

                contentArea.getChildren().setAll(
                        MembersPage.showMembersPage()
                );

                searchField.setPromptText(
                        "Search members..."
                );

                setActiveButton(
                        membersButton,
                        navigationButtons
                );
            }
        });

        instructorsButton.setOnAction(e -> {

            if (Session
                    .getCurrentUser()
                    .canShowInstructors()) {

                contentArea.getChildren().setAll(
                        InstructorPage.showInstructorsPage()
                );

                searchField.setPromptText(
                        "Search Instructors..."
                );

                setActiveButton(
                        instructorsButton,
                        navigationButtons
                );
            }
        });

        usersButton.setOnAction(e -> {

            if (Session
                    .getCurrentUser()
                    .canShowUsers()) {

                contentArea.getChildren().setAll(
                        UserPage.showUsersPage()
                );

                searchField.setPromptText(
                        "Search Users..."
                );

                setActiveButton(
                        usersButton,
                        navigationButtons
                );
            }
        });

        logout.setOnAction(e -> {

            Session.logout();

            Stage currentStage =
                    (Stage) logout
                            .getScene()
                            .getWindow();

            currentStage
                    .getScene()
                    .setRoot(
                            LoginPage.showLoginPage()
                    );
        });

        Region spacer = new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        sideBar.getChildren().addAll(
                clubHeader,
                dashboardButton,
                membersButton,
                instructorsButton,
                usersButton,
                spacer,
                logout
        );

        root.setLeft(sideBar);

        root.setCenter(
                pageContent(contentArea)
        );

        sideBar.prefWidthProperty().bind(
                stage
                        .widthProperty()
                        .multiply(0.18)
        );

        sideBar.setStyle(
                "-fx-background-color: #74182D;"
        );

        return root;
    }

    private static VBox createClubHeader() {

        Image logoImage = new Image(
                MainPage.class
                        .getResource(
                                "/resources/Karate Swoosh Emblem.png"
                        )
                        .toExternalForm()
        );

        ImageView logo = new ImageView(
                logoImage
        );

        logo.setFitWidth(95);
        logo.setFitHeight(95);
        logo.setPreserveRatio(true);

        Label clubName = new Label(
                "KARATE CLUB"
        );

        clubName.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        VBox header = new VBox(
                5,
                logo,
                clubName
        );

        header.setAlignment(
                Pos.CENTER
        );

        header.setPadding(
                new Insets(
                        0,
                        0,
                        20,
                        0
                )
        );

        return header;
    }

    private static Button pageButton(
            String title,
            String iconName,
            double iconSize) {

        Image itemImage = new Image(
                MainPage.class
                        .getResource(
                                "/resources/"
                                        + iconName
                                        + ".png"
                        )
                        .toExternalForm()
        );

        ImageView itemIcon =
                new ImageView(itemImage);

        itemIcon.setFitWidth(
                iconSize
        );

        itemIcon.setFitHeight(
                iconSize
        );

        itemIcon.setPreserveRatio(
                true
        );

        Button page = new Button(
                title
        );

        page.setGraphic(
                itemIcon
        );

        page.setGraphicTextGap(
                16
        );

        page.setMaxWidth(
                Double.MAX_VALUE
        );

        page.setPrefHeight(
                60
        );

        page.setStyle(
                NORMAL_BUTTON_STYLE
        );

        return page;
    }

    private static void setActiveButton(
            Button selectedButton,
            Button[] buttons) {

        for (Button button : buttons) {

            button.setStyle(
                    NORMAL_BUTTON_STYLE
            );
        }

        selectedButton.setStyle(
                ACTIVE_BUTTON_STYLE
        );
    }

    private static VBox welcomeSection() {

        Label welcomeLabel =
                new Label("Welcome Back!");

        welcomeLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2B2B2B;"
        );

        Label description =
                new Label(
                        "Here's an overview of your karate club"
                );

        description.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #8A8A8A;"
        );

        return new VBox(
                welcomeLabel,
                description
        );
    }

    private static HBox searchSection() {

        searchField.setPrefWidth(
                300
        );

        searchField.setPrefHeight(
                40
        );

        searchField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-padding: 0 15;"
        );

        return new HBox(
                searchField
        );
    }

    private static VBox pageContent(
            VBox contentArea) {

        VBox pagePanel = new VBox();

        pagePanel.setPadding(
                new Insets(
                        35,
                        40,
                        30,
                        40
                )
        );

        pagePanel.setFocusTraversable(
                true
        );

        HBox topBar = new HBox();

        VBox welcome =
                welcomeSection();

        topBar.getChildren().addAll(
                welcome,
                searchSection()
        );

        HBox.setHgrow(
                welcome,
                Priority.ALWAYS
        );

        pagePanel.getChildren().addAll(
                topBar,
                contentArea
        );

        pagePanel.setSpacing(
                20
        );

        return pagePanel;
    }
}