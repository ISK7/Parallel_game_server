package com.example.parallel_game_server;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class GameResult extends Application {
    @FXML
    Label name;
    @FXML
    Button ReconnectBut;
    String myName;
    Stage s;
    Gson gson = new Gson();

    int port = 4321;
    InetAddress ip = null;
    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    @Override
    public void start(Stage s) throws IOException {
        FXMLLoader gr = new FXMLLoader(ClientApplication.class.getResource("game-result.fxml"));
        Scene scene = new Scene(gr.load(),320,240);
        s.setScene(scene);
        s.setTitle("Result");
        s.show();
    }

    public void setRes(Socket sok, String winName, String plName) {
        try {
            FXMLLoader gr = new FXMLLoader(ClientApplication.class.getResource("game-result.fxml"));
            Scene scene = new Scene(gr.load(), 320, 240);
            GameResult gres = gr.getController();

            name = gres.name;
            gres.cs = sok;
            String wN = winName;
            name.setText(wN);
            gres.myName = plName;

            s = new Stage();
            s.setScene(scene);
            s.setTitle("Result");
            gres.s = s;
            s.show();
        } catch (IOException ex) {
            System.out.println("Game result: " + ex);
        }
    }

    public void onReconnectClick() {
        try {
            ip = InetAddress.getLocalHost();
            cs = new Socket(ip, port);
            is = cs.getInputStream();
            os = cs.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
            String obj_str = myName;
            dos.writeUTF(obj_str);
            String ans = dis.readUTF();
            Integer result = Integer.parseInt(ans);
            if (result == -1) {
                ReconnectBut.setText("Try another");
                return;
            }
            if (result == -2) {
                ReconnectBut.setText("Game already started");
                return;
            }
            if (result == -3) {
                ReconnectBut.setText("No free seats");
                return;
            }
            s.close();
            ans = dis.readUTF();
            Listener l = new Listener(cs, gson.fromJson(ans,PlayerData.class));
            l.start();
        } catch (IOException ex) {
            System.out.println("Game result: " + ex);
        }
    }
}
