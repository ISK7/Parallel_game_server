package com.example.parallel_game_server;

import com.example.parallel_game_server.PlayerData;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientApplication extends Application {
    Gson gson = new Gson();

    int port = 4321;
    InetAddress ip = null;
    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;
    ClientVisualizer cv;

    @FXML
    TextField text;


    void setSt(Stage s){
        this.s=s;

    }
    Stage s;
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader nameAsk = new FXMLLoader(ClientApplication.class.getResource("ask-name.fxml"));
            Scene scene = new Scene(nameAsk.load(),320,240);
            stage.setScene(scene);
            s = stage;
            s.setTitle("Registration");

            ClientApplication ca = nameAsk.getController();
            ca.setSt(s);

            s.show();

        } catch (Exception ex) {System.out.println(ex);}
    }

    public static void main(String[] args) {
        launch();
    }

    public void onAseptClick() {

        try {
            ip = InetAddress.getLocalHost();
            cs = new Socket(ip, port);
            is = cs.getInputStream();
            os = cs.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
            String obj_str = text.getText();
            dos.writeUTF(obj_str);
            String ans = dis.readUTF();
            Integer result = Integer.parseInt(ans);
            if (result == -1) {
                text.setText("");
                text.setPromptText("Try another");
                return;
            }
            if (result == -2) {
                text.setText("");
                text.setPromptText("Game already started");
                return;
            }
            if (result == -3) {
                text.setText("");
                text.setPromptText("No free seats");
                return;
            }
            if (result == -4) {
                text.setText("");
                text.setPromptText("Incorrect password or DB error");
                return;
            }
            s.close();
            ans = dis.readUTF();
            Listener l = new Listener(cs, gson.fromJson(ans,PlayerData.class));
            l.start();
        } catch (IOException ex) {
            text.setText("");
            System.out.println("Client Application: " + ex);
        }
    }
}