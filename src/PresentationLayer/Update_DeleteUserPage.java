package PresentationLayer;

import BusinessLayer.Session;
import BusinessLayer.User;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Update_DeleteUserPage {

    private static boolean editing;


    public static Stage showUpdate_DeleteUserPage(
            Window owner,
            User selectedUser,
            TableView<User> usersTable,
            ObservableList<User> users) {

        editing = false;

        Stage updateDeleteUserStage =
                new Stage();

        updateDeleteUserStage.initModality(
                Modality.APPLICATION_MODAL
        );

        updateDeleteUserStage.initOwner(owner);


        VBox root =
                userDetail(
                        selectedUser,
                        users,
                        usersTable
                );

        root.setPadding(
                new Insets(20)
        );


        Scene scene =
                new Scene(
                        root,
                        700,
                        550
                );

        updateDeleteUserStage.setScene(scene);
        updateDeleteUserStage.show();

        return updateDeleteUserStage;
    }


    private static VBox userDetail(
            User user,
            ObservableList<User> users,
            TableView<User> usersTable) {

        VBox card =
                new VBox(20);

        VBox userInfo =
                new VBox(15);


        // ================= USER INFORMATION =================

        TextField username =
                UIHelper.lockedField(
                        user.getUsername()
                );

        username.setEditable(false);


        PasswordField password =
                createPasswordField(
                        user.getPassword()
                );

        password.setEditable(false);


        // ================= MEMBER PERMISSIONS =================

        CheckBox showMembers =
                createCheckBox(
                        "Show Members",
                        user.canShowMembers()
                );

        CheckBox addMember =
                createCheckBox(
                        "Add Member",
                        user.canAddMember()
                );

        CheckBox updateMember =
                createCheckBox(
                        "Update Member",
                        user.canUpdateMember()
                );

        CheckBox deleteMember =
                createCheckBox(
                        "Delete Member",
                        user.canDeleteMember()
                );


        VBox memberPermissions =
                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Member Permissions"
                        ),
                        showMembers,
                        addMember,
                        updateMember,
                        deleteMember
                );


        // ================= INSTRUCTOR PERMISSIONS =================

        CheckBox showInstructors =
                createCheckBox(
                        "Show Instructors",
                        user.canShowInstructors()
                );

        CheckBox addInstructor =
                createCheckBox(
                        "Add Instructor",
                        user.canAddInstructor()
                );

        CheckBox updateInstructor =
                createCheckBox(
                        "Update Instructor",
                        user.canUpdateInstructor()
                );

        CheckBox deleteInstructor =
                createCheckBox(
                        "Delete Instructor",
                        user.canDeleteInstructor()
                );


        VBox instructorPermissions =
                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Instructor Permissions"
                        ),
                        showInstructors,
                        addInstructor,
                        updateInstructor,
                        deleteInstructor
                );


        // ================= USER PERMISSIONS =================

        CheckBox showUsers =
                createCheckBox(
                        "Show Users",
                        user.canShowUsers()
                );

        CheckBox addUser =
                createCheckBox(
                        "Add User",
                        user.canAddUser()
                );

        CheckBox updateUser =
                createCheckBox(
                        "Update User",
                        user.canUpdateUser()
                );

        CheckBox deleteUser =
                createCheckBox(
                        "Delete User",
                        user.canDeleteUser()
                );


        VBox userPermissions =
                new VBox(
                        8,
                        UIHelper.createLabel(
                                "User Permissions"
                        ),
                        showUsers,
                        addUser,
                        updateUser,
                        deleteUser
                );


        HBox permissions =
                new HBox(
                        35,
                        memberPermissions,
                        instructorPermissions,
                        userPermissions
                );


        userInfo.getChildren().addAll(

                UIHelper.eachField(
                        "Username",
                        username
                ),

                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Password"
                        ),
                        password
                ),

                permissions
        );


        userInfo.setMaxWidth(
                Double.MAX_VALUE
        );


        card.getChildren().addAll(

                userInfo,

                updateDeleteButtons(
                        usersTable,
                        users,
                        user,
                        password,

                        showMembers,
                        addMember,
                        updateMember,
                        deleteMember,

                        showInstructors,
                        addInstructor,
                        updateInstructor,
                        deleteInstructor,

                        showUsers,
                        addUser,
                        updateUser,
                        deleteUser
                )
        );


        return card;
    }


    private static HBox updateDeleteButtons(
            TableView<User> usersTable,
            ObservableList<User> users,
            User user,
            PasswordField password,

            CheckBox showMembers,
            CheckBox addMember,
            CheckBox updateMember,
            CheckBox deleteMember,

            CheckBox showInstructors,
            CheckBox addInstructor,
            CheckBox updateInstructor,
            CheckBox deleteInstructor,

            CheckBox showUsers,
            CheckBox addUser,
            CheckBox updateUser,
            CheckBox deleteUser) {


        // ================= BUTTONS =================

        Button updateButton =
                UIHelper.createPrimaryButton(
                        "Update User",
                        165
                );

        Button deleteButton =
                UIHelper.createPrimaryButton(
                        "Delete User",
                        165
                );

        Button cancelButton =
                UIHelper.createSecondaryButton(
                        "Cancel",
                        150
                );


        // ================= PERMISSIONS =================

        if (!Session.getCurrentUser().canDeleteUser()) {
            deleteButton.setDisable(true);
        }

        if (!Session.getCurrentUser().canUpdateUser()) {
            updateButton.setDisable(true);
        }


        // ================= UPDATE =================

        updateButton.setOnAction(e -> {

            if (!editing) {

                // Username remains read-only because it is the key.
                password.setEditable(true);

                setPermissionsDisable(
                        false,

                        showMembers,
                        addMember,
                        updateMember,
                        deleteMember,

                        showInstructors,
                        addInstructor,
                        updateInstructor,
                        deleteInstructor,

                        showUsers,
                        addUser,
                        updateUser,
                        deleteUser
                );


                updateButton.setText(
                        "Save Changes"
                );

                editing = true;
            } else {

                // ================= VALIDATION =================

                if (password.getText().isBlank()) {

                    UIHelper.showWarning(
                            updateButton
                                    .getScene()
                                    .getWindow(),

                            "Missing Information",
                            "Password Required",
                            "Password cannot be empty."
                    );

                    return;
                }


                // ================= UPDATE USER =================

                user.setPassword(
                        password.getText()
                );


                // Remove old permissions before adding
                // the currently selected permissions.
                user.clearPermissions();


                // Member permissions
                if (showMembers.isSelected())
                    user.allowShowMembers();

                if (addMember.isSelected())
                    user.allowAddMember();

                if (updateMember.isSelected())
                    user.allowUpdateMember();

                if (deleteMember.isSelected())
                    user.allowDeleteMember();


                // Instructor permissions
                if (showInstructors.isSelected())
                    user.allowShowInstructors();

                if (addInstructor.isSelected())
                    user.allowAddInstructor();

                if (updateInstructor.isSelected())
                    user.allowUpdateInstructor();

                if (deleteInstructor.isSelected())
                    user.allowDeleteInstructor();


                // User permissions
                if (showUsers.isSelected())
                    user.allowShowUsers();

                if (addUser.isSelected())
                    user.allowAddUser();

                if (updateUser.isSelected())
                    user.allowUpdateUser();

                if (deleteUser.isSelected())
                    user.allowDeleteUser();


                boolean saved =
                        user.save();


                if (saved) {

                    usersTable.refresh();

                    Stage stage =
                            (Stage) updateButton
                                    .getScene()
                                    .getWindow();

                    stage.close();
                }


                updateButton.setText(
                        "Update User"
                );

                editing = false;
            }
        });


        // ================= DELETE =================

        deleteButton.setOnAction(e -> {
            boolean result = UIHelper.confirm(deleteButton
                    .getScene()
                    .getWindow(), "Delete User", "Confirm Delete", "Are you sure you want to delete this user?");


            if (result) {

                boolean deleted =
                        user.deleteUser();


                if (deleted) {

                    users.remove(
                            user
                    );

                    Stage stage =
                            (Stage) deleteButton
                                    .getScene()
                                    .getWindow();

                    stage.close();
                }
            }
        });


        cancelButton.setOnAction(e -> {

            editing = false;

            Stage stage =
                    (Stage) cancelButton
                            .getScene()
                            .getWindow();

            stage.close();
        });


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        return new HBox(
                10,
                spacer,
                cancelButton,
                deleteButton,
                updateButton
        );
    }


    // PasswordField stays here because UIHelper
    // does not have createPasswordField().
    private static PasswordField createPasswordField(
            String oldValue) {

        PasswordField passwordField =
                new PasswordField();

        passwordField.setText(
                oldValue
        );

        passwordField.setEditable(
                false
        );

        passwordField.setPrefWidth(
                300
        );

        passwordField.setPrefHeight(
                35
        );

        passwordField.setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-border-color: #D9D9D9;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 13px;"
        );

        return passwordField;
    }


    // Creates a permission CheckBox.
    private static CheckBox createCheckBox(
            String text,
            boolean selected) {

        CheckBox checkBox =
                new CheckBox(text);

        checkBox.setSelected(
                selected
        );

        // Permissions are read-only
        // until Update User is clicked.
        checkBox.setDisable(
                true
        );

        checkBox.setOpacity(
                1
        );

        return checkBox;
    }


    private static void setPermissionsDisable(
            boolean disable,
            CheckBox... permissions) {

        for (CheckBox permission : permissions) {

            permission.setDisable(
                    disable
            );

            permission.setOpacity(
                    1
            );
        }
    }
}