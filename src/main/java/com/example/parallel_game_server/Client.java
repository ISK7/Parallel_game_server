package com.example.parallel_game_server;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client extends Application {
    Gson gson = new Gson();

    static int port = 4321;
    InetAddress ip = null;
    static Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    @FXML
    TextField text;
    @FXML
    Canvas canvas;
    @FXML
    StackPane table;
    @FXML
    Circle bigTee, smallTee;
    @FXML
    Button pauseButton, readyButton;

    HashMap<Integer,PlayerData> players;
    static PlayerData player;
    Color[] colors = {Color.RED, Color.BLUE,Color.GREEN,Color.BLACK};
    Listener listener;

    static Stage s1, s2, s3;

    public void start(Stage stage) throws IOException {
        try {
            //game page
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("hello-view.fxml"));
            Scene scene2 = new Scene(fxmlLoader.load(), 320, 240);
            s2 = stage;
            s2.setScene(scene2);
            s2.setWidth(600);
            s2.setHeight(450);
            s2.setTitle("Well marksman");

            //ask name page
            FXMLLoader nameAsk = new FXMLLoader(Client.class.getResource("ask-name.fxml"));
            Scene scene1 = new Scene(nameAsk.load(),320,240);
            s1 = new Stage();
            s1.setScene(scene1);
            s1.setWidth(160);
            s1.setHeight(140);
            s1.setTitle("Registration");

            //end page


            s2.show();
            s1.show();
        } catch (Exception ex) {System.out.println("Client start: " + ex);}
    }

    public static void main(String[] args) {
        launch();
    }

    //Accept name button
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
            ans = dis.readUTF();
            player = gson.fromJson(ans,PlayerData.class);

            s1.close();

        } catch (IOException ex) {
            text.setText("");
            System.out.println("Client on accept click: " + ex);
        }
    }

    //game controls-------------------------------
    public void onShootClick() {
        player.setAskShoot(true);
    }
    public void onPauseClick() {
        player.setAskPause(true);
        if(pauseButton.getText().equals("Pause")) pauseButton.setText("Go");
        else pauseButton.setText("Pause");
    }
    public void onReadyClick() {
//        player.setReady(true);
//        try {
//            listener = new Listener(this, cs);
//            listener.startGame();
//        }
//        catch (UnknownHostException ex) {
//            System.out.println("ReadyClick: " + ex);
//        }
//        readyButton.setDisable(true);
    }
    //-----------------------------------------------

    //draw functions--------------------------------
    public void clearView() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        table.getChildren().clear();
    }
    public void drawPlayer(PlayerData p) {
        int number = p.getNumber();
        if(!players.containsKey(number)) {
            players.put(number,p);
        }
        if(!p.isReady()) drawGun(p.getY(),Color.GRAY);
        else drawGun(p.getY(),colors[p.getNumber()]);
        drawShoot(p.getX(),p.getY());
        showScore(p.getY(),p.getName(),p.getScoreCount(),p.getScoreCount());
    }

    public void drawMarks(int x1, int y1, int x2, int y2) {
        bigTee.relocate(x1,y1);
        smallTee.relocate(x2,y2);
    }
    private void drawGun(int y, Color c) {
        double[] xs = {25,25,50};
        double[] ys = {y-25,y+25,y};
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(c);
        gc.setFill(c);
        gc.fillPolygon(xs,ys,3);
        gc.strokePolygon(xs, ys, 3);
    }
    private void drawShoot(int x, int y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int r = 2;
        gc.setFill(Color.BLACK);
        gc.fillOval(x-r,y-r,2*r,2*r);
    }
    //draw score text
    private void showScore(int y, String name, Integer sc, Integer sh) {
        int x = 450;
        Label namel = new Label(name);
        namel.setLayoutX(x);
        namel.setLayoutY(y-10);
        Label score = new Label(sc.toString());
        Label shoots = new Label(sh.toString());
        score.setLayoutX(x);
        score.setLayoutY(y);
        shoots.setLayoutX(x);
        shoots.setLayoutY(y+10);
        table.getChildren().add(namel);
        table.getChildren().add(shoots);
        table.getChildren().add(score);
    }
    //---------------------------------------------------

    public PlayerData getPlayer() {
        PlayerData ans = player;
        player.setReady(false);
        player.setAskPause(false);
        player.setAskShoot(false);
        return ans;
    }
}
