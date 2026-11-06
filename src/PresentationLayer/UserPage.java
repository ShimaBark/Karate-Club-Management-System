package PresentationLayer;
import BusinessLayer.Session;
import BusinessLayer.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class UserPage {

    private static final ObservableList<User> usersList =
            FXCollections.observableList(
                    User.getAllUsers()
            );

    private static final VBox accessDetails = new VBox(18);


    public static VBox showUsersPage() {
        // Has 2 sections tobBar and body
        VBox usersPage = new VBox(18);

        TableView<User> usersTable = userView();
        // The body contains users table  and access details for selected user
        HBox body = new HBox(
                20,
                usersTable,
                accessDetails
        );
        // Add the topBar and body to the box
        usersPage.getChildren().addAll(
                userBar(),
                body
        );
        // Sets the width of each section as a percentage of the available body width.
        usersTable.prefWidthProperty()
                .bind(body.widthProperty().multiply(0.45));

        accessDetails.prefWidthProperty()
                .bind(body.widthProperty().multiply(0.55));

        body.setPrefHeight(650);
        VBox.setVgrow(body, Priority.ALWAYS);

        showEmptyAccessDetails();

        return usersPage;
    }

    private static HBox userBar() {

        Label usersTitle =
                new Label("Users");

        Button addUserButton =
                new Button("Add New User");

        if (!Session.getCurrentUser().canAddUser())
            addUserButton.setDisable(true);
        addUserButton.setPrefWidth(165);
        addUserButton.setPrefHeight(42);


        usersTitle.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );


        addUserButton.setStyle(
                "-fx-background-color: #8B1538;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );


        addUserButton.setOnAction(e -> {

            AddUser.showAddUserForm(
                    addUserButton.getScene().getWindow(),
                    usersList
            );
        });


        // Adds space between the title and the button.
        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        return new HBox(
                usersTitle,
                spacer,
                addUserButton
        );
    }



    private static TableView<User> userView() {

        TableView<User> usersTable =
                new TableView<>();


        TableColumn<User, String> usernameColumn =
                new TableColumn<>("Username");

        TableColumn<User, Integer> permissionColumn =
                new TableColumn<>("Permissions");

        usernameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getUsername()
                )
        );
        permissionColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getUserPermission()
                ).asObject()
        );


        usersTable.getColumns().addAll(
                usernameColumn,
                permissionColumn
        );


        // Makes the columns fill the available table width.
        usersTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        usersTable.setItems(usersList);

        usersTable.setOnMouseClicked(e -> {

            User selectedUser =
                    usersTable
                            .getSelectionModel()
                            .getSelectedItem();


            if (selectedUser != null) {

                // one click displays the user's permissions.
                showUserAccessDetails(selectedUser);

                // Double click opens the Update/Delete window.
                if (e.getClickCount() == 2) {
                    if ( Session.getCurrentUser().canUpdateUser() || Session.getCurrentUser().canDeleteUser())
                    {
                        Update_DeleteUserPage.showUpdate_DeleteUserPage(
                                usersTable.getScene().getWindow(),
                                selectedUser,
                                usersTable,
                                usersList
                        );
                    }


                }
            }
        });


        searchUser(usersTable);

        return usersTable;
    }



    private static void searchUser(
            TableView<User> usersTable) {

        TextField searchField =
                MainPage.getSearchField();


        FilteredList<User> filteredUsers =
                new FilteredList<>(
                        usersList,
                        user -> true
                );


        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filteredUsers.setPredicate(user ->

                            user.getUsername()
                                    .toLowerCase()
                                    .contains(
                                            newValue.toLowerCase()
                                    )
                    );
                }
        );


        usersTable.setItems(filteredUsers);
    }



    private static void showEmptyAccessDetails() {

        accessDetails.getChildren().clear();


        Label title =
                new Label("User Access Details");

        title.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );


        Label message =
                new Label(
                        "Select a user from the table to view their permissions."
                );

        message.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #8A8A8A;"
        );


        accessDetails.getChildren().addAll(
                title,
                message
        );


        styleAccessDetails();
    }


    private static void showUserAccessDetails(User user) {

        accessDetails.getChildren().clear();


        Label title =
                new Label("User Access Details");

        title.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );


        Label username =
                new Label(user.getUsername());

        username.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #8B1538;"
        );



        VBox members =
                permissionSection(
                        "Members",
                        "View", user.canShowMembers(),
                        "Add", user.canAddMember(),
                        "Update", user.canUpdateMember(),
                        "Delete", user.canDeleteMember()
                );


        VBox instructors =
                permissionSection(
                        "Instructors",
                        "View", user.canShowInstructors(),
                        "Add", user.canAddInstructor(),
                        "Update", user.canUpdateInstructor(),
                        "Delete", user.canDeleteInstructor()
                );


        VBox users =
                permissionSection(
                        "Users",
                        "View", user.canShowUsers(),
                        "Add", user.canAddUser(),
                        "Update", user.canUpdateUser(),
                        "Delete", user.canDeleteUser()
                );


        HBox permissionGroups =
                new HBox(
                        12,
                        members,
                        instructors,
                        users
                );


        HBox.setHgrow(
                members,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                instructors,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                users,
                Priority.ALWAYS
        );


        members.setMaxWidth(Double.MAX_VALUE);
        instructors.setMaxWidth(Double.MAX_VALUE);
        users.setMaxWidth(Double.MAX_VALUE);

        int memberCount = countMemberPermissions(user);
        int instructorCount = countInstructorPermissions(user);
        int userCount = countUserPermissions(user);

        int totalPermissions =
                memberCount
                        + instructorCount
                        + userCount;



        VBox accessSummary =
                createAccessSummary(
                        totalPermissions,
                        memberCount,
                        instructorCount,
                        userCount
                );


        accessDetails.getChildren().addAll(
                title,
                username,
                permissionGroups,
                accessSummary
        );


        styleAccessDetails();
    }



    private static VBox permissionSection(
            String title,
            String permission1,
            boolean allowed1,
            String permission2,
            boolean allowed2,
            String permission3,
            boolean allowed3,
            String permission4,
            boolean allowed4) {


        Label sectionTitle =
                new Label(title);


        sectionTitle.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );


        VBox section =
                new VBox(
                        12,
                        sectionTitle,
                        permissionLabel(permission1, allowed1),
                        permissionLabel(permission2, allowed2),
                        permissionLabel(permission3, allowed3),
                        permissionLabel(permission4, allowed4)
                );


        section.setPadding(
                new Insets(15)
        );


        section.setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-background-radius: 10;"
        );


        return section;
    }


    // ==================== Permission Label ====================

    private static Label permissionLabel(
            String permission,
            boolean allowed) {

        Label label =
                new Label(
                        (allowed ? "✓  " : "✕  ")
                                + permission
                );


        label.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: " +
                        (allowed
                                ? "#2E7D32;"
                                : "#9E9E9E;")
        );


        return label;
    }


    // ==================== Access Summary ====================

    private static VBox createAccessSummary(
            int total,
            int members,
            int instructors,
            int users) {


        Label title =
                new Label("Overall Access");

        title.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );


        // Overall permissions.
        ProgressBar overallBar =
                createProgressBar(
                        total / 12.0
                );

        Label overallLabel =
                new Label(
                        total + " / 12 Permissions"
                );


        // Member permissions.
        ProgressBar memberBar =
                createProgressBar(
                        members / 4.0
                );

        Label memberLabel =
                new Label(
                        "Members        "
                                + members
                                + " / 4"
                );


        // Instructor permissions.
        ProgressBar instructorBar =
                createProgressBar(
                        instructors / 4.0
                );

        Label instructorLabel =
                new Label(
                        "Instructors    "
                                + instructors
                                + " / 4"
                );


        // User permissions.
        ProgressBar userBar =
                createProgressBar(
                        users / 4.0
                );

        Label userLabel =
                new Label(
                        "Users          "
                                + users
                                + " / 4"
                );


        VBox summary =
                new VBox(
                        10,
                        title,

                        overallLabel,
                        overallBar,

                        memberLabel,
                        memberBar,

                        instructorLabel,
                        instructorBar,

                        userLabel,
                        userBar
                );


        summary.setPadding(
                new Insets(15)
        );


        summary.setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-background-radius: 10;"
        );


        return summary;
    }


    // ==================== Progress Bar ====================

    private static ProgressBar createProgressBar(
            double progress) {

        ProgressBar bar =
                new ProgressBar(progress);


        bar.setMaxWidth(
                Double.MAX_VALUE
        );


        bar.setPrefHeight(12);


        bar.setStyle(
                "-fx-accent: #8B1538;"
        );


        return bar;
    }


    // ==================== Count Member Permissions ====================

    private static int countMemberPermissions(
            User user) {

        int count = 0;


        if (user.canShowMembers())
            count++;

        if (user.canAddMember())
            count++;

        if (user.canUpdateMember())
            count++;

        if (user.canDeleteMember())
            count++;


        return count;
    }


    // ==================== Count Instructor Permissions ====================

    private static int countInstructorPermissions(
            User user) {

        int count = 0;


        if (user.canShowInstructors())
            count++;

        if (user.canAddInstructor())
            count++;

        if (user.canUpdateInstructor())
            count++;

        if (user.canDeleteInstructor())
            count++;


        return count;
    }


    // ==================== Count User Permissions ====================

    private static int countUserPermissions(
            User user) {

        int count = 0;


        if (user.canShowUsers())
            count++;

        if (user.canAddUser())
            count++;

        if (user.canUpdateUser())
            count++;

        if (user.canDeleteUser())
            count++;


        return count;
    }


    // ==================== Access Details Style ====================

    private static void styleAccessDetails() {

        accessDetails.setPadding(
                new Insets(25)
        );


        accessDetails.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-radius: 12;"
        );
    }
}