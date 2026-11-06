package PresentationLayer;

import BusinessLayer.Member;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PromoteBeltPage {

    public static Stage showPromoteBeltPage(
            Window owner,
            Member member,
            TableView<Member> membersTable) {

        Stage promoteStage = new Stage();

        promoteStage.initModality(Modality.APPLICATION_MODAL);
        promoteStage.initOwner(owner);

        VBox root = createForm(
                member,
                membersTable,
                promoteStage
        );

        root.setPadding(new Insets(25));

        Scene scene = new Scene(root, 500, 350);

        promoteStage.setScene(scene);
        promoteStage.setTitle("Belt Promotion");
        promoteStage.show();

        return promoteStage;
    }


    private static VBox createForm(
            Member member,
            TableView<Member> membersTable,
            Stage promoteStage) {

        VBox form = new VBox(18);

        Label title =
                createTitle("Promote Member Belt");

        Label memberName =
                createLabel(
                        "Member: " + member.getName()
                );

        Label currentBelt =
                createLabel(
                        "Current Belt: " + member.getBelt()
                );

        String nextBelt =
                member.getNextBelt();

        Label nextBeltLabel =
                createLabel(
                        "Next Belt: "
                                + (nextBelt == null
                                ? "Highest Belt"
                                : nextBelt)
                );

        Label membershipStatus =
                createLabel(
                        "Membership Status: "
                                + (member.isActive()
                                ? "Active"
                                : "Inactive")
                );


        CheckBox passedExam =
                new CheckBox(
                        "Member passed the belt exam"
                );

        passedExam.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #3A171E;"
        );


        HBox buttons =
                createButtons(
                        member,
                        passedExam,
                        membersTable,
                        promoteStage
                );


        form.getChildren().addAll(
                title,
                memberName,
                currentBelt,
                nextBeltLabel,
                membershipStatus,
                passedExam,
                buttons
        );

        return form;
    }


    private static HBox createButtons(
            Member member,
            CheckBox passedExam,
            TableView<Member> membersTable,
            Stage promoteStage) {

        Button promoteButton =
                new Button("Promote Belt");

        Button cancelButton =
                new Button("Cancel");


        promoteButton.setPrefWidth(150);
        promoteButton.setPrefHeight(42);

        cancelButton.setPrefWidth(120);
        cancelButton.setPrefHeight(42);


        promoteButton.setStyle(
                "-fx-background-color: #8B1538;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 7;" +
                        "-fx-cursor: hand;"
        );


        cancelButton.setStyle(
                "-fx-background-color: #F7F5F5;" +
                        "-fx-text-fill: #3A171E;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #D9D9D9;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-cursor: hand;"
        );


        promoteButton.setOnAction(e -> {

            // Check exam result
            if (!passedExam.isSelected()) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.WARNING,
                                "The member must pass the belt exam before promotion.",
                                ButtonType.OK
                        );

                alert.setTitle("Promotion Failed");
                alert.setHeaderText(
                        "Belt Exam Not Passed"
                );

                alert.initOwner(promoteStage);
                alert.showAndWait();

                return;
            }


            // Check membership
            if (!member.isActive()) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.WARNING,
                                "The member must have an active membership before promotion.",
                                ButtonType.OK
                        );

                alert.setTitle("Promotion Failed");
                alert.setHeaderText(
                        "Inactive Membership"
                );

                alert.initOwner(promoteStage);
                alert.showAndWait();

                return;
            }


            // Check if member already has Black Belt
            if (member.getNextBelt() == null) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.INFORMATION,
                                "The member has already reached the highest belt level.",
                                ButtonType.OK
                        );

                alert.setTitle("Belt Promotion");
                alert.setHeaderText(
                        "Highest Belt Reached"
                );

                alert.initOwner(promoteStage);
                alert.showAndWait();

                return;
            }


            boolean promoted =
                    member.promoteBelt();


            if (promoted) {

                membersTable.refresh();

                Alert alert =
                        new Alert(
                                Alert.AlertType.INFORMATION,
                                "The member has been promoted successfully.",
                                ButtonType.OK
                        );

                alert.setTitle("Belt Promotion");
                alert.setHeaderText(
                        "Promotion Successful"
                );

                alert.initOwner(promoteStage);
                alert.showAndWait();

                promoteStage.close();
            }

        });


        cancelButton.setOnAction(e ->
                promoteStage.close()
        );


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
                promoteButton
        );
    }


    private static Label createLabel(String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #3A171E;"
        );

        return label;
    }


    private static Label createTitle(String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );

        return label;
    }
}