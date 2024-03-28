package com.example.parallel_game_server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class PlayerListener extends Thread {
    private Socket socket;
    Gson gson = new Gson();
    private int number;
    private GameData gd;
    private RunServer parent;

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
            try {
                while (true) {
                    dos.writeUTF(gson.toJson(gd));
                    dos.flush();
                    String ans = dis.readUTF();
                    PlayerData pd = gson.fromJson(ans, PlayerData.class);
                    if (pd.getAskShoot()) parent.shoot(number);
                    if (pd.isReady()) parent.changeReady(number);
                    if (pd.isAskPause()) parent.changePause(number);
                }

            } catch (EOFException ex) {
                System.out.println("Client " + number + "disconnected!");
                socket.close();
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
