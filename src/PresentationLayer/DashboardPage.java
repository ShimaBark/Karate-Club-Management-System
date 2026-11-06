package PresentationLayer;

import BusinessLayer.Instructor;
import BusinessLayer.User;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Priority;
import BusinessLayer.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DashboardPage {
    private DashboardPage() {
    }

    public static VBox showDashboardPage() {
        return new VBox(20, statistics(), membersByBeltChart());
    }


    private static HBox statistics() {
        HBox cards = new HBox(20);

        HBox membersCard = itemCard("memberImage", "Total Mermbers", Member.getAllMembers().size());
        HBox instructorsCard = itemCard("Instructor", "Total Instructors", Instructor.getAllInstructors().size());
        HBox usersCard = itemCard("User", "Total Users", User.getAllUsers().size());

        cards.getChildren().addAll(
                membersCard,
                instructorsCard,
                usersCard
        );
        // Allows the cards to use the available horizontal space.
        HBox.setHgrow(membersCard, Priority.ALWAYS);
        HBox.setHgrow(instructorsCard, Priority.ALWAYS);
        HBox.setHgrow(usersCard, Priority.ALWAYS);

        return cards;

    }

    private static HBox itemCard(String inconName, String title, int num) {
        HBox card = new HBox();

        VBox itemDetail = new VBox();
        Label titleLabel = new Label(title);
        Label numLabel = new Label(String.valueOf(num));


        // Loads the image from the resources folder.
        Image itemImage = new Image(
                DashboardPage.class.getResource("/resources/" + inconName + ".png").toExternalForm()
        );
        // Creates an ImageView to display the image.
        ImageView itemIcon = new ImageView(itemImage);
        // Sets the icon size.
        itemIcon.setFitWidth(55);
        itemIcon.setFitHeight(55);


        titleLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #555555;"
        );
        numLabel.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2B171B;"
        );
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-width: 1;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;"
        );

        itemDetail.getChildren().addAll(titleLabel, numLabel);
        card.getChildren().addAll(itemIcon, itemDetail);

        // Sets the preferred height of the card.
        card.setPrefHeight(120);
        // Allows the card to expand to the maximum available width.
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(15, 20, 15, 20));

        return card;

    }

    private static VBox membersByBeltChart() {

        Label title = new Label("Members by Belt Rank");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart =
                new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        VBox chartCard = new VBox(15);
        title.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3A171E;"
        );
        barChart.setStyle(
                "CHART_COLOR_1: #8B1538;"
        );
        chartCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #EEEEEE;" +
                        "-fx-border-radius: 12;"
        );
        // Hides the label that explains the chart data.
        barChart.setLegendVisible(false);
        // Disables chart animation when the data changes.
        barChart.setAnimated(false);
        // Allows the barChart to expand to the maximum available width.
        barChart.setMaxWidth(Double.MAX_VALUE);
        barChart.setPrefHeight(400);
        String[] belts = {
                "White",
                "Yellow",
                "Orange",
                "Green",
                "Blue",
                "Brown",
                "Black"
        };
        ObservableList<Member> members =
                FXCollections.observableArrayList(
                        Member.getAllMembers()
                );

        for (String belt : belts) {

            int count = (int) members.stream()
                    .filter(member ->
                            member.getBelt()
                                    .equalsIgnoreCase(belt)
                    )
                    .count();

            series.getData().add(
                    new XYChart.Data<>(
                            belt,
                            count
                    )
            );
        }

        barChart.getData().add(series);
        chartCard.getChildren().addAll(title, barChart);
        chartCard.setPadding(
                new Insets(20)
        );

        chartCard.setMaxWidth(
                Double.MAX_VALUE
        );

        // the size between each category
        barChart.setCategoryGap(100);
        return chartCard;
    }

}
