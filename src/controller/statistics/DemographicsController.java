package controller.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import repository.report.ReportRepository;

import java.util.Map;

public class DemographicsController {

    @FXML private Button genderBtn;
    @FXML private Button ageBtn;
    @FXML private Button religionBtn;
    @FXML private Pane chartPane;

    private final ReportRepository repo = new ReportRepository();

    @FXML
    public void initialize() {
        genderBtn.setOnAction(e -> showGenderChart());
        ageBtn.setOnAction(e -> showAgeChart());
        religionBtn.setOnAction(e -> showReligionChart());
        // Show gender chart by default:
        showGenderChart();
    }

    private void showGenderChart() {
        chartPane.getChildren().clear();
        Map<String, Integer> data = repo.getPopulationByGender();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        data.forEach((gender, count) -> pieData.add(new PieChart.Data(gender + " (" + count + ")", count)));

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Phân bố Giới tính");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(960, 600);
        chartPane.getChildren().add(pieChart);
    }

    private void showAgeChart() {
        chartPane.getChildren().clear();
        Map<String, Integer> data = repo.getPopulationByAgeGroup();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Nhóm tuổi");
        yAxis.setLabel("Số lượng");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Phân bố theo nhóm tuổi");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nhóm tuổi");

        data.forEach((ageRange, count) -> series.getData().add(new XYChart.Data<>(ageRange, count)));
        barChart.getData().add(series);
        barChart.setPrefSize(960, 600);
        chartPane.getChildren().add(barChart);
    }

    private void showReligionChart() {
        chartPane.getChildren().clear();
        Map<String, Integer> data = repo.getPopulationByReligion();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        data.forEach((religion, count) -> pieData.add(new PieChart.Data(religion + " (" + count + ")", count)));

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Phân bố theo Tôn giáo");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(960, 600);
        chartPane.getChildren().add(pieChart);
    }
}
