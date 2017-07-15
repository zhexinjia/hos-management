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


public class main extends Application {
	Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("南阳卫校");
	    FXMLLoader loader = new FXMLLoader();
	    Pane myPane = loader.load(getClass().getResource("View.fxml"));	
	    Scene myScene = new Scene(myPane);
	    //myScene.getStylesheets().add("controlStyle.css");
	    primaryStage.setScene(myScene);
	    //primaryStage.setMaximized(true); // Default to full screen
	    primaryStage.show();
	}
	
	/**
	
	Stage stage;
	Controller myc;
	static Table table = new Table();
	static boolean readError;
    
	
    public static void main(String[] args) {
    	// If we have command line args, open that address book
    	readError = false;
        if (args.length == 1) {
        	try {
				table.readFile((args[0]));
			} catch (Exception e) {
				readError = true;
			}
        } else {
        	
        }
        launch(args); // Launch the GUI
    }
    
    	

    @SuppressWarnings("static-access")
	@Override
    public void start(Stage primaryStage) throws Exception {
       stage = primaryStage;
       primaryStage.setTitle("南阳卫校");
       FXMLLoader loader = new FXMLLoader();
       Pane myPane = loader.load(getClass().getResource("View.fxml").openStream());
       myc = (Controller)loader.getController();
       // Set the book that we got from the commandline arguements
       if (readError) {
    	   myc.openErrorAlert();
       } else {
       	   myc.myTable = table;
       	   myc.reload();
       }
       primaryStage.setOnCloseRequest(e->{
    	   e.consume();
    	   //closeProgram();
       });
       Scene myScene = new Scene(myPane);
       myScene.getStylesheets().add("controlStyle.css");
       primaryStage.setScene(myScene);
       //primaryStage.setMaximized(true); // Default to full screen
       primaryStage.show();
       
       // Menu Bar
       MenuBar menuBar = new MenuBar();
       menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
       
       Menu menuFile = new Menu("File");
       MenuItem saveMenuItem = new MenuItem("Save");
       saveMenuItem.setOnAction(myc.save());
       MenuItem saveAsMenuItem = new MenuItem("Save As...");
       saveAsMenuItem.setOnAction(myc.saveAs());
       MenuItem openMenuItem = new MenuItem("Open");
       openMenuItem.setOnAction(myc.open());
       MenuItem newMenuItem = new MenuItem("New");
       newMenuItem.setOnAction(myc.newCallback());
       menuFile.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem);
       menuBar.getMenus().add(menuFile);
       
       GridPane root = (GridPane) myScene.getRoot();
       root.getChildren().add(menuBar);
       root.setRowIndex(menuBar, 0);
    }
    
    
    public void closeProgram(){
    	if (myc.entryCheck() || myc.getChangeStats()){
    		Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setTitle("Exit Confirmation");
	    	alert.setHeaderText("You have unsaved information.");
	    	alert.setContentText("Do you want to save your changes before exiting?");
	    	ButtonType saveButton = new ButtonType("Save");
	    	ButtonType quitButton = new ButtonType("Quit");
	    	ButtonType cancelButton = new ButtonType("Cancel");
	    	alert.getButtonTypes().setAll(saveButton, quitButton, cancelButton);
	    	Optional<ButtonType> result = alert.showAndWait();
	    	if (result.get() == saveButton){
	    		if (myc.entryCheck()){
	    			myc.addContactFunction();
	    		}
	    		//call save function in Controller
	    		if (myc.getbook().filePath != null){
	    			try {
						myc.getbook().save();
					} catch (Exception e) {
						System.out.println("saving error");
					}
	    		}else{
	    			popUpSaveAs();
	    		}
	    		stage.close();
	    	}else if (result.get() == quitButton) {
	    		//user closed the dialog or clicked quit button
	    		stage.close();
	    	} 
	    // If cancel button clicked, no action taken, simply close pop-up
    	}else{
    		stage.close();
    	}
    }
    
    public void popUpSaveAs(){
    	Stage newstage = new Stage();
		newstage.setTitle("Save As");
		newstage.initModality(Modality.APPLICATION_MODAL);
		
		HBox box = new HBox();
		//box.setPadding(new Insets(10, 50, 10, 50));
		//box.setPadding(new Insets(10, 50, 10, 50));
        box.setSpacing(5);
        
        // Make the field where filename is typed
        TextField fileName = new TextField();
        fileName.setPromptText("File Name");
        box.getChildren().add(fileName);
        
        Button button = new Button("Save");
        button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Close the window getting the file name
				stage.close();
				try {
					myc.getbook().saveAs(fileName.getText());
				} catch (Exception e) {
					// Alert the save failed
					myc.saveErrorAlert();
				}
				newstage.close();
			}
        });
        box.getChildren().add(button);
        
        Scene scene = new Scene(box, 350, 75);
        scene.getStylesheets().add("controlStyle.css");
        newstage.setScene(scene);
        newstage.show();
    }
    **/
    
}
	
	
	
	
	
	/**
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		Table table = new Table();
		Person p1 = new Person();
		p1.setName("张三");
		
		p1.setCurrent(0);
		p1.setTotal(0);
		p1.setPosition("1");
		
		Person p2 = new Person();
		p2.setName("Jen");
		p2.setCurrent(5);
		p2.setTotal(10);
		p2.setPosition("1");
		
		Person p3 = new Person();
		p3.setName("Bob");
		p3.setCurrent(0);
		p3.setTotal(0);
		p3.setPosition("2");
		
		table.add(p1);
		table.add(p2);
		table.add(p3);
		
		//System.out.println(table.staffs.get(0).getName() + table.staffs.get(0).getPosition());
		table.saveAs("123.csv");
		table.readFile("123.csv");
		for (int i = 0; i < table.staffs.size(); i++) {
		}
		System.out.println(table.staffs.get(0).getName());
		
	}
}
**/
