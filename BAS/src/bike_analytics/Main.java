package bike_analytics;
	
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	private Stage stage;
	
	public SettingsController sc;
	final private double MIN_WIDTH = 800.0;
	final private double MIN_HEIGHT = 600.0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			stage.setTitle("Bike Analytics");
			stage.setMinWidth(MIN_WIDTH);
			stage.setMinHeight(MIN_HEIGHT);
			
			goToMain();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void goToMain() {
		try {
			MainController gui = (MainController) replaceSceneContent("Main.fxml");
			gui.setApp(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void goToAnalytics() {
		try {
			AnalyticsController gui = (AnalyticsController) replaceSceneContent("Analytics.fxml");
			gui.setApp(this);
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public void goToSettings() {
		try {
			SettingsController gui = (SettingsController) replaceSceneContent("Settings.fxml");
			gui.setApp(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Initializable replaceSceneContent(String fxml) {
		FXMLLoader loader = new FXMLLoader();
		try {
			InputStream in = Main.class.getResourceAsStream(fxml);
			loader.setBuilderFactory(new JavaFXBuilderFactory());
			loader.setLocation(Main.class.getResource(fxml));
			AnchorPane ap = (AnchorPane) loader.load(in);
			in.close();
			Scene scene = new Scene(ap);
			stage.setScene(scene);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return (Initializable) loader.getController();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
