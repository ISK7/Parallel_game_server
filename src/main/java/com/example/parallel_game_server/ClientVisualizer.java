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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientVisualizer extends Application {

    @FXML
    Canvas canvas;
    @FXML
    StackPane table;
    @FXML
    Circle bigTee, smallTee;
    @FXML
    Button pauseButton, readyButton;

    HashMap<Integer,PlayerData> players;
    PlayerData player;
    Color[] colors = {Color.RED, Color.BLUE,Color.GREEN,Color.BLACK};
    Listener listener;

    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setTable(StackPane table) {
        this.table = table;
    }

    public void setBigTee(Circle bigTee) {
        this.bigTee = bigTee;
    }

    public void setSmallTee(Circle smallTee) {
        this.smallTee = smallTee;
    }

    public void setPauseButton(Button pauseButton) {
        this.pauseButton = pauseButton;
    }

    public void setReadyButton(Button readyButton) {
        this.readyButton = readyButton;
    }
    public void setSocket(Socket s) {
        cs = s;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage s) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        listener = new Listener(this, cs);
//        listener.startGame();
//        ClientVisualizer cv = fxmlLoader.getController();
//
//        cv.setCanvas(canvas);
//        cv.setBigTee(bigTee);
//        cv.setSmallTee(smallTee);
//        cv.setPauseButton(pauseButton);
//        cv.setReadyButton(readyButton);
//        cv.setTable(table);
//
//
//        s.setScene(scene);
//        s.setWidth(600);
//        s.setHeight(450);
//        s.setTitle("Well marksman");
//        s.show();

    }

    public void startApp(Socket soc, PlayerData pd) {
        try {

            this.setSocket(soc);
            this.setPlayer(pd);

            String[] s = {};
            main(s);
        } catch (Exception ex) {
            System.out.println("Client visualizer: " + ex);
        }
    }

    public void onShootClick() {
        player.setAskShoot(true);
    }
    public void onPauseClick() {
        player.setAskPause(true);
        if(pauseButton.getText().equals("Pause")) pauseButton.setText("Go");
        else pauseButton.setText("Pause");
    }
    public void onReadyClick() {
        player.setReady(true);
        readyButton.setDisable(true);
    }

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

    public PlayerData getPlayer() {
        PlayerData ans = player;
        player.setReady(false);
        player.setAskPause(false);
        player.setAskShoot(false);
        return ans;
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
}
