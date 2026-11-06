package PresentationLayer;

import BusinessLayer.Instructor;
import BusinessLayer.Member;
import BusinessLayer.Session;
import BusinessLayer.SubscriptionPeriod;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;

public class Update_DeleteMemberPage {

    private static boolean editing;


    public static Stage showUpdate_DeleteMemberPage(
            Window owner,
            Member selectedMember,
            TableView<Member> memberTable,
            ObservableList<Member> members) {

        editing = false;

        Stage updateDeleteMemberStage =
                new Stage();

        updateDeleteMemberStage.initModality(
                Modality.APPLICATION_MODAL
        );

        updateDeleteMemberStage.initOwner(
                owner
        );


        VBox root =
                memberDetail(
                        selectedMember,
                        members,
                        memberTable
                );

        root.setPadding(
                new Insets(20)
        );


        Scene scene =
                new Scene(
                        root,
                        700,
                        450
                );

        updateDeleteMemberStage.setScene(
                scene
        );

        updateDeleteMemberStage.show();

        return updateDeleteMemberStage;
    }


    private static VBox memberDetail(
            Member member,
            ObservableList<Member> members,
            TableView<Member> membersTable) {

        VBox card =
                new VBox(20);

        HBox memberInfo =
                new HBox(20);

        VBox leftSide =
                new VBox(10);

        VBox rightSide =
                new VBox(10);


        // ================= MEMBER INFORMATION =================

        TextField name =
                UIHelper.lockedField(
                        member.getName()
                );

        TextField address =
                UIHelper.lockedField(
                        member.getAddress()
                );

        TextField contact =
                UIHelper.lockedField(
                        member.getContactInfo()
                );

        TextField emergencyContact =
                UIHelper.lockedField(
                        member.getEmergencyContactInfo()
                );


        name.setEditable(false);
        address.setEditable(false);
        contact.setEditable(false);
        emergencyContact.setEditable(false);


        // ================= BELT =================

        ComboBox<String> beltBox =
                UIHelper.createComboBox("");

        beltBox.getItems().addAll(
                "White",
                "Yellow",
                "Orange",
                "Green",
                "Blue",
                "Brown",
                "Black"
        );

        beltBox.setValue(
                member.getBelt()
        );

        beltBox.setDisable(true);
        beltBox.setOpacity(1);


        // ================= SUBSCRIPTION =================

        ComboBox<String> subscriptionPeriod =
                UIHelper.createComboBox("");

        subscriptionPeriod.getItems().addAll(
                "Month",
                "Half Year",
                "Year"
        );

        subscriptionPeriod
                .getSelectionModel()
                .select(
                        member
                                .getSubscriptionPeriod()
                                .getPeriodType()
                );

        subscriptionPeriod.setDisable(true);
        subscriptionPeriod.setOpacity(1);


        // ================= START DATE =================

        DatePicker startDate =
                UIHelper.createDatePicker();

        startDate.setValue(
                member
                        .getSubscriptionPeriod()
                        .getStartDate()
        );

        startDate.setDisable(true);
        startDate.setOpacity(1);


        // ================= INSTRUCTOR =================

        Instructor currentInstructor =
                Instructor.find(
                        member.getInstructorID()
                );


        ComboBox<String> instructorsBox =
                UIHelper.createComboBox("");

        instructorsBox.setValue(
                currentInstructor.getName()
        );

        instructorsBox.setDisable(true);
        instructorsBox.setOpacity(1);


        // Show instructors for the current belt
        ArrayList<Instructor> instructors =
                Instructor.getInstructorsByBelt(
                        member.getBelt()
                );


        for (Instructor instructor : instructors) {

            instructorsBox
                    .getItems()
                    .add(
                            instructor.getName()
                    );
        }


        // If belt changes,
        // update the instructors list.
        beltBox.setOnAction(e -> {

            String selectedBelt =
                    beltBox.getValue();


            ArrayList<Instructor> instructorsByBelt =
                    Instructor.getInstructorsByBelt(
                            selectedBelt
                    );


            instructorsBox
                    .getItems()
                    .clear();

            instructorsBox.setValue(null);


            for (Instructor instructor : instructorsByBelt) {

                instructorsBox
                        .getItems()
                        .add(
                                instructor.getName()
                        );
            }
        });


        // ================= LAYOUT =================

        rightSide.getChildren().addAll(

                UIHelper.eachField(
                        "Emergency Contact Info",
                        emergencyContact
                ),

                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Belt Level"
                        ),
                        beltBox
                ),

                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Instructor"
                        ),
                        instructorsBox
                ),

                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Subscription Period Type"
                        ),
                        subscriptionPeriod
                )
        );


        leftSide.getChildren().addAll(

                UIHelper.eachField(
                        "Full Name",
                        name
                ),

                UIHelper.eachField(
                        "Address",
                        address
                ),

                UIHelper.eachField(
                        "Contact Info",
                        contact
                ),

                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Start Date"
                        ),
                        startDate
                )
        );


        HBox.setHgrow(
                leftSide,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                rightSide,
                Priority.ALWAYS
        );


        leftSide.setMaxWidth(
                Double.MAX_VALUE
        );

        rightSide.setMaxWidth(
                Double.MAX_VALUE
        );


        memberInfo.getChildren().addAll(
                leftSide,
                rightSide
        );


        card.getChildren().addAll(

                memberInfo,

                updateDeleteButtons(
                        membersTable,
                        members,
                        member,
                        name,
                        address,
                        contact,
                        emergencyContact,
                        beltBox,
                        instructorsBox,
                        startDate,
                        subscriptionPeriod
                )
        );


        return card;
    }


    private static HBox updateDeleteButtons(
            TableView<Member> membersTable,
            ObservableList<Member> members,
            Member member,
            TextField name,
            TextField address,
            TextField contactInfo,
            TextField emergency,
            ComboBox<String> belt,
            ComboBox<String> instructor,
            DatePicker startDate,
            ComboBox<String> period) {


        // ================= BUTTONS =================

        Button updateButton =
                UIHelper.createPrimaryButton(
                        "Update Member",
                        165
                );

        Button deleteButton =
                UIHelper.createPrimaryButton(
                        "Delete Member",
                        165
                );

        Button promoteButton =
                UIHelper.createPrimaryButton(
                        "Promote Belt",
                        150
                );

        Button cancelButton =
                UIHelper.createSecondaryButton(
                        "Cancel",
                        150
                );


        // ================= PERMISSIONS =================

        if (!Session
                .getCurrentUser()
                .canUpdateMember()) {

            updateButton.setDisable(true);
            promoteButton.setDisable(true);
        }


        if (!Session
                .getCurrentUser()
                .canDeleteMember()) {

            deleteButton.setDisable(true);
        }


        // ================= PROMOTE =================

        promoteButton.setOnAction(e -> {

            PromoteBeltPage.showPromoteBeltPage(
                    promoteButton
                            .getScene()
                            .getWindow(),

                    member,

                    membersTable
            );
        });


        // ================= UPDATE =================

        updateButton.setOnAction(e -> {

            if (!editing) {

                name.setEditable(true);
                address.setEditable(true);
                contactInfo.setEditable(true);
                emergency.setEditable(true);

                belt.setDisable(false);
                instructor.setDisable(false);
                startDate.setDisable(false);
                period.setDisable(false);

                updateButton.setText(
                        "Save Changes"
                );

                editing = true;
            } else {

                // ================= VALIDATION =================

                if (name.getText().isBlank()
                        || address.getText().isBlank()
                        || contactInfo.getText().isBlank()
                        || emergency.getText().isBlank()
                        || belt.getValue() == null
                        || instructor.getValue() == null
                        || startDate.getValue() == null
                        || period.getValue() == null) {

                    UIHelper.showWarning(
                            updateButton
                                    .getScene()
                                    .getWindow(),

                            "Missing Information",
                            "Complete All Fields",
                            "Please fill in all required fields."
                    );

                    return;
                }


                // ================= GET INSTRUCTOR =================

                Instructor selectedInstructor =
                        Instructor.find(
                                instructor.getValue()
                        );


                int oldInstructorID =
                        member.getInstructorID();


                // Check capacity only
                // if instructor was changed.
                if (oldInstructorID
                        != selectedInstructor.getInstructorID()

                        && !selectedInstructor
                        .hasAvailableSpace()) {


                    UIHelper.showWarning(
                            updateButton
                                    .getScene()
                                    .getWindow(),

                            "Instructor Capacity",
                            "Instructor is Full",
                            "This instructor has reached the maximum number of students."
                    );

                    return;
                }


                // ================= UPDATE MEMBER =================

                member.setName(
                        name.getText()
                );

                member.setAddress(
                        address.getText()
                );

                member.setContactInfo(
                        contactInfo.getText()
                );

                member.setEmergencyContactInfo(
                        emergency.getText()
                );

                member.setBelt(
                        belt.getValue()
                );

                member.setInstructorID(
                        selectedInstructor
                                .getInstructorID()
                );

                member.setSubscriptionPeriod(
                        new SubscriptionPeriod(
                                startDate.getValue(),
                                period
                                        .getSelectionModel()
                                        .getSelectedIndex()
                        )
                );


                boolean saved =
                        member.Save();


                if (saved) {

                    membersTable.refresh();

                    Stage stage =
                            (Stage) updateButton
                                    .getScene()
                                    .getWindow();

                    stage.close();
                }


                updateButton.setText(
                        "Update Member"
                );

                editing = false;
            }
        });

        deleteButton.setOnAction(e -> {
            boolean result = UIHelper.confirm(deleteButton
                            .getScene()
                            .getWindow(), "Delete Member"
                    , "Confirm Delete", "Are you sure you want to delete this member?");

            if (result ) {

                boolean deleted =
                        member.deleteMember();


                if (deleted) {

                    members.remove(
                            member
                    );

                    Stage stage =
                            (Stage) deleteButton
                                    .getScene()
                                    .getWindow();

                    stage.close();
                }
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
                deleteButton,
                updateButton,
                promoteButton
        );
    }
}