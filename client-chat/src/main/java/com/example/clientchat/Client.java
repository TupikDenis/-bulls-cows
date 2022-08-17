package com.example.clientchat;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket serverSocket){
        try{
            this.socket = serverSocket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating client!");
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void sendMessageToServer(String messageToServer){
        try{
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to client!");
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void receiveMessageFromServer(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        String messageFromServer = bufferedReader.readLine();
                        if(messageFromServer.length() == 4){
                            ClientController.addLabel("Число загадано! Попробуйте отгадать", vBox);
                        } else {
                            ClientController.addLabel(messageFromServer, vBox);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message to client!");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }

            if(bufferedWriter != null){
                bufferedWriter.close();
            }

            if(socket != null){
                socket.close();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
