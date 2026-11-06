package PresentationLayer;
import javafx.scene.control.*;
import BusinessLayer.Instructor;
import BusinessLayer.SubscriptionPeriod;
import BusinessLayer.Member;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class AddMember {
    public static Stage showAddMemberForm(Window owner, ObservableList<Member> member) {
        Stage addMemberStage = new Stage();
        // Prevents the user from interacting with other windows until this window is closed.
        addMemberStage.initModality(Modality.APPLICATION_MODAL);
        // Sets the parent window for this window.
        addMemberStage.initOwner(owner);

        VBox root = memberDetail(member);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 700, 450);
        addMemberStage.setScene(scene);
        addMemberStage.show();
        return addMemberStage;
    }

    private static HBox saveAndCancelButton(ObservableList<Member> members, TextField name, TextField address, TextField contactInfo, TextField emergancy, ComboBox<String> belt, ComboBox<String> instructor, DatePicker startDate, ComboBox<String> period) {
        Button saveButton = UIHelper.createPrimaryButton("Add New Member",165);
        Button cancelButton = UIHelper.createSecondaryButton("Cancel",150);

        saveButton.setOnAction(e -> {

            if (name.getText().isBlank()
                    || address.getText().isBlank()
                    || contactInfo.getText().isBlank()
                    || emergancy.getText().isBlank()
                    || belt.getValue() == null
                    || instructor.getValue() == null
                    || startDate.getValue() == null
                    || period.getValue() == null) {

                  UIHelper.showWarning(saveButton.getScene().getWindow(),"Missing Information","Complete All Fields","Please fill in all required fields.");

                return;
            }
            Member member = new Member();
            member.setName(name.getText());
            member.setAddress(address.getText());
            member.setContactInfo(contactInfo.getText());
            member.setEmergencyContactInfo(emergancy.getText());
            member.setBelt(belt.getValue());
            Instructor instructor1 = Instructor.find(instructor.getValue());
            member.setInstructorID(instructor1.getInstructorID());
            member.setSubscriptionPeriod(new SubscriptionPeriod(
                    startDate.getValue(),
                    period.getSelectionModel().getSelectedIndex()
            ));

            if (!instructor1.hasAvailableSpace()) {
                UIHelper.showWarning(saveButton
                        .getScene()
                        .getWindow(), "Instructor Capacity","Instructor is Full","This instructor has reached the maximum number of students."
                );

                return;
            }

            boolean saved = member.Save();
            if (saved) {
                members.add(member);
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }
        });

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return new HBox(10, spacer, cancelButton, saveButton);
    }

    private static VBox memberDetail(ObservableList<Member> members) {
        VBox card = new VBox(20);
        HBox memberInfo = new HBox(20);
        VBox leftSide = new VBox(10);
        // Member information fields
        TextField name = UIHelper.createTextField("Enter member name");
        TextField address =UIHelper.createTextField("Enter member address");
        TextField contact =UIHelper.createTextField("e.g phone or email");
        TextField emergencyContact = UIHelper.createTextField("phone");
        ComboBox<String> beltBox = UIHelper.createComboBox("Select belt level");
        beltBox.getItems().addAll(
                "White",
                "Yellow",
                "Orange",
                "Green",
                "Blue",
                "Brown",
                "Black"
        );
        ComboBox<String> instructorsBox = UIHelper.createComboBox("Select Instructor");
        beltBox.setOnAction(e -> {

            String selectedBelt =
                    beltBox.getValue();

            ArrayList<Instructor> instructors =
                    Instructor.getInstructorsByBelt(
                            selectedBelt
                    );

            instructorsBox.getItems().clear();

            for (Instructor instructor : instructors) {

                instructorsBox.getItems().add(
                        instructor.getName()
                );
            }
        });
        ComboBox<String> subscriptionPeriod = UIHelper.createComboBox("Select a subscription period type");
        subscriptionPeriod.getItems().addAll(
                "Month",
                "Half Year",
                "Year"
        );

        DatePicker startDate = UIHelper.createDatePicker();

        VBox rigthSide = new VBox(10);
        rigthSide.getChildren().addAll(UIHelper.eachField("Emergency Contact info", emergencyContact), new VBox(8, UIHelper.createLabel("Belt Level"), beltBox), new VBox(8, UIHelper.createLabel("Instructor"), instructorsBox), new VBox(8, UIHelper.createLabel("Subscription period type"), subscriptionPeriod));


        leftSide.getChildren().addAll(UIHelper.eachField("Full Name ", name), UIHelper.eachField("Address ", address), UIHelper.eachField("ContactInfo", contact), new VBox(8, UIHelper.createLabel("Start Date"), startDate));

        HBox.setHgrow(leftSide, Priority.ALWAYS);
        HBox.setHgrow(rigthSide, Priority.ALWAYS);

        leftSide.setMaxWidth(Double.MAX_VALUE);
        rigthSide.setMaxWidth(Double.MAX_VALUE);

        memberInfo.getChildren().addAll(leftSide, rigthSide);
        card.getChildren().addAll(memberInfo, saveAndCancelButton(members, name, address, contact, emergencyContact, beltBox, instructorsBox, startDate, subscriptionPeriod));
        return card;
    }

}
