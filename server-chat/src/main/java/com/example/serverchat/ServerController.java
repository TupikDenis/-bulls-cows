package com.example.serverchat;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private Button button_send;

    @FXML
    private TextField tf_message;

    @FXML
    private VBox vbox_messages;

    @FXML
    private ScrollPane sp_main;

    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            server = new Server(new ServerSocket(8080));
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating server");
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNumber, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        addLabel("Введите число: ", vbox_messages);

        server.receiveMessageFromClient(vbox_messages);

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = tf_message.getText();
                if(messageToSend.length() == 4 && isDigitDifferent(messageToSend) && isNumber(messageToSend)){
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5, 5, 5, 10));

                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color: rgb(239, 242, 255);" +
                            "-fx-background-color: rgb(15, 125, 242);" +
                            "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    hbox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hbox);

                    server.sendMessageToClient(messageToSend);
                    server.setNumber(messageToSend);

                    tf_message.clear();
                }
            }
        });
    }

    public static void addLabel(String messageFromClient, VBox vbox){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: rgb(233, 233, 235);" +
                "-fx-background-radius: 20px");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hbox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hbox);
            }
        });
    }

    private boolean isDigitDifferent(String msg){
        for(int i=0; i < msg.length(); i++){
            for(int k=i+1; k <msg.length(); k++){
                if(msg.charAt(i) == msg.charAt(k)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isNumber(String number){
        for(int i=0; i < number.length(); i++){
            if(!Character.isDigit(number.charAt(i))){
                return false;
            }
        }
        return true;
    }
}