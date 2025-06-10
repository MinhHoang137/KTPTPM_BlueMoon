package controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import repository.report.ReportRepository;

import java.util.Map;

public class ReportFeeCollectionsController {

    @FXML private ComboBox<String> groupByCombo;
    @FXML private Button showBtn;
    @FXML private Pane chartPane;

    private final ReportRepository repo = new ReportRepository();

    @FXML
    public void initialize() {
        groupByCombo.getSelectionModel().selectFirst();
        showBtn.setOnAction(e -> refreshChart());
    }

    private void refreshChart() {
        chartPane.getChildren().clear();
        String choice = groupByCombo.getValue();
        switch (choice) {
            case "Khoản thu (fee_name)" -> showPieChartByFeeName();
            case "Hộ gia đình"         -> showBarChartByHousehold();
            case "Thời gian"           -> showBarChartByMonth();
            default -> { /* do nothing */ }
        }
    }

    private void showPieChartByFeeName() {
        Map<String, Double> data = repo.getTotalCollectedByFeeName();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        data.forEach((feeName, total) -> pieData.add(new PieChart.Data(feeName + " (" + total + "₫)", total)));

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Tổng thu theo Khoản thu");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(960, 600);
        chartPane.getChildren().add(pieChart);
    }

    private void showBarChartByHousehold() {
        Map<String, Double> data = repo.getTotalCollectedByHousehold();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Hộ gia đình");
        yAxis.setLabel("Tổng số (₫)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Tổng thu theo Hộ gia đình");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Thu hộ gia đình");

        data.forEach((household, total) -> series.getData().add(new XYChart.Data<>(household, total)));
        barChart.getData().add(series);
        barChart.setPrefSize(960, 600);
        chartPane.getChildren().add(barChart);
    }

    private void showBarChartByMonth() {
        Map<String, Double> data = repo.getTotalCollectedByMonth();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tháng (YYYY-MM)");
        yAxis.setLabel("Tổng số (₫)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Tổng thu theo Thời gian (Tháng)");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Thu theo tháng");

        data.forEach((month, total) -> series.getData().add(new XYChart.Data<>(month, total)));
        barChart.getData().add(series);
        barChart.setPrefSize(960, 600);
        chartPane.getChildren().add(barChart);
    }
}
