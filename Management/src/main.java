import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class main extends Application {
	Stage stage;
	Controller myc;
	static Table table;
	static boolean readError;
	
	public static void main(String[] args) { 
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		FXMLLoader loader = new FXMLLoader();
	    Pane myPane = loader.load(getClass().getResource("View.fxml").openStream());
	    myc = (Controller)loader.getController();
	    Scene scene = new Scene(myPane);
	        		    
	    primaryStage.setTitle("南阳卫校");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    //MenuBar
	    MenuBar menuBar = new MenuBar();
	    menuBar.prefWidthProperty().bind(stage.widthProperty());
	    Menu menuFile = new Menu("File");
	    Menu menuEdit = new Menu("Edit");
	    Menu menuView = new Menu("View");
	    Menu menuClose = new Menu("Close");
	    menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuClose);
	    GridPane root = (GridPane) scene.getRoot();
	    root.getChildren().add(menuBar);   

	    //文件目录，保存，新建，打开，另存为...
	    MenuItem saveMenuItem = new MenuItem("Save");
	    saveMenuItem.setOnAction(myc.save());
	    MenuItem saveAsMenuItem = new MenuItem("Save As...");
	    saveAsMenuItem.setOnAction(myc.saveAs());
	    MenuItem openMenuItem = new MenuItem("Open");
	    openMenuItem.setOnAction(myc.open());
	    MenuItem newMenuItem = new MenuItem("New");
	    newMenuItem.setOnAction(myc.newCallback());
	    MenuItem closeItem = new MenuItem("Close");
	    menuFile.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, closeItem);
	    //结束文件目录
	   
	 }
}
