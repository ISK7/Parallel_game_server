package com.example.parallel_game_server;

import com.example.parallel_game_server.GameData;
import com.example.parallel_game_server.PlayerData;
import com.google.gson.Gson;
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

    public Listener(ClientVisualizer cv, Socket soc, PlayerData pd) throws UnknownHostException {
        try {
            parent = cv;
            cs = soc;
            parent.start(new Stage());
            parent.setupApp(soc, pd);
        }catch (java.io.IOException ex) {
            System.out.println("Listener: " + ex);
        }
    }

    @Override
    public void run() {
        try {
            InputStream is = cs.getInputStream();
            OutputStream os = cs.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
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
