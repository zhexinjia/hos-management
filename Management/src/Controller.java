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
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
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
	
	@FXML private Button addButton;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button recordButton;
	
	private static String currentFilePath = null;
	
	private ArrayList<TextField> textFieldsCopy;
	
	Table table;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reload();	
		ArrayList<Button> buttons = new ArrayList<Button>();
		buttons.add(addButton);
		buttons.add(editButton);
		buttons.add(deleteButton);
		buttons.add(recordButton);
		setButtonShadow(buttons);
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
	
	protected boolean getChangeStatus() {
		return table.hasChanged;
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
			textFieldsCopy = new ArrayList<TextField>(textFields);
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
    				try {
    					t.setText(values.get(i));
    				}catch(Exception e) {
    					
    				}
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
    		HBox buttonBox = new HBox();
    		buttonBox.setSpacing(40);
    		buttonBox.getChildren().addAll(saveButton, cancelButton);
    		box.getChildren().add(buttonBox);
    		//box.getChildren().addAll(saveButton, cancelButton);
    		Scene editScene = new Scene(box);
    		editStage.setScene(editScene);
    		if (selectedPerson != null) {
    			editStage.show();
    		}
    		//editStage.show();
    }
      
    @FXML
    protected void lookupRecord(ActionEvent event) {
    		errorMessage("1","2","3");
    }
    
    public boolean popUpAlert() {
    		if (!entryCheck()) {
    			errorMessage("出错","名字不能为空","姓名不能为空");
    			return true;
    		}
    		if (!scoreValidation()) {
    			errorMessage("出错","得分必须为数字","得分必须为数字");
    			return true;
    		}
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
				}catch(Exception e) {
				}
			}
		};
    }
    
    //打开文档
    protected EventHandler<ActionEvent> open(){
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openFileChooser("打开");
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
    
    //打开一个文档选择器，用在saveAs()和open()中
    protected boolean openFileChooser(String task) {
    		final Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(task);
		if (task.equals("另存为")) {
			File file = fileChooser.showSaveDialog(stage);
			if(file != null) {
				try {
					table.saveAs(file.getAbsolutePath());
					currentFilePath = file.getAbsolutePath();
					return true;
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
					return true;
				}catch(Exception e) {	
				}
			}
		}
		return false;
    }
    
    //出错弹出窗口
    protected void errorMessage(String title, String text, String content) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle(title);
    		alert.setHeaderText(text);
    		alert.setContentText(content);
    		alert.showAndWait();
    }
    
    //检查得分和总分是否为数字
    protected boolean scoreValidation() {
    		if (!textFieldsCopy.get(2).getText().trim().isEmpty()) {
    			try {
    				Double.parseDouble(textFieldsCopy.get(2).getText());
    			}catch(Exception e) {
    				return false;
    			}
    		}
    		if (!textFieldsCopy.get(3).getText().trim().isEmpty()) {
    			try {
    				Double.parseDouble(textFieldsCopy.get(3).getText());
    			}catch(Exception e) {
    				return false;
    			}
    		}
    		return true;
    }
    
    protected boolean entryCheck() {
    		if (textFieldsCopy.get(1).getText().trim().isEmpty()) {
    			return false;
    		}
    		return true;
    }
    
    //Button effect
    public void setButtonShadow(ArrayList<Button> buttons) {
    		DropShadow shadow = new DropShadow();
    		for (int i = 0; i < buttons.size(); i++) {
    			final int index = i;
    			buttons.get(i).addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e)->{
    				buttons.get(index).setEffect(shadow);
    			});
    			buttons.get(i).addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e)->{
    				buttons.get(index).setEffect(null);
    			});
    		}
    	
    }
    
    
    
}
	
