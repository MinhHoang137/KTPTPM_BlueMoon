package controller.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.Pane;
import repository.report.ReportRepository;

import java.util.Map;

public class PopulationByWardController {

    @FXML private Pane chartPane;

    private final ReportRepository repo = new ReportRepository();

    @FXML
    public void initialize() {
        showBarChartByWard();
    }

    private void showBarChartByWard() {
        chartPane.getChildren().clear();
        Map<String, Integer> data = repo.getPopulationByWard();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Phường");
        yAxis.setLabel("Số lượng");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Số lượng nhân khẩu theo Phường");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Dân số");

        data.forEach((ward, count) -> series.getData().add(new XYChart.Data<>(ward, count)));
        barChart.getData().add(series);
        barChart.setPrefSize(960, 650);
        chartPane.getChildren().add(barChart);
    }
}
