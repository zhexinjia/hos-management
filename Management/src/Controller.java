import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Controller implements Initializable {
    @FXML private TableView<Person> table;
    @FXML private HBox bar;
    @FXML private TextField searchField;
    
    //error checking info
    private ArrayList<TextField> textFieldsCopy;
    private String errorMessage;
    //private boolean bookChanged;
    
    public Table myTable; // Current open address book
    
    public Controller() {
    		myTable = new Table();
    		//bookChanged = false;
    }
    
    public Table getbook(){
    		return myTable;
    }
    
    @FXML
    public void reload() { // Reloads the contents of book, updating table columns, etc. as necessary
		ArrayList<TableColumn<Person, String>> cols = new ArrayList<TableColumn<Person, String>>();
		ArrayList<TextField> textFields = new ArrayList<TextField>();
		String search = searchField.getText();
		for (int i = 0; i < myTable.fields.size(); ++i) {
			// Set up the table column
			TableColumn<Person, String> col = new TableColumn<Person, String>(myTable.fields.get(i));
			final int index = i;
			col.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Person, String> p) {
					SimpleStringProperty ret = new SimpleStringProperty();
					//ret.setValue(p.getValue().getArrayList().get(index));
					return ret;
				}
			});
			cols.add(col);
			
			// Set up the textfield
			TextField t = new TextField();		
			t.setPromptText(myTable.fields.get(i));
			textFields.add(t);
			
			//bookChanged = true;
			// Used for error checking
			textFieldsCopy = new ArrayList<TextField>(textFields);
		}
   	 
		// Set the table columns
   	 	table.getColumns().setAll(cols);
   	 	
   	 	// Set the text fields
   	 	bar.getChildren().setAll(textFields);

   	 	// Populate tableView
   	 	ObservableList<Person> arr = FXCollections.observableArrayList();
   	 	for (int i = 0; i < myTable.staffs.size(); i++) {
   	 		if (search != "") { // check if search term exists
   	 			String contactFields = "";
   	 			boolean valid = true; // false if contact doesn't contain search term
   	 			/**
	   	 		for (String field : myTable.staffs.get(i).getArrayList()) {
	   	 			if (field != null) {
	   	 				contactFields += field.toLowerCase() + " ";
	   	 			}
	   	 		}
	   	 		**/
	   	 		String[] searchTerms = (search.toLowerCase()).split(" ");

	   	 		for (String term : searchTerms) {
	   	 		 // Contact must contain each of the search terms
	   	   	 		if(!contactFields.contains(term)){ 
	   	   	 			valid = false;
	   	   	 		    break;
	   	   	 		}
	   	 		}
	   	 		if (valid) {
	   	 		    arr.add(myTable.staffs.get(i));
	   	 		}
   	 		} else {
   	 		    arr.add(myTable.staffs.get(i));
   	 		}
   		}
   	 	table.setItems(arr);
   	 	
   	 	// Resize columns to fit screen
   	    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reload();
		//bookChanged = false;
	}
	
	// Called when Enter pressed in search bar
	@FXML
	private void onSearchEnter(ActionEvent ae){
	   reload();
	}
	
	// Called when Clear clicked
	@FXML
	private void clearSearch() {
		searchField.setText("");
		reload();
	}
    
    /* Called when Add button clicked */
    @FXML
    protected void addContact(ActionEvent event) {
    	//move addContact function to addContactFunction(), so it can be called in Main
    	if (!popUpAlert()){
    		addContactFunction();
    	}
    }
    
    protected void addContactFunction(){
    	Person newContact;
        ArrayList<String> fields = new ArrayList<String>();
        for (int i = 0; i < bar.getChildren().size(); ++i) {
        	TextField t = (TextField) bar.getChildren().get(i);
        	fields.add(t.getText());
        }
        //newContact = new Person(fields.iterator());
        //myTable.add(newContact); // Add to contacts
        reload();
    }
    
    /* Called when Delete button clicked */
    @FXML
    protected void deleteContact(ActionEvent event) {
    	Person victim = table.getSelectionModel().getSelectedItem();
    	if (victim != null) {
    		if (deleteConfirmation()) {
		        for (int i=0; i < myTable.staffs.size(); i++) {
		        	if (myTable.staffs.get(i) == victim) {
		        		//myTable.staffs.delete(i);
		        		break; // Prevents deletion of multiple (identical) contacts
		        	}
		        }
		        reload();
    		}
    	}
    }
    
    private boolean deleteConfirmation() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Deletion");
		alert.setHeaderText("");
		alert.setContentText("Are you sure you wish to delete this contact?");
		ButtonType confirmButton = new ButtonType("Yes");
		ButtonType cancelButton = new ButtonType("Cancel");
		alert.getButtonTypes().setAll(confirmButton, cancelButton);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == confirmButton){
	        return true;
		}
		return false;
    }
    
    /* Called when Edit button clicked */
    @FXML
    protected void editContact(ActionEvent event) {
    	final Stage editStage = new Stage();
		editStage.setTitle("Edit");
		editStage.initModality(Modality.APPLICATION_MODAL);
		
        VBox box = new VBox();
        box.setPadding(new Insets(10, 50, 10, 50));
        box.setSpacing(5);
        
		ArrayList<TextField> textFields = new ArrayList<TextField>();
		
		Button saveButton = new Button("Confirm");
		Button cancelButton = new Button("Cancel");
		
    	Person selection = table.getSelectionModel().getSelectedItem();
    	if (selection != null) {
    		/**
	    	ArrayList<String> values = selection.getArrayList();
	    	
	    	// Create text fields and populate with values
			for (int i = 0; i < myTable.fields.size(); ++i) {
				TextField t = new TextField();			
				t.setPromptText(myTable.fields.get(i));
				t.setText(values.get(i));
				textFields.add(t);
			}
			**/
	   	 	box.getChildren().addAll(textFields); // add text fields
	
	        // Called when Save button clicked
	   	    saveButton.setOnAction(new EventHandler<ActionEvent>() {
	   	        @Override public void handle(ActionEvent e) {
	   	        	for (int i=0; i < myTable.staffs.size(); i++) {
	   	         	    if (myTable.staffs.get(i) == selection) {
	   	     			    textFieldsCopy = new ArrayList<TextField>(textFields);
	   	     			    if (!popUpAlert()) { // Validate fields
	   	         		        Person newContact;
	   	                        ArrayList<String> fields = new ArrayList<String>();
	   	                        for (int j = 0; j < textFields.size(); ++j) {
	   	                 	        TextField t = (TextField) textFields.get(j);
	   	                 	        fields.add(t.getText());
	   	                        }
	   	                        //newContact = new Person(fields.iterator());
	   	                        //myTable.replace(i, newContact);
	   	   	                    reload();
	   	   	   	                editStage.close();
	   	         	        }
	   	   	                break;//Prevent editing of multiple(identical) contacts
	   	         	    }
	   	            }
	   	        }
	   	    });
	   	 
	   	    // Called when Cancel button clicked
	   	    cancelButton.setOnAction(new EventHandler<ActionEvent>() {
	   	        @Override public void handle(ActionEvent e) {
	   	            editStage.close();
	   	        }
	   	    });
	   	    
			box.getChildren().addAll(saveButton, cancelButton); // add buttons
	
	        Scene editScene = new Scene(box, 500, 400);
	        editScene.getStylesheets().add("controlStyle.css");
	        editStage.setScene(editScene);
	        editStage.show();
    	}
    }
    
    //below is error checking stuff
    protected boolean getChangeStats(){
    	//return book.hasChanged;
    		return true;
    }
    //entry has to have one name and one filed
    protected boolean entryCheck(){
    	boolean output = false;
    	if (textFieldsCopy.get(0).getText().trim().isEmpty() && 
    			textFieldsCopy.get(1).getText().trim().isEmpty()){
    		return false;
    	}
    	for (int i = 2; i < textFieldsCopy.size(); i++){
    		if (textFieldsCopy.get(i).getText() != null && !textFieldsCopy.get(i).getText().trim().isEmpty()){
    			output = true;
    			break;
    		}
    	}
    	return output;
    }
    
    //field validation
    private boolean validation(){
    	errorMessage = "Please complete or check the formatting of the following fields: \n \n";
    	for (int i = 0; i < textFieldsCopy.size(); i++){
    		if (textFieldsCopy.get(i).getText() != null && !textFieldsCopy.get(i).getText().trim().isEmpty()){
    			//errorMessage += book.verifyAll(book.getFields().get(i), textFieldsCopy.get(i).getText());
    		} else{
    			if (myTable.fields.get(i) != "Address 2") { // Address 2 is optional
                    errorMessage += myTable.fields.get(i) + "\n";
    			}
            }
    	}
    	if (errorMessage.equals("Please complete or check the formatting of the following fields: \n \n")){
    		return true;
    	}
    	return false;
    }
    
    //alert needed if didn't pass validation
    private boolean popUpAlert(){
    	if (!entryCheck()){
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Error");
    		alert.setHeaderText("Invalid contact");
    		alert.setContentText("Please enter a name and at least one field.");
    		alert.showAndWait();
    		return true;
    	}
    	if(!validation()){
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Error");
    		alert.setHeaderText("Invalid information");
    		alert.setContentText(errorMessage);
    		
    		ButtonType saveButton = new ButtonType("Add Anyway");
    		ButtonType cancelButton = new ButtonType("Cancel");
    		alert.getButtonTypes().setAll(saveButton, cancelButton);
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == saveButton){
    			// I removed the add contact code in order for editContact() to use this function.
    			// Because this if statement now returns false, the contact is able to be added
    			// in addContact() if the user clicks "Save"
    	        return false;
    		}
    		return true;
    	}
    	return false;
    }

    // Menu bar
    protected void saveErrorAlert() {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Unable To Save");
		alert.setContentText("There was an error saving to the specified file name. Please try again.");
		alert.showAndWait();
    }
    
    public void openErrorAlert() {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Unable To Open");
		alert.setContentText("There was an error opening the specified file name. Please try again.");
		alert.showAndWait();
    }
    
    private void saveAsCallback() {
		// Get the new filename
		// Init
		final Stage stage = new Stage();
		stage.setTitle("Save As...");
		stage.initModality(Modality.APPLICATION_MODAL);
		
		HBox box = new HBox();
		box.setPadding(new Insets(10, 50, 10, 50));
        box.setSpacing(5);
        
        // Make the field where filename is typed
        TextField fileName = new TextField();
        fileName.setPromptText("File Name");
        box.getChildren().add(fileName);
        
        // Make "save" button
        Button button = new Button("Save");
        button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Close the window getting the file name
				stage.close();
				try {
					myTable.saveAs(fileName.getText());
					//bookChanged = false;
				} catch (Exception e) {
					// Alert the save failed
					saveErrorAlert();
				}
			}
        });
        box.getChildren().add(button);
        
        // Show
        Scene scene = new Scene(box, 350, 75);
        scene.getStylesheets().add("controlStyle.css");
        stage.setScene(scene);
        stage.show();
    }
    
    protected EventHandler<ActionEvent> save() {
    	return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (myTable.filePath != null) {
					try {
						myTable.save();
					} catch (Exception e) {
						saveErrorAlert();
					}
				} else {
					saveAsCallback();
				}
			}
    	};
    }

    protected EventHandler<ActionEvent> saveAs() {
    	return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				saveAsCallback();
			}
    	};
    }
    
    protected EventHandler<ActionEvent> open() {
    	return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Get the new filename
				// Init
				final Stage stage = new Stage();
				stage.setTitle("Open");
				stage.initModality(Modality.APPLICATION_MODAL);
				
				HBox box = new HBox();
				box.setPadding(new Insets(10, 50, 10, 50));
		        box.setSpacing(5);
		        
		        // Make the field where filename is typed
		        TextField fileName = new TextField();
		        fileName.setPromptText("File Name");
		        box.getChildren().add(fileName);
		        
		        // Make "save" button
		        Button button = new Button("Open");
		        button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// Close the window getting the file name
						stage.close();
						
						// First we try to open it. If we can, then we go ahead and open it in a new window.
						boolean canBeRead = true;
						try {
							//new Book(fileName.getText());
						} catch (Exception e1) {
							canBeRead = false;
							openErrorAlert();
						}
						// If we succeeded in reading the book, open in a new window.
						if (canBeRead) {
							try {
								//book = new Book(fileName.getText());
								Runtime.getRuntime().exec("java -jar addressBook.jar " + fileName.getText());
								reload();
							} catch (Exception e) {
								// Alert the save failed
								openErrorAlert();
							}
						}
					}
		        });
		        box.getChildren().add(button);
		        
		        // Show
		        Scene scene = new Scene(box, 350, 75);
		        scene.getStylesheets().add("controlStyle.css");
		        stage.setScene(scene);
		        stage.show();
			}
    	};
    }
    
    protected EventHandler<ActionEvent> newCallback() {
    	return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Runtime.getRuntime().exec("java -jar addressBook.jar");
				} catch (IOException e) {
					System.out.println("Could not open new address book");
				}
			}    		
    	};
    }
}
