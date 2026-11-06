package PresentationLayer;
import BusinessLayer.Instructor;
import BusinessLayer.Session;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Update_DeleteInstructorPage {

    private static boolean editing;

    public static Stage showUpdate_DeleteInstructorPage(
            Window owner,
            Instructor selectedInstructor,
            TableView<Instructor> instructorTable,
            ObservableList<Instructor> instructors) {

        editing = false;

        Stage updateDeleteInstructorStage = new Stage();

        updateDeleteInstructorStage.initModality(
                Modality.APPLICATION_MODAL
        );

        updateDeleteInstructorStage.initOwner(owner);

        VBox root = instructorDetail(
                selectedInstructor,
                instructors,
                instructorTable
        );

        root.setPadding(new Insets(20));

        Scene scene = new Scene(
                root,
                700,
                450
        );

        updateDeleteInstructorStage.setScene(scene);
        updateDeleteInstructorStage.show();

        return updateDeleteInstructorStage;
    }


    private static VBox instructorDetail(
            Instructor instructor,
            ObservableList<Instructor> instructors,
            TableView<Instructor> instructorsTable) {

        VBox card = new VBox(20);

        TextField name =
                UIHelper.lockedField(instructor.getName());

        TextField address =
                UIHelper.lockedField(instructor.getAddress());

        TextField contact =
                UIHelper.lockedField(instructor.getContactInfo());


        name.setEditable(false);
        address.setEditable(false);
        contact.setEditable(false);


        ComboBox<String> qualificationBox =
                UIHelper.createComboBox("");

        qualificationBox.getItems().addAll(
                "Black Belt - 1st Dan",
                "Black Belt - 2nd Dan",
                "Black Belt - 3rd Dan",
                "Black Belt - 4th Dan",
                "Black Belt - 5th Dan",
                "Black Belt - 6th Dan",
                "Black Belt - 7th Dan",
                "Black Belt - 8th Dan",
                "Black Belt - 9th Dan",
                "Black Belt - 10th Dan"
        );

        qualificationBox.setValue(
                instructor.getQualification()
        );

        qualificationBox.setDisable(true);
        qualificationBox.setOpacity(1);


        ComboBox<String> teachingBeltBox =
                UIHelper.createComboBox("");

        teachingBeltBox.getItems().addAll(
                "White",
                "Yellow",
                "Orange",
                "Green",
                "Blue",
                "Brown",
                "Black"
        );

        teachingBeltBox.setValue(
                instructor.getTeachingBelt()
        );

        teachingBeltBox.setDisable(true);
        teachingBeltBox.setOpacity(1);


        VBox instructorInfo = new VBox(10);

        instructorInfo.getChildren().addAll(

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
                        UIHelper.createLabel("Qualification"),
                        qualificationBox
                ),

                new VBox(
                        8,
                        UIHelper.createLabel("Teaching Belt"),
                        teachingBeltBox
                )
        );


        card.getChildren().addAll(
                instructorInfo,

                updateDeleteButtons(
                        instructorsTable,
                        instructors,
                        instructor,
                        name,
                        address,
                        contact,
                        qualificationBox,
                        teachingBeltBox
                )
        );

        return card;
    }


    private static HBox updateDeleteButtons(
            TableView<Instructor> instructorsTable,
            ObservableList<Instructor> instructors,
            Instructor instructor,
            TextField name,
            TextField address,
            TextField contactInfo,
            ComboBox<String> qualification,
            ComboBox<String> teachingBelt) {

        Button updateButton =
                UIHelper.createPrimaryButton(
                        "Update Instructor",
                        165
                );

        Button deleteButton =
                UIHelper.createPrimaryButton(
                        "Delete Instructor",
                        165
                );

        Button cancelButton =
                UIHelper.createSecondaryButton(
                        "Cancel",
                        150
                );


        if (!Session.getCurrentUser().canUpdateInstructor()) {
            updateButton.setDisable(true);
        }

        if (!Session.getCurrentUser().canDeleteInstructor()) {
            deleteButton.setDisable(true);
        }


        updateButton.setOnAction(e -> {

            if (!editing) {

                name.setEditable(true);
                address.setEditable(true);
                contactInfo.setEditable(true);

                qualification.setDisable(false);
                teachingBelt.setDisable(false);

                updateButton.setText("Save Changes");

                editing = true;
            }

            else {

                // Validation
                if (name.getText().isBlank()
                        || address.getText().isBlank()
                        || contactInfo.getText().isBlank()
                        || qualification.getValue() == null
                        || teachingBelt.getValue() == null) {

                    UIHelper.showWarning(
                            updateButton.getScene().getWindow(),
                            "Missing Information",
                            "Complete All Fields",
                            "Please fill in all required fields."
                    );

                    return;
                }


                instructor.setName(
                        name.getText()
                );

                instructor.setAddress(
                        address.getText()
                );

                instructor.setContactInfo(
                        contactInfo.getText()
                );

                instructor.setQualification(
                        qualification.getValue()
                );

                instructor.setTeachingBelt(
                        teachingBelt.getValue()
                );


                boolean saved =
                        instructor.save();


                if (saved) {

                    instructorsTable.refresh();

                    Stage stage =
                            (Stage) updateButton
                                    .getScene()
                                    .getWindow();

                    stage.close();
                }


                updateButton.setText(
                        "Update Instructor"
                );

                editing = false;
            }
        });


        deleteButton.setOnAction(e -> {

            // Instructor cannot be deleted
            // while students are assigned to them.
            if (instructor.getNumberOfStudents() > 0) {

                UIHelper.showWarning(
                        deleteButton.getScene().getWindow(),
                        "Cannot Delete Instructor",
                        "Instructor Has Students",
                        "This instructor cannot be deleted because they still have registered students."
                );

                return;
            }

            boolean result =  UIHelper.confirm(deleteButton
                    .getScene()
                    .getWindow(),"Delete Instructor", "Confirm Delete", "Are you sure you want to delete this instructor?");

            if (result) {

                boolean deleted =
                        instructor.deleteInstructor();


                if (deleted) {

                    instructors.remove(
                            instructor
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

            Stage stage =
                    (Stage) cancelButton
                            .getScene()
                            .getWindow();

            stage.close();
        });


        Region spacer = new Region();

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
}