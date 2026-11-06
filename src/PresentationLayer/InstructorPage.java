package PresentationLayer;

import BusinessLayer.Instructor;
import BusinessLayer.Member;
import BusinessLayer.Session;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class InstructorPage {

    // List used by the Instructor TableView
    private static final ObservableList<Instructor> instructorsList =
            FXCollections.observableArrayList(
                    Instructor.getAllInstructors()
            );

    // Data used by the members per instructor chart
    private static final XYChart.Series<String, Number> series =
            new XYChart.Series<>();

    // Labels used by the instructor overview
    private static final Label totalInstructorLabel =
            new Label();

    private static final Label totalMembersLabel =
            new Label();

    private static final Label averageLabel =
            new Label();


    public static VBox showInstructorsPage() {

        VBox instructorPage = new VBox(18);

        // Updates the charts when an instructor is added or deleted.
        instructorsList.addListener(
                (ListChangeListener<Instructor>) change -> {
                    refreshCharts();
                }
        );

        instructorPage.getChildren().addAll(
                instructorBar(),
                instructorView(),
                chartsSection()
        );

        return instructorPage;
    }


    private static HBox instructorBar() {

        Label instructorTitle =
                new Label("Instructors");

        Button addInstructorButton =
                new Button("Add New Instructor");

        addInstructorButton.setPrefWidth(165);
        addInstructorButton.setPrefHeight(42);

        if (!Session.getCurrentUser().canAddInstructor()) {
            addInstructorButton.setDisable(true);
        }

        instructorTitle.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );

        addInstructorButton.setStyle(
                "-fx-background-color: #8B1538;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        // Open Add Instructor window
        addInstructorButton.setOnAction(e -> {

            AddInstructor.showAddInstructorForm(
                    addInstructorButton
                            .getScene()
                            .getWindow(),

                    instructorsList
            );
        });

        // Adds space between the label and the button.
        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        return new HBox(
                instructorTitle,
                spacer,
                addInstructorButton
        );
    }


    private static TableView<Instructor> instructorView() {

        TableView<Instructor> instructorsTable =
                new TableView<>();

        TableColumn<Instructor, Integer> idColumn =
                new TableColumn<>("ID");

        TableColumn<Instructor, String> nameColumn =
                new TableColumn<>("Name");

        TableColumn<Instructor, String> addressColumn =
                new TableColumn<>("Address");

        TableColumn<Instructor, String> contactColumn =
                new TableColumn<>("Contact");

        TableColumn<Instructor, String> qualificationColumn =
                new TableColumn<>("Qualification");


        idColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getInstructorID()
                ).asObject()
        );

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        addressColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getAddress()
                )
        );

        contactColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getContactInfo()
                )
        );

        qualificationColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getQualification()
                )
        );


        instructorsTable.getColumns().addAll(
                idColumn,
                nameColumn,
                addressColumn,
                contactColumn,
                qualificationColumn
        );

        instructorsTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        // Connects TableView with ObservableList.
        instructorsTable.setItems(instructorsList);


        instructorsTable.setOnMouseClicked(e -> {

            Instructor selectedInstructor =
                    instructorsTable
                            .getSelectionModel()
                            .getSelectedItem();

            if (selectedInstructor != null) {

                Update_DeleteInstructorPage
                        .showUpdate_DeleteInstructorPage(

                                instructorsTable
                                        .getScene()
                                        .getWindow(),

                                selectedInstructor,

                                instructorsTable,

                                instructorsList
                        );
            }
        });

        return instructorsTable;
    }


    private static HBox chartsSection() {

        VBox membersChart =
                membersPerInstructorChart();

        VBox overviewCard =
                instructorOverview();

        HBox charts =
                new HBox(
                        20,
                        membersChart,
                        overviewCard
                );

        HBox.setHgrow(
                membersChart,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                overviewCard,
                Priority.ALWAYS
        );

        membersChart.setMaxWidth(
                Double.MAX_VALUE
        );

        overviewCard.setMaxWidth(
                Double.MAX_VALUE
        );

        return charts;
    }


    // =========================
    // Members Per Instructor
    // =========================

    private static VBox membersPerInstructorChart() {

        Label titleLabel =
                new Label("Members per Instructor");

        titleLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );

        // Instructor name
        CategoryAxis xAxis =
                new CategoryAxis();

        // Number of members
        NumberAxis yAxis =
                new NumberAxis();

        BarChart<String, Number> barChart =
                new BarChart<>(
                        xAxis,
                        yAxis
                );

        barChart.setLegendVisible(false);

        // Disables chart animation when the data changes.
        barChart.setAnimated(false);

        barChart.setStyle(
                "CHART_COLOR_1: #8B1538;"
        );

        // Loads the current chart data.
        refreshCharts();

        barChart.getData().add(series);

        VBox chartCard =
                new VBox(
                        18,
                        titleLabel,
                        barChart
                );

        chartCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-radius: 12;"
        );

        chartCard.setPadding(
                new Insets(15)
        );

        return chartCard;
    }


    // =========================
    // Instructor Overview
    // =========================

    private static VBox instructorOverview() {

        Label titleLabel =
                new Label("Instructor Overview");

        titleLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );

        // Loads the current overview data.
        refreshCharts();

        totalInstructorLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: #3A171E;"
        );

        totalMembersLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: #3A171E;"
        );

        averageLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: #3A171E;"
        );

        VBox overviewCard =
                new VBox(
                        20,
                        titleLabel,
                        totalInstructorLabel,
                        totalMembersLabel,
                        averageLabel
                );

        overviewCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-radius: 12;"
        );

        overviewCard.setPadding(
                new Insets(15)
        );

        return overviewCard;
    }


    // =========================
    // Refresh Charts
    // =========================

    public static void refreshCharts() {

        // Updates members per instructor chart.
        series.getData().clear();

        ArrayList<Member> members =
                Member.getAllMembers();

        for (Instructor instructor : instructorsList) {

            long memberCount =
                    members.stream()
                            .filter(member ->
                                    member.getInstructorID()
                                            ==
                                            instructor.getInstructorID()
                            )
                            .count();

            series.getData().add(
                    new XYChart.Data<>(
                            instructor.getName(),
                            memberCount
                    )
            );
        }


        // Updates instructor overview.
        int totalInstructors =
                instructorsList.size();

        int totalMembers =
                members.size();

        double averageMembers = 0;

        if (totalInstructors > 0) {

            averageMembers =
                    (double) totalMembers
                            /
                            totalInstructors;
        }

        totalInstructorLabel.setText(
                "Total Instructors: "
                        + totalInstructors
        );

        totalMembersLabel.setText(
                "Total Members: "
                        + totalMembers
        );

        averageLabel.setText(
                "Average Members per Instructor: "
                        + String.format(
                        "%.1f",
                        averageMembers
                )
        );
    }

}