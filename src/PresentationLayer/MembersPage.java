package PresentationLayer;
import BusinessLayer.Instructor;
import BusinessLayer.Session;
import javafx.collections.transformation.FilteredList;
import BusinessLayer.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.collections.ListChangeListener;
import javafx.beans.property.SimpleStringProperty;
import java.time.Month;
import javafx.scene.chart.XYChart;

public class MembersPage {

    public static final ObservableList<Member> membersList =
            FXCollections.observableList(Member.getAllMembers());

    private static final PieChart beltChart = new PieChart();
    private static final XYChart.Series<String, Number> series = new XYChart.Series<>();
    private static final TableView<Member> membersTable = new TableView<>();


    public static VBox showMembersPage() {

        membersList.addListener((ListChangeListener<Member>) change -> {
            refreshCharts();
        });
        VBox memberPage = new VBox(18);

        memberPage.getChildren().addAll(memberBar(), memberView(), MembersPage.chartsSection());
        return memberPage;
    }

    private static HBox memberBar() {
        Label membersTitle = new Label("Members");
        Button addMemberButton = new Button("Add New Member");
        if (!Session.getCurrentUser().canAddMember())
            addMemberButton.setDisable(true);
        addMemberButton.setPrefWidth(165);
        addMemberButton.setPrefHeight(42);
        membersTitle.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );
        addMemberButton.setStyle(
                "-fx-background-color: #8B1538;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        addMemberButton.setOnAction(e -> {

            AddMember.showAddMemberForm(addMemberButton.getScene().getWindow(), membersList);
        });

        // add space between the label and the button
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return new HBox(membersTitle, spacer, addMemberButton);

    }

    private static TableView<Member> memberView() {
        TableView<Member> membersTable = new TableView<>();
        TableColumn<Member, String> keyColumn = new TableColumn<>("Key");
        TableColumn<Member, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Member, String> contactColumn = new TableColumn<>("Contact");
        TableColumn<Member, String> statusColumn = new TableColumn<>("Status");
        TableColumn<Member, String> instructorColumn = new TableColumn<>("Instructor");

        keyColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getMembershipKey()
                )
        );

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        contactColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getContactInfo()
                )
        );

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().isActive() ? "Active" : "Inactive"
                )
        );

        instructorColumn.setCellValueFactory(data ->{
                Instructor instructor = Instructor.find(data.getValue().getInstructorID());

            return new SimpleStringProperty(
                instructor != null
                        ? instructor.getName()
                        : "Not Assigned");}
        );

        membersTable.setOnMouseClicked(e -> {

            Member selectedMember =
                    membersTable.getSelectionModel().getSelectedItem();

            if (selectedMember != null) {

                Update_DeleteMemberPage.showUpdate_DeleteMemberPage(
                        membersTable.getScene().getWindow(),
                        selectedMember,
                        membersTable,
                        membersList
                );
            }
        });

        membersTable.getColumns().addAll(keyColumn, nameColumn, contactColumn, statusColumn, instructorColumn);
        membersTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
        membersTable.setItems(membersList);

        searchMember(membersTable);
        return membersTable;


    }



    public static HBox chartsSection() {
        VBox growthChart = memberGrowthChart();
        VBox beltChart = beltLevelDistribution();

        HBox charts = new HBox(20, growthChart, beltChart);
        HBox.setHgrow(growthChart, Priority.ALWAYS);
        HBox.setHgrow(beltChart, Priority.ALWAYS);

        growthChart.setMaxWidth(Double.MAX_VALUE);
        beltChart.setMaxWidth(Double.MAX_VALUE);
        return charts;
    }

    private static VBox memberGrowthChart() {
        Label titelLabel = new Label("Member Growth Chart");
        titelLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );
        // Month name
        CategoryAxis xAxis = new CategoryAxis();

        // Number of members who join in that month
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> growthChart = new LineChart<>(xAxis, yAxis);
        // Drow several points
        growthChart.setStyle(
                "-fx-background-color: transparent;"
        );




        growthChart.getData().add(series);
        growthChart.setLegendVisible(false);

        VBox chartCard = new VBox(18, titelLabel, growthChart);
        chartCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-radius: 12;"
        );


        chartCard.setPadding(new Insets(15));
        growthChart.setAnimated(false);

        return chartCard;
    }

    private static VBox beltLevelDistribution() {

        Label titelLabel = new Label("Belt Level Distribution");
        titelLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );
        beltChart.setAnimated(false);

        refreshCharts();

        VBox pieChartCard = new VBox(18, titelLabel, beltChart);
        pieChartCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-radius: 12;"
        );
        pieChartCard.setPadding(new Insets(15));
        return pieChartCard;

    }

    private static void refreshCharts() {

        int white, yellow, green, blue, brown, black;
        white = (int) membersList.stream().filter(m -> m.getBelt().equalsIgnoreCase("White")).count();
        yellow = (int) membersList.stream().filter(m -> m.getBelt().equalsIgnoreCase("yellow")).count();
        green = (int) membersList.stream().filter(m -> m.getBelt().equalsIgnoreCase("green")).count();
        blue = (int) membersList.stream().filter(m -> m.getBelt().equalsIgnoreCase("blue")).count();
        brown = (int) membersList.stream().filter(m -> m.getBelt().equalsIgnoreCase("brown")).count();
        black = (int) membersList.stream().filter(m -> m.getBelt().equalsIgnoreCase("black")).count();
        int orange = (int) membersList.stream()
                .filter(m -> m.getBelt().equalsIgnoreCase("Orange"))
                .count();
        beltChart.getData().setAll(
                new PieChart.Data("White", white),
                new PieChart.Data("Yellow", yellow),
                new PieChart.Data("Orange", orange),
                new PieChart.Data("Green", green),
                new PieChart.Data("Blue", blue),
                new PieChart.Data("Brown", brown),
                new PieChart.Data("Black", black)
        );


        series.getData().clear();

        for (Month month : Month.values()) {

            long count = membersList.stream()
                    .filter(m -> m.getSubscriptionPeriod() != null)
                    .filter(m ->
                            m.getSubscriptionPeriod()
                                    .getStartDate()
                                    .getMonth() == month
                    )
                    .count();

            series.getData().add(
                    new XYChart.Data<>(
                            month.toString().substring(0, 3),
                            count
                    )
            );

        }

    }


    private static void searchMember(TableView<Member> membersTable)
    {
        TextField searchField = MainPage.getSearchField();
        FilteredList<Member> filteredMembers =
                new FilteredList<>(membersList, member -> true);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    filteredMembers.setPredicate(member -> {

                        return member.getName()
                                .toLowerCase()
                                .contains(newValue.toLowerCase());
                    });
                }
        );

        membersTable.setItems(filteredMembers);
    }

}
