package com.example.parallel_game_server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerListener extends Thread {
    private Socket socket;
    Gson gson = new Gson();
    private int number;
    private GameData gd;
    private RunServer parent;
    private boolean isWin = false;
    private boolean askLeaders = false;

    public void setVal(Socket socket, int n, GameData gd, RunServer runServer) {
        this.socket = socket;
        number = n;
        this.gd = gd;
        parent = runServer;
    }

    public void newGameData(GameData n) {
        gd = n;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            synchronized (this) {
                try {
                    while (true) {
                        if(askLeaders) {
                            gd.setAskLead(true);
                            ArrayList<LeaderData> ld = parent.askLeaders();
                            gd.setLeaders(ld);
                        }
                        dos.writeUTF(gson.toJson(gd));
                        dos.flush();
                        askLeaders = false;
                        gd.setAskLead(false);
                        if(gd.isWin())
                            break;
                        String ans = dis.readUTF();
                        PlayerData pd = gson.fromJson(ans, PlayerData.class);
                        if (pd.getAskShoot())
                            parent.shoot(number);
                        if (pd.isReady())
                            parent.changeReady(number);
                        if (pd.isAskPause())
                            parent.changePause(number);
                        if(pd.isAskLeaders())
                            askLeaders = true;
                        wait(20);
                    }
                } catch (EOFException ex) {
                    System.out.println("Client " + number + "disconnected!");
                    socket.close();
                } catch (InterruptedException ex) {
                    System.out.println("PlayerListener " + ex.getMessage());
                    socket.close();
                }
            }
        } catch (IOException ex) {
            System.out.println("PlayerListener " + ex.getMessage());
        }
    }
    public int getNumber() {
        return number;
    }
}
