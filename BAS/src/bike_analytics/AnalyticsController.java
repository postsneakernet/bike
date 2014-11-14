package bike_analytics;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class AnalyticsController extends AnchorPane implements Initializable {
	private Main application;
	
	@FXML
	private Label labelTitle;
	@FXML
	private Button buttonSettings;
	@FXML
	private Button buttonMain;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		labelTitle.setText("Bike Analytics::Analysis");
	}
	
	@FXML
	private void handleSettings(ActionEvent event) {
		application.goToSettings();
	}
	
	@FXML
	private void handleMain(ActionEvent event) {
		application.goToMain();
	}
	
	public void setApp(Main application) {
		this.application = application;
	}
}