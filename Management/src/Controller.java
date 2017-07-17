import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Controller implements Initializable {
	@FXML private TableView<Person> tableview;
	@FXML private TextField searchField;
	@FXML private HBox bar;
	
	private static String currentFilePath = null;
	
	private ArrayList<TextField> textFieldsCopy;
	
	Table table;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reload();	
		System.out.println("init done");
	}
	
	
	
	
	public Controller() throws FileNotFoundException {
		table = new Table();
		if (currentFilePath != null) {
			table.readFile(currentFilePath);
		}
	}
	
	public Table getTable() {
		return table;
	}
	
	
	@FXML
	public void reload() {
		String search = searchField.getText();
		//配置将要显示在表格中的数据
		ArrayList<TableColumn<Person,String>> cols = new ArrayList<TableColumn<Person,String>>();
		ArrayList<TextField> textFields = new ArrayList<TextField>();
		
		for (int i = 0; i < table.fields.size(); i++) {
			TableColumn<Person,String> col = new TableColumn<Person,String>(table.fields.get(i));
			final int index = i;
			col.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>(){

				@Override
				public ObservableValue<String> call(CellDataFeatures<Person, String> param) {
					SimpleStringProperty ret = new SimpleStringProperty();
					try {
						ret.setValue(param.getValue().getArrayList().get(index));
					}catch(Exception e) {
					}
					return ret;
				}
			});
			cols.add(col);
			
			TextField t = new TextField();			
			t.setPromptText(table.fields.get(i));
			textFields.add(t);
		}
		tableview.getColumns().setAll(cols);
		bar.getChildren().setAll(textFields);
		
		//读取table数据，显示在表格中
		ObservableList<Person> arr = FXCollections.observableArrayList();
   	 	for (int i = 0; i < table.staffs.size(); i++) {
   	 		if (search != "") { // check if search term exists
   	 			String contactFields = "";
   	 			boolean valid = true; // false if contact doesn't contain search term
   	 			
	   	 		for (String field : table.staffs.get(i).getArrayList()) {
	   	 			if (field != null) {
	   	 				contactFields += field.toLowerCase() + " ";
	   	 			}
	   	 		}
	   	 		
	   	 		String[] searchTerms = (search.toLowerCase()).split(" ");

	   	 		for (String term : searchTerms) {
	   	 		 // Contact must contain each of the search terms
	   	   	 		if(!contactFields.contains(term)){ 
	   	   	 			valid = false;
	   	   	 		    break;
	   	   	 		}
	   	 		}
	   	 		if (valid) {
	   	 		    arr.add(table.staffs.get(i));
	   	 		}
   	 		} else {
   	 		    arr.add(table.staffs.get(i));
   	 		}
   		}
   	 	tableview.setItems(arr);
   	 	// Resize columns to fit screen
   	    tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
   	    System.out.println("reload done");
	}
	
	
	@FXML //按下取消搜索按键，清空搜索栏，重新显示数据
	public void clearSearch() {
		searchField.setText("");
		reload();
	}
	
	
	
	//填加，删除，更改用户
	@FXML
    protected void addPerson(ActionEvent event) {
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
        newContact = new Person(fields.iterator());
        table.add(newContact); // Add to contacts
        reload();
    }
    
    // Called when Delete button clicked
    @FXML
    protected void deletePerson(ActionEvent event) {
    		Person victim = tableview.getSelectionModel().getSelectedItem();
    		if (victim != null) {
    			if (deleteConfirmation()) {
    				for (int i=0; i < table.staffs.size(); i++) {
    					if (table.staffs.get(i) == victim) {
    						table.delete(i);
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
    
    @FXML
    protected void editPerson(ActionEvent event) {
    		final Stage editStage = new Stage();
    		editStage.setTitle("Edit");
    		editStage.initModality(Modality.APPLICATION_MODAL);
    		
    		VBox box = new VBox();
    		box.setPadding(new Insets(10,50,10,50));
    		box.setSpacing(5);
    		
    		ArrayList<TextField> textFields = new ArrayList<TextField>();
    		Button saveButton = new Button("确认");
    		Button cancelButton = new Button("取消");
    		
    		Person selectedPerson = tableview.getSelectionModel().getSelectedItem();
    		if (selectedPerson != null) {
    			ArrayList<String> values = selectedPerson.getArrayList();
    			for (int i = 0; i < table.fields.size(); i++) {
    				TextField t = new TextField();
    				t.setPromptText(table.fields.get(i));
    				t.setText(values.get(i));
    				textFields.add(t);
    			}
    			box.getChildren().addAll(textFields);
    		}
    		
    		//确认按钮
    		saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					for (int i = 0; i < table.staffs.size(); i++) {
						if (table.staffs.get(i) == selectedPerson) {
							textFieldsCopy = new ArrayList<TextField>(textFields);
							if (!popUpAlert()) {
								Person newPerson;
								ArrayList<String> fields = new ArrayList<String>();
								for (int j = 0; j < textFields.size(); j++) {
									fields.add(textFields.get(j).getText());
								}
								newPerson = new Person(fields.iterator());
								table.replace(i, newPerson);
								reload();
								editStage.close();
							}
						}
						
					}
				}	
    		});
    		
    		//取消按钮
    		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					editStage.close();					
				}
    		});
    		
    		box.getChildren().addAll(saveButton, cancelButton);
    		Scene editScene = new Scene(box);
    		editStage.setScene(editScene);
    		editStage.show();
    }
      
    public boolean popUpAlert() {
    		return false;
    }
    
    private void popUpFunction(boolean bool) {
    		final Stage editStage = new Stage();
    		if (bool == true) {
    			editStage.setTitle("Add");
    		}else {
    			editStage.setTitle("Edit");
    		}
		//editStage.setTitle("Edit");
		editStage.initModality(Modality.APPLICATION_MODAL);
		
		VBox box = new VBox();
		box.setPadding(new Insets(10,50,10,50));
		box.setSpacing(5);
		
		ArrayList<TextField> textFields = new ArrayList<TextField>();
		Button saveButton = new Button("确认");
		Button cancelButton = new Button("取消");
		
		Person selectedPerson = tableview.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			ArrayList<String> values = selectedPerson.getArrayList();
			for (int i = 0; i < table.fields.size(); i++) {
				TextField t = new TextField();
				t.setPromptText(table.fields.get(i));
				t.setText(values.get(i));
				textFields.add(t);
			}
			box.getChildren().addAll(textFields);
		}
		
		//确认按钮
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < table.staffs.size(); i++) {
					if (table.staffs.get(i) == selectedPerson) {
						textFieldsCopy = new ArrayList<TextField>(textFields);
						if (!popUpAlert()) {
							Person newPerson;
							ArrayList<String> fields = new ArrayList<String>();
							for (int j = 0; j < textFields.size(); j++) {
								fields.add(textFields.get(j).getText());
							}
							newPerson = new Person(fields.iterator());
							table.replace(i, newPerson);
							reload();
							editStage.close();
						}
					}
					
				}
			}	
		});
		
		//取消按钮
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				editStage.close();					
			}
		});
		
		box.getChildren().addAll(saveButton, cancelButton);
		Scene editScene = new Scene(box);
		editStage.setScene(editScene);
		editStage.show();
    }
    
    //新建文档
    protected EventHandler<ActionEvent> newCallback(){
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("in new");
				try {
					table = new Table();
					reload();
					//Runtime.getRuntime().exec("java -jar addressBook.jar");
					System.out.println("open");
				}catch(Exception e) {
					System.out.println("无法新建");
				}
			}
		};
    }
    
    //打开文档
    protected EventHandler<ActionEvent> open(){
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Stage stage = new Stage();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("打开");
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					try {
						System.out.println(file.getAbsolutePath());
						table = new Table();
						table.readFile(file.getAbsolutePath());
						System.out.println("read file");
						reload();
						System.out.println("reloaded");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		};
    }
    
    //保存
    protected EventHandler<ActionEvent> save(){
    		return new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					try {
						table.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
    		};
    }
    //另存为    
    protected EventHandler<ActionEvent> saveAs() {
    		return new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					openFileChooser("另存为");
				}
    		};
    }
    
    private void openFileChooser(String task) {
    		final Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(task);
		if (task.equals("另存为")) {
			File file = fileChooser.showSaveDialog(stage);
			if(file != null) {
				try {
					table.saveAs(file.getAbsolutePath());
					currentFilePath = file.getAbsolutePath();
				}catch(Exception e) {
				}
			}
		}else {
			File file = fileChooser.showOpenDialog(stage);
			if(file != null) {
				try {
					table = new Table();
					table.readFile(file.getAbsolutePath());
					currentFilePath = file.getAbsolutePath();
					reload();
				}catch(Exception e) {			
				}
			}
		}
    }
    
    
}
	
