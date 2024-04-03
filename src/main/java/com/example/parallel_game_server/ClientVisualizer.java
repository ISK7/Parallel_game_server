package com.example.parallel_game_server;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    Button pauseButton, readyButton, shootButton;
    PlayerData player;
    Stage st;
    Color[] colors = {Color.RED, Color.BLUE,Color.GREEN,Color.BLACK};

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

    public void setPlayer(PlayerData player) {
        this.player = player;
    }
    @Override
    public void start(Stage s) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        ClientVisualizer cv = fxmlLoader.getController();

        cv.setCanvas(canvas);
        cv.setBigTee(bigTee);
        cv.setSmallTee(smallTee);
        cv.setPauseButton(pauseButton);
        cv.setReadyButton(readyButton);
        cv.setTable(table);


        s.setScene(scene);
        s.setWidth(600);
        s.setHeight(450);
        s.setTitle("Well marksman");
        s.show();

    }

    public ClientVisualizer setupApp(PlayerData pd) throws IOException {
        try {
            FXMLLoader game = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(game.load(), 320, 240);
            Stage s = new Stage();
            ClientVisualizer cv = game.getController();

            cv.setPlayer(pd);

            s.setScene(scene);
            s.setWidth(600);
            s.setHeight(450);
            s.setTitle("Well marksman");
            cv.st = s;
            s.show();
            return cv;
        } catch (Exception ex) {
            System.out.println("Client visualizer: " + ex);
        }
        return new ClientVisualizer();
    }

    public void close() {
        st.close();
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
        if(!p.isReady()) drawGun(p.getY(),Color.GRAY);
        else drawGun(p.getY(),colors[p.getNumber()]);
        drawShoot(p.getX(),p.getY());
        showScore(p.getY(),p.getName(),p.getScoreCount(),p.getShootCount());
    }

    public void drawMarks(int x1, int y1, int x2, int y2) {
        bigTee.relocate(x1,y1);
        smallTee.relocate(x2,y2);
    }

    public PlayerData getPlayer() {
        PlayerData ans = new PlayerData(player);
        player.setReady(false);
        player.setAskPause(false);
        player.setAskShoot(false);
        return ans;
    }


    private void drawGun(int y, Color c) {
        double[] xs = {45,45,70};
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
        Label namel = new Label(name);
        Label score = new Label("Score: " + sc.toString());
        Label shoots = new Label("Shoots: " + sh.toString());
        table.getChildren().add(namel);
        table.getChildren().add(shoots);
        table.getChildren().add(score);
        table.setAlignment(namel, Pos.TOP_CENTER);
        table.setAlignment(score, Pos.TOP_CENTER);
        table.setAlignment(shoots, Pos.TOP_CENTER);
        table.setMargin(namel, new Insets(y - 10,0,0,0));
        table.setMargin(score, new Insets(y,0,0,0));
        table.setMargin(shoots, new Insets(y + 10,0,0,0));
    }
}
