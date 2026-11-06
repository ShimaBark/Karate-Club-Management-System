package PresentationLayer;

import BusinessLayer.Instructor;
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

public class AddInstructor {

    public static Stage showAddInstructorForm(
            Window owner,
            ObservableList<Instructor> instructors) {

        Stage addInstructorStage = new Stage();

        // Prevents the user from interacting with other windows
        // until this window is closed.
        addInstructorStage.initModality(
                Modality.APPLICATION_MODAL
        );

        // Sets the parent window for this window.
        addInstructorStage.initOwner(owner);

        VBox root = instructorDetail(instructors);

        root.setPadding(
                new Insets(20)
        );

        Scene scene =
                new Scene(root, 700, 450);

        addInstructorStage.setScene(scene);
        addInstructorStage.show();

        return addInstructorStage;
    }


    private static VBox instructorDetail(
            ObservableList<Instructor> instructors) {

        VBox card = new VBox(20);
        VBox instructorInfo = new VBox(10);


        // Instructor information fields
        TextField ID =
                UIHelper.createTextField(
                        "Enter instructor ID"
                );

        TextField name =
                UIHelper.createTextField(
                        "Enter instructor name"
                );

        TextField address =
                UIHelper.createTextField(
                        "Enter instructor address"
                );

        TextField contact =
                UIHelper.createTextField(
                        "e.g phone or email"
                );


        ComboBox<String> qualificationLevel =
                UIHelper.createComboBox(
                        "Select qualification level"
                );

        qualificationLevel.getItems().addAll(
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


        ComboBox<String> teachingBelt =
                UIHelper.createComboBox(
                        "Select teaching belt"
                );

        teachingBelt.getItems().addAll(
                "White",
                "Yellow",
                "Orange",
                "Green",
                "Blue",
                "Brown",
                "Black"
        );


        instructorInfo.getChildren().addAll(

                UIHelper.eachField(
                        "Instructor ID",
                        ID
                ),

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
                                "Qualification Level"
                        ),
                        qualificationLevel
                ),

                new VBox(
                        8,
                        UIHelper.createLabel(
                                "Teaching Belt"
                        ),
                        teachingBelt
                )
        );


        instructorInfo.setMaxWidth(
                Double.MAX_VALUE
        );


        card.getChildren().addAll(
                instructorInfo,

                saveAndCancelButton(
                        instructors,
                        ID,
                        name,
                        address,
                        contact,
                        qualificationLevel,
                        teachingBelt
                )
        );

        return card;
    }


    private static HBox saveAndCancelButton(
            ObservableList<Instructor> instructors,
            TextField instructorID,
            TextField name,
            TextField address,
            TextField contactInfo,
            ComboBox<String> qualification,
            ComboBox<String> teachingBelt) {


        Button saveButton =
                UIHelper.createPrimaryButton(
                        "Add New Instructor",
                        165
                );

        Button cancelButton =
                UIHelper.createSecondaryButton(
                        "Cancel",
                        150
                );


        saveButton.setOnAction(e -> {

            // Validation
            if (instructorID.getText().isBlank()
                    || name.getText().isBlank()
                    || address.getText().isBlank()
                    || contactInfo.getText().isBlank()
                    || qualification.getValue() == null
                    || teachingBelt.getValue() == null) {

                UIHelper.showWarning(
                        saveButton.getScene().getWindow(),
                        "Missing Information",
                        "Complete All Fields",
                        "Please fill in all required fields."
                );

                return;
            }


            Instructor instructor =
                    new Instructor();

            instructor.setName(
                    name.getText()
            );

            instructor.setAddress(
                    address.getText()
            );

            instructor.setContactInfo(
                    contactInfo.getText()
            );

            instructor.setInstructorID(
                    Integer.parseInt(
                            instructorID.getText()
                    )
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

                instructors.add(
                        instructor
                );

                Stage stage =
                        (Stage) saveButton
                                .getScene()
                                .getWindow();

                stage.close();
            }
        });


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