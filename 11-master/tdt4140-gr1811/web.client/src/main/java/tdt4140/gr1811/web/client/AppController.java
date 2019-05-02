package tdt4140.gr1811.web.client;

import java.io.IOException;
import java.net.InetAddress;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AppController {
	
	@FXML VBox root, ECHO_ME_box, INSERT_DATA_box, DELETE_USER_box, MODIFY_USER_box, server_response;
	@FXML TextField clientid_field, data_value, data_time, modify_weight, modify_height, modify_email, modify_phone;
	@FXML MenuItem connectItem, disconnectItem, closeItem;	
	@FXML Label server_label, port_label;
	@FXML TextArea server_message, echo_field;
	@FXML ComboBox<String> msgtype_combobox;
	@FXML ComboBox<Boolean> deleteData_combobox;
	@FXML Button insert_data_button, clear_data_button;
	@FXML ScrollPane response_scrollpane;
	
	private TCPClient tcp_client = null;	
	private InetAddress hostname;
	private Integer port;	
	
	private String msg_type = "";
	private Integer client_id;
	private String echo_this;
	private HashMap<String, Number> data_map = new HashMap<>();	
	private boolean delete_data;
	
	public void initialize()  {
		
		root.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
			KeyCode code = event.getCode();
			if(code == KeyCode.ENTER && event.isControlDown()) {
				String message = server_message.getText();
				sendMessage(message);
			}
		});	
		
		connectItem.setOnAction(e -> {
			connectToServer();
		});
		
		disconnectItem.setOnAction(e -> {
			disconnectFromServer();
		});
		
		closeItem.setOnAction(e -> {
			Platform.exit();
		});
		
		msgtype_combobox.setDisable(true);
		clientid_field.setDisable(true);
		server_message.setDisable(true);
		response_scrollpane.setDisable(true);
		
		initializeDynamicJson();
			
		try {
			tcp_client = new TCPClient("localhost", 64672);
			updateServerInfo();
			
		} catch (IOException e) {
			System.out.println("Cannot connect to localhost server. Connect to server manually");
		}
	}
	
	private void buildJsonString() {
		String output = "";
		if(msg_type.equals("ECHO_ME")) {
			output = "{\"msgType\":\""+msg_type+"\",\"clientId\":" + client_id + ",\"echoThis\":\"" + echo_this +"\"}";
		}
		else if(msg_type.equals("REQUEST_ALL_INFO") || msg_type.equals("DELETE")) {
			output = "{\"msgType\":\"" + msg_type + "\",\"clientId\":" + client_id + "}";
		}
		else if(msg_type.equals("DELETE_DATA")) {
			output = "{\"msgType\":\""+msg_type+"\",\"clientId\":" + client_id + "}";
		}
		else if(msg_type.equals("INSERT_BLOODSUGAR_DATA") || msg_type.equals("INSERT_PULSE_DATA")) {
			String dataString = "data:{";
			if(data_map != null) {
				for(Map.Entry<String, Number> data : data_map.entrySet()) {
					dataString += "'" + data.getKey() + "':" + data.getValue() +",";
				}
				dataString = dataString.substring(0, dataString.length() -1);
			}	
			dataString += "}";
			output = "{\"msgType\":\"" + msg_type + "\",\"clientId\":" + client_id + "," + dataString + "}";
		}
		else if(msg_type.equals("CREATE_USER")) {
			output = "{\"msgType\":\""+msg_type+"\",\"ssn\":\"\", \"firstName\":\"\",\"lastName\":\"\", \"birthDate\":\"\", \"sex\":'', \"weight\":\"\", \"height\":\"\", \"email\":\"\",\"phoneNum\":\"\"}";
		}
		else if(msg_type.equals("DELETE_USER")) {
			output = "{\"msgType\":\""+msg_type+"\",\"clientId\":" + client_id + ", \"deleteData\":"+delete_data+"}";
		}
		else if(msg_type.equals("SET_DELETE_STATE")) {
			output = "{\"msgType\":\""+msg_type+"\",\"clientId\":" + client_id + ", \"status\":"+delete_data+"}";
		}
		else if(msg_type.equals("MODIFY_USER")) {
			String weight = "";
			String height = "";
			String phone = "";
			String email = "";
			if(!modify_weight.getText().equals("")) {
				weight = ", weight:\"" + modify_weight.getText() + "\"";
			}
			if(!modify_height.getText().equals("")) {
				height = ", height\"" + modify_height.getText() + "\"";
			}
			if(!modify_phone.getText().equals("")) {
				phone = ", phone:\"" + modify_phone.getText() + "\"";
			}
			if(!modify_email.getText().equals("")) {
				email = ", email:\"" + modify_email.getText() + "\"";
			}
			output = "{\"msgType\":\""+msg_type+"\",\"clientId\":" + client_id + weight + height + phone + email + "}";
		}
		else if(msg_type.equals("STATUS_CHANGE")) {
			output = "{\"msgType\":\""+msg_type+"\",\"clientId\":" + client_id + ", \"status\":\"\"}";
		}
		
		server_message.setText(output);
	}

	public void connectToServer() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerConnect.fxml"));
			Parent root = loader.load();
			Scene connectScene = new Scene(root);
			Stage connectStage = new Stage();
			connectStage.setScene(connectScene);
			connectStage.setTitle("Koble til server...");
			connectStage.initModality(Modality.APPLICATION_MODAL);
			connectStage.showAndWait();	
			
			Object object = loader.getController();
			
			if(object instanceof ServerConnectController) {
				ServerConnectController controller = (ServerConnectController) object;
				tcp_client = controller.getTCPClient();
				updateServerInfo();
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void sendMessage(String input) {	
		
		if(tcp_client != null && !input.equals("")) {
			
			String output = tcp_client.sendMessage(input);
						
			// Add response to response_box
			VBox response_box = new VBox();
			HBox input_box = new HBox();
			HBox output_box = new HBox();
			Label message_label = new Label("Message:");
			Label response_label = new Label("Response:");
			message_label.setStyle("-fx-font-weight: bold;");
			message_label.setPrefWidth(65);
			response_label.setStyle("-fx-font-weight: bold;");
			response_label.setPrefWidth(65);
			
			Separator sep = new Separator(Orientation.HORIZONTAL);
			Label input_label = new Label(input);
			Label output_label = new Label(output);
			input_label.setWrapText(true);
			output_label.setWrapText(true);
			input_label.setPrefWidth(410);
			output_label.setPrefWidth(410);
			
			input_box.getChildren().addAll(message_label, input_label);
			output_box.getChildren().addAll(response_label, output_label);
			response_box.getChildren().addAll(input_box, output_box);	
			server_response.getChildren().add(0, sep);
			server_response.getChildren().add(0, response_box);
		}	
	}
	
	public void disconnectFromServer() {
		tcp_client = null;
		updateServerInfo();
	}
	
	private void updateServerInfo() {
		if(tcp_client != null) {
			hostname = tcp_client.getHostname();
			port = tcp_client.getPort();
			server_label.setText(hostname.toString());
			port_label.setText(Integer.toString(port));
			msgtype_combobox.setDisable(false);
			clientid_field.setDisable(false);
			server_message.setDisable(false);
			response_scrollpane.setDisable(false);
		} else {
	    	hostname = null;
    		port = null;
    		server_label.setText("N / A");
    		port_label.setText("N / A");
    		msgtype_combobox.setDisable(true);
    		clientid_field.setDisable(true);
    		server_message.setDisable(true);
    		response_scrollpane.setDisable(true);
		}
	}
	
	public void closeWindow() {
		Platform.exit();
	}

	private void initializeDynamicJson() {
		ECHO_ME_box.setVisible(false);
		ECHO_ME_box.setManaged(false);
		INSERT_DATA_box.setVisible(false);
		INSERT_DATA_box.setManaged(false);
		DELETE_USER_box.setVisible(false);
		DELETE_USER_box.setManaged(false);
		MODIFY_USER_box.setVisible(false);
		MODIFY_USER_box.setManaged(false);
		
		msgtype_combobox.getItems().addAll("ECHO_ME", "REQUEST_ALL_INFO", "DELETE_DATA", "INSERT_BLOODSUGAR_DATA", "INSERT_PULSE_DATA", "CREATE_USER" ,"DELETE_USER",
				"SET_DELETE_STATE", "MODIFY_USER", "STATUS_CHANGE");
		deleteData_combobox.getItems().addAll(true, false);
		
		msgtype_combobox.valueProperty().addListener((obs, oldv, newv) -> {
			if(newv.equals("ECHO_ME")) {
				clientid_field.setVisible(true);
				ECHO_ME_box.setVisible(true);
				ECHO_ME_box.setManaged(true);
				INSERT_DATA_box.setVisible(false);
				INSERT_DATA_box.setManaged(false);
				DELETE_USER_box.setVisible(false);
				DELETE_USER_box.setManaged(false);
				MODIFY_USER_box.setVisible(false);
				MODIFY_USER_box.setManaged(false);
			}
			else if(newv.equals("REQUEST_ALL_INFO") || newv.equals("DELETE")) {
				clientid_field.setVisible(true);
				ECHO_ME_box.setVisible(false);
				ECHO_ME_box.setManaged(false);
				INSERT_DATA_box.setVisible(false);
				INSERT_DATA_box.setManaged(false);
				DELETE_USER_box.setVisible(false);
				DELETE_USER_box.setManaged(false);
				MODIFY_USER_box.setVisible(false);
				MODIFY_USER_box.setManaged(false);
			}
			else if(newv.equals("INSERT_BLOODSUGAR_DATA") || newv.equals("INSERT_PULSE_DATA")) {
				clientid_field.setVisible(true);
				ECHO_ME_box.setVisible(false);
				ECHO_ME_box.setManaged(false);
				INSERT_DATA_box.setVisible(true);
				INSERT_DATA_box.setManaged(true);
				DELETE_USER_box.setVisible(false);
				DELETE_USER_box.setManaged(false);
				MODIFY_USER_box.setVisible(false);
				MODIFY_USER_box.setManaged(false);
			}
			else if(newv.equals("CREATE_USER")) {
				clientid_field.setVisible(false);
				ECHO_ME_box.setVisible(false);
				ECHO_ME_box.setManaged(false);
				INSERT_DATA_box.setVisible(false);
				INSERT_DATA_box.setManaged(false);
				DELETE_USER_box.setVisible(false);
				DELETE_USER_box.setManaged(false);
				MODIFY_USER_box.setVisible(false);
				MODIFY_USER_box.setManaged(false);
			}
			else if(newv.equals("DELETE_USER") || newv.equals("SET_DELETE_STATE")) {
				clientid_field.setVisible(true);
				ECHO_ME_box.setVisible(false);
				ECHO_ME_box.setManaged(false);
				INSERT_DATA_box.setVisible(false);
				INSERT_DATA_box.setManaged(false);
				DELETE_USER_box.setVisible(true);
				DELETE_USER_box.setManaged(true);
				MODIFY_USER_box.setVisible(false);
				MODIFY_USER_box.setManaged(false);
			}
			else if(newv.equals("MODIFY_USER")) {
				clientid_field.setVisible(true);
				ECHO_ME_box.setVisible(false);
				ECHO_ME_box.setManaged(false);
				INSERT_DATA_box.setVisible(false);
				INSERT_DATA_box.setManaged(false);
				DELETE_USER_box.setVisible(false);
				DELETE_USER_box.setManaged(false);
				MODIFY_USER_box.setVisible(true);
				MODIFY_USER_box.setManaged(true);
			}
			else if(newv.equals("STATUS_CHANGE")) {
				clientid_field.setVisible(true);
				ECHO_ME_box.setVisible(false);
				ECHO_ME_box.setManaged(false);
				INSERT_DATA_box.setVisible(false);
				INSERT_DATA_box.setManaged(false);
				DELETE_USER_box.setVisible(false);
				DELETE_USER_box.setManaged(false);
				MODIFY_USER_box.setVisible(false);
				MODIFY_USER_box.setManaged(false);
			}
			msg_type = newv;
			buildJsonString();
		});
		
		clientid_field.textProperty().addListener((obs, oldv, newv) -> {
			if (!newv.matches("\\d*")) {
				newv = newv.replaceAll("[^\\d]", "");
			}
			if (newv.length() > 8) {
				newv = newv.substring(0, 8);
			}

			if(!newv.equals("")) {
				client_id = Integer.parseInt(newv);
				buildJsonString();
			}	
			else {
				client_id = null;
				buildJsonString();
			}
			clientid_field.setText(newv);
		}) ;
		
		echo_field.textProperty().addListener((obs, oldv, newv) -> {
			echo_this = echo_field.getText();
			buildJsonString();
		});
		
		insert_data_button.setOnAction(e -> {
			try {
				data_map.put(data_time.getText(), NumberFormat.getInstance().parse(data_value.getText()));
				buildJsonString();
			} catch (Exception e1) {
				// DO absolutely nothing
			}
		});	
		
		clear_data_button.setOnAction(e -> {
			data_map.clear();
			buildJsonString();
		});
		
		deleteData_combobox.valueProperty().addListener((obs, oldv, newv) -> {
			delete_data = deleteData_combobox.getValue();
			buildJsonString();
		});
		
		modify_weight.textProperty().addListener((obs, oldv, newv) -> {
			buildJsonString();
		});
		modify_height.textProperty().addListener((obs, oldv, newv) -> {
			buildJsonString();
		});
		modify_phone.textProperty().addListener((obs, oldv, newv) -> {
			buildJsonString();
		});
		modify_email.textProperty().addListener((obs, oldv, newv) -> {
			buildJsonString();
		});
		
	}
}
