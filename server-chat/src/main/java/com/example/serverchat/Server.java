package com.example.serverchat;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String number;
    private int userCount = 10;

    public Server(ServerSocket serverSocket){
        try{
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating server!");
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void sendMessageToClient(String messageToClient){
        try{
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to client!");
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void receiveMessageFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        if(userCount != 0) {
                            String messageFromClient = bufferedReader.readLine();
                            int cow = 0;
                            int bull = 0;

                            for (int i = 0; i < messageFromClient.length(); i++) {
                                if (messageFromClient.charAt(i) == number.charAt(i)) {
                                    bull++;
                                }
                            }

                            for (int i = 0; i < messageFromClient.length(); i++) {
                                for (int k = 0; k < number.length(); k++) {
                                    if (i != k) {
                                        if (messageFromClient.charAt(i) == number.charAt(k)) {
                                            cow++;
                                            break;
                                        }
                                    }
                                }
                            }

                            userCount--;
                            String answer = "Коров - " + cow + "\nБыков - " + bull + "\nОсталось попыток- " + userCount;
                            sendMessageToClient(answer);

                            int numberUser = 10 - userCount;
                            ServerController.addLabel("Число пользователя №" + numberUser + " - " + messageFromClient, vBox);
                            if (bull == 4) {
                                sendMessageToClient("Игра окончена! Вы победили!");
                                sendMessageToClient("Ожидайте, пока пользователь загадает число...");
                                number = "";
                                ServerController.addLabel("Пользователь победил", vBox);
                                ServerController.addLabel("Введите число", vBox);
                            }
                        } else {
                            userCount = 10;
                            sendMessageToClient("Игра окончена! Вы проиграли!");
                            sendMessageToClient("Правильный ответ - " + number);
                            sendMessageToClient("Ожидайте, пока пользователь загадает число...");
                            number = "";
                            ServerController.addLabel("Пользователь проиграл", vBox);
                            ServerController.addLabel("Введите число", vBox);
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

    public void setNumber(String number) {
        this.number = number;
    }
}
