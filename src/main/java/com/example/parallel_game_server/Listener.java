package com.example.parallel_game_server;

import com.example.parallel_game_server.GameData;
import com.example.parallel_game_server.PlayerData;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Listener extends Thread {
    private GameData gd = null;
    Socket cs;
    private Gson gson = new Gson();
    ClientVisualizer parent;
    boolean isActive;

    public Listener(Socket soc, PlayerData pd) throws UnknownHostException {
        try {
            parent = new ClientVisualizer();
            cs = soc;
            parent = parent.setupApp(pd);
            isActive = true;
        }catch (java.io.IOException ex) {
            System.out.println("Listener setup: " + ex);
        }
    }

    @Override
    public void run() {
        try {
            InputStream is = cs.getInputStream();
            OutputStream os = cs.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
            synchronized (this) {
                while (true) {
                    String s = dis.readUTF();
                    Platform.runLater(() -> {
                        gd = gson.fromJson(s, GameData.class);
                        parent.clearView();
                        for (PlayerData pd : gd.getPd()) {
                            parent.drawPlayer(pd);
                        }
                        parent.drawMarks((int) gd.getBigx(), (int) gd.getBigy(), (int) gd.getSmallx(), (int) gd.getSmally());
                        if(gd.isWin()) {
                            GameResult gr = new GameResult();
                            gr.setRes(cs, gd.getWinner(), parent.getPlayer().getName());
                            isActive = false;
                            parent.close();
                        }
                    });
                    String ans = gson.toJson(parent.getPlayer());
                    wait(20);
                    dos.writeUTF(ans);
                }
            }
        } catch (Exception ex) {
            System.out.println("Listener run: " + ex);
        }
    }
}
