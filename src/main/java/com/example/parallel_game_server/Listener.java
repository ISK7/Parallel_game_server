package com.example.parallel_game_server;

import com.example.parallel_game_server.GameData;
import com.example.parallel_game_server.PlayerData;
import com.google.gson.Gson;
import javafx.scene.control.Dialog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

public class Listener extends Thread {
    private GameData gd = null;
    Socket cs;
    private Gson gson = new Gson();
    Client parent;

    public Listener(Client cv, Socket soc) throws UnknownHostException {
            parent = cv;
            cs = soc;
    }

    public void startGame() {
        start();
    }
    @Override
    public void run() {
        try {
            boolean nameChoosed = false;
            InputStream is = cs.getInputStream();
            OutputStream os = cs.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
            PlayerData player = new PlayerData();
            while(!nameChoosed) {
                Dialog d = new AskNameDialog();
                Optional<String> sRes = d.showAndWait();
                if(sRes.isEmpty()) continue;
                dos.writeUTF(sRes.get());
                String ans = dis.readUTF();
                Integer result = Integer.parseInt(ans);
                if (result == -1) {
                    text.setText("");
                    text.setPromptText("Try another");
                    continue;
                }
                if (result == -2) {
                    text.setText("");
                    text.setPromptText("Game already started");
                    continue;
                }
                if (result == -3) {
                    text.setText("");
                    text.setPromptText("No free seats");
                    continue;
                }
                ans = dis.readUTF();
                player = gson.fromJson(ans,PlayerData.class);
                nameChoosed = true;
            }
            parent.setPlayer(player);
            while (true) {
                String s = dis.readUTF();
                gd = gson.fromJson(s,GameData.class);
                parent.clearView();
                for(PlayerData pd : gd.getPd()) {
                    parent.drawPlayer(pd);
                }
                parent.drawMarks((int)gd.getBigx(),(int)gd.getBigy(),(int)gd.getSmallx(),(int)gd.getSmally());
                String ans = gson.toJson(parent.getPlayer());
                dos.writeUTF(ans);
            }
        } catch (Exception ex) {

            System.out.println("Listener: " + ex);
        }
    }
}
