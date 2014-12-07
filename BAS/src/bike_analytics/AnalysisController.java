package bike_analytics;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

public class AnalysisController extends AnchorPane implements Initializable {
	private Main application;
	
	@FXML
	private Label labelTitle;
	@FXML
	private Label labelVersion;
	@FXML
	private Label labelDirectory;
	@FXML
	private Label labelDate;
	@FXML
	private Label labelDistance;
	@FXML
	private Label labelOdometer;
	@FXML
	private Label labelAvgSpeed;
	@FXML
	private Label labelAvgCad;
	@FXML
	private Label labelMaxSpeed;
	@FXML
	private Label labelMaxCad;
	@FXML
	private Label labelRideTime;
	
	@FXML
	private Button buttonSettings;
	@FXML
	private Button buttonMain;
	@FXML
	private Button buttonDirectory;
	
	@FXML
	private ComboBox<String> comboBoxFilter;
	
	@FXML
	private ListView<File> listViewRides;
	
	final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
	
	@FXML
	private LineChart lineChartRide = new LineChart(xAxis, yAxis);
	
	private XYChart.Series speedSeries;
	private XYChart.Series cadenceSeries;
	
	private File rideDirectory;
	private final static String[] dateFilter = {"All", "This Week", "This Month", "This Year"};
	
	private List<File> listFile = new ArrayList<File>();
	private List<File> listYear = new ArrayList<File>();
	private List<File> listMonth = new ArrayList<File>();
	private List<File> listWeek = new ArrayList<File>();

	
	private ObservableList<String> listFilter = FXCollections.observableArrayList(dateFilter);
	private boolean isValidState = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		labelTitle.setText("Bike Analytics::Analysis");
		comboBoxFilter.getItems().addAll(listFilter);
		comboBoxFilter.setValue(listFilter.get(0));
				
		listViewRides.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {
			public void changed(ObservableValue<? extends File> ov, File old_val, File new_val) {
				if (isValidState) {
					labelDate.setText(new_val.getName());
					parseJson(new_val);
				}
			}
		});
		
		comboBoxFilter.valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				if (isValidState) {					
					isValidState = false;
					listViewRides.getSelectionModel().clearSelection();
					isValidState = true;
	
					if (new_val.equalsIgnoreCase("this year")) {	
						setYearFilter(new_val);
					}
					else if (new_val.equalsIgnoreCase("all")) {	
						setAllFilter(new_val);
					}
					else if (new_val.equalsIgnoreCase("this month")) {	
						setMonthFilter(new_val);
					}
					else if (new_val.equalsIgnoreCase("this week")) {
						setWeekFilter(new_val);
					}
				}
			}
		});
	}
	
	public void setAllFilter(String s) {
		listViewRides.setItems(FXCollections.observableArrayList(listFile));
	}
	
	public void setWeekFilter(String s) {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		listWeek.clear();
		setMonthFilter("");
		
		for (File f : listMonth) {
			String sub = new String(f.getName());
			if (day - Integer.parseInt(sub.substring(6,8)) <= 7) {
				listWeek.add(f);
			}
		}
		
		listViewRides.setItems(FXCollections.observableArrayList(listWeek));
	}
	
	public void setMonthFilter(String s) {
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		
		listMonth.clear();
		setYearFilter("");
		
		for (File f : listYear) {
			String sub = new String(f.getName());
			if (sub.substring(4, 6).equalsIgnoreCase(Integer.toString(month))) {
				listMonth.add(f);
			}
		}
		
		listViewRides.setItems(FXCollections.observableArrayList(listMonth));
	}
	
	public void setYearFilter(String s) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		listYear.clear();
		
		for (File f : listFile) {
			String sub = new String(f.getName());
			if (sub.substring(0, 4).equalsIgnoreCase(Integer.toString(year))) {
				listYear.add(f);
			}
		}
		
		listViewRides.setItems(FXCollections.observableArrayList(listYear));
	}
	
	public void setGraph(Ride ride) {
		lineChartRide.getData().removeAll(speedSeries);
		lineChartRide.getData().removeAll(cadenceSeries);

		speedSeries = new XYChart.Series();
		cadenceSeries = new XYChart.Series();
		
		speedSeries.setName("Speed (mph)");
		cadenceSeries.setName("Cadence (rpm)");
		
		int i = 0;
		for (Averages a : ride.getAverages()) {
			speedSeries.getData().add(new XYChart.Data(i*3+3, a.getSpeed()));
			cadenceSeries.getData().add(new XYChart.Data(i*3+3, a.getCadence()));
			++i;
		}
		
		lineChartRide.getData().add(speedSeries);
		lineChartRide.getData().add(cadenceSeries);
	}
	
	public void parseJson(File f) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			Ride ride = mapper.readValue(f, Ride.class);
			setLabels(ride);
			setGraph(ride);
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@FXML
	private void handleSettings(ActionEvent event) {
		application.goToSettings();
	}
	
	@FXML
	private void handleMain(ActionEvent event) {
		application.goToMain();
	}
	
	@FXML
	private void handleDirectory(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Ride Data Directory");
		rideDirectory = directoryChooser.showDialog(null);
		
		listFile.clear();
		isValidState = false;
		
		if (rideDirectory != null) {
			labelDirectory.setText(rideDirectory.getPath());
			labelDirectory.setTooltip(new Tooltip(rideDirectory.getPath()));
			labelDirectory.getTooltip().setAutoHide(true);
			
			comboBoxFilter.setValue(listFilter.get(0));
			
			for (File f : rideDirectory.listFiles()) {
				String extension = "";

				int i;
				if ((i = f.getName().lastIndexOf('.')) > 0) {
				    extension = f.getName().substring(i+1);
				}
				
				if (extension.equalsIgnoreCase("json")) {
					listFile.add(f);
				}
			}
			
			listViewRides.setItems(FXCollections.observableArrayList(listFile));
			
			if (listFile.size() > 0) {
				isValidState = true;
			}
			else {
				clearLabels();
			}
		}
		else {
			labelDirectory.setText("No directory selected");
			labelDirectory.setTooltip(new Tooltip(""));
			Tooltip.uninstall(labelDirectory, labelDirectory.getTooltip());
			labelDirectory.getTooltip().setAutoHide(true);
			
			comboBoxFilter.setValue(listFilter.get(0));
			
			listViewRides.setItems(FXCollections.observableArrayList(listFile));
			clearLabels();
		}
	}
	
	public void setLabels(Ride ride) {
		labelDate.setText("Ride Date: " + ride.getRideDate());
		labelDistance.setText("Distance: " + ride.getDistance() + " miles");
		labelOdometer.setText("Odometer-to-Date: " + ride.getOdometer() + " miles");
		labelAvgSpeed.setText("Average Speed: " + truncateDecimal(ride.calcAverages()[0], 2) + " mph");
		labelAvgCad.setText("Average Cadence: " + truncateDecimal(ride.calcAverages()[1], 2) + " rpm");
		labelMaxSpeed.setText("Maximum Speed: " + ride.getMaxSpeed() + " mph");
		labelMaxCad.setText("Maximum Cadence: " + ride.getMaxCadence() + " rpm");
		labelRideTime.setText("Ride Time: " + ride.getRideTime() + " (HH:MM:SS)");
	}
	
	public void clearLabels() {
		lineChartRide.getData().removeAll(speedSeries);
		lineChartRide.getData().removeAll(cadenceSeries);
		
		labelDate.setText("Ride Date:");
		labelDistance.setText("Distance:");
		labelOdometer.setText("Odometer-to-Date:");
		labelAvgSpeed.setText("Average Speed:");
		labelAvgCad.setText("Average Cadence:");
		labelMaxSpeed.setText("Maximum Speed:");
		labelMaxCad.setText("Maximum Cadence:");
		labelRideTime.setText("Ride Time:");
	}
	
	private static BigDecimal truncateDecimal(double x, int numberofDecimals) {
	    if ( x > 0) {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
	    }
	    else {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
	    }
	}
	
	public void setApp(Main application) {
		this.application = application;
	}
}