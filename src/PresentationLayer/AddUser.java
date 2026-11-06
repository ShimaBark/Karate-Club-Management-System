package PresentationLayer;

import BusinessLayer.User;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AddUser {

    public static Stage showAddUserForm(
            Window owner,
            ObservableList<User> users) {

        Stage addUserStage = new Stage();

        // Prevents the user from interacting with other windows
        // until this window is closed.
        addUserStage.initModality(
                Modality.APPLICATION_MODAL
        );

        // Sets the parent window for this window.
        addUserStage.initOwner(owner);

        VBox root = userDetail(users);

        root.setPadding(
                new Insets(20)
        );

        Scene scene =
                new Scene(root, 700, 450);

        addUserStage.setScene(scene);
        addUserStage.show();

        return addUserStage;
    }


    private static VBox userDetail(
            ObservableList<User> users) {

        VBox card = new VBox(20);
        VBox userInfo = new VBox(10);


        // ================= USER INFORMATION =================

        TextField username =
                UIHelper.createTextField(
                        "Enter username"
                );


        PasswordField password =
                new PasswordField();

        password.setPromptText(
                "Enter password"
        );

        password.setPrefWidth(300);
        password.setPrefHeight(35);

        password.setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-border-color: #D9D9D9;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 13px;"
        );


        // ================= MEMBER PERMISSIONS =================

        CheckBox showMembers =
                new CheckBox("Show Members");

        CheckBox addMember =
                new CheckBox("Add Member");

        CheckBox updateMember =
                new CheckBox("Update Member");

        CheckBox deleteMember =
                new CheckBox("Delete Member");


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
                new CheckBox("Show Instructors");

        CheckBox addInstructor =
                new CheckBox("Add Instructor");

        CheckBox updateInstructor =
                new CheckBox("Update Instructor");

        CheckBox deleteInstructor =
                new CheckBox("Delete Instructor");


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
                new CheckBox("Show Users");

        CheckBox addUser =
                new CheckBox("Add User");

        CheckBox updateUser =
                new CheckBox("Update User");

        CheckBox deleteUser =
                new CheckBox("Delete User");


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


        // ================= PERMISSIONS LAYOUT =================

        HBox permissions =
                new HBox(
                        35,
                        memberPermissions,
                        instructorPermissions,
                        userPermissions
                );


        userInfo.getChildren().addAll(

                UIHelper.eachField(
                        "Username *",
                        username
                ),

                UIHelper.eachField(
                        "Password *",
                        password
                ),

                permissions
        );


        userInfo.setMaxWidth(
                Double.MAX_VALUE
        );


        card.getChildren().addAll(

                userInfo,

                saveAndCancelButton(
                        users,
                        username,
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


    private static HBox saveAndCancelButton(
            ObservableList<User> users,
            TextField username,
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


        Button saveButton =
                UIHelper.createPrimaryButton(
                        "Add New User",
                        165
                );

        Button cancelButton =
                UIHelper.createSecondaryButton(
                        "Cancel",
                        150
                );


        // ================= SAVE =================

        saveButton.setOnAction(e -> {

            // Check empty fields
            if (username.getText().isBlank()
                    || password.getText().isBlank()) {

                UIHelper.showWarning(
                        saveButton
                                .getScene()
                                .getWindow(),

                        "Missing Information",
                        "Complete All Fields",
                        "Please enter a username and password."
                );

                return;
            }


            // Check duplicate username
            if (User.isExists(username.getText())) {

                UIHelper.showWarning(
                        saveButton
                                .getScene()
                                .getWindow(),

                        "Duplicate User",
                        "Username Already Exists",
                        "Please choose a different username."
                );

                return;
            }


            User user =
                    new User();

            user.setUsername(
                    username.getText()
            );

            user.setPassword(
                    password.getText()
            );


            // ================= MEMBER PERMISSIONS =================

            if (showMembers.isSelected())
                user.allowShowMembers();

            if (addMember.isSelected())
                user.allowAddMember();

            if (updateMember.isSelected())
                user.allowUpdateMember();

            if (deleteMember.isSelected())
                user.allowDeleteMember();


            // ================= INSTRUCTOR PERMISSIONS =================

            if (showInstructors.isSelected())
                user.allowShowInstructors();

            if (addInstructor.isSelected())
                user.allowAddInstructor();

            if (updateInstructor.isSelected())
                user.allowUpdateInstructor();

            if (deleteInstructor.isSelected())
                user.allowDeleteInstructor();


            // ================= USER PERMISSIONS =================

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

                users.add(user);

                Stage stage =
                        (Stage) saveButton
                                .getScene()
                                .getWindow();

                stage.close();
            }
        });


        // ================= CANCEL =================

        cancelButton.setOnAction(e -> {

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
                saveButton
        );
    }
}