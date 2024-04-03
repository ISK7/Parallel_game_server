package com.example.parallel_game_server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RunServer extends Thread {
    int port = 4321;
    InetAddress ip = null;
    GameRunner gr;
    DataInputStream dis;
    DataOutputStream dos;
    Gson gson = new Gson();
    int x = 70;

    List<PlayerListener> pls;

    @Override
    public void run()
    {
        try {
            gr = new GameRunner(this);
            ip = InetAddress.getLocalHost();
            ServerSocket socket = new ServerSocket(port);

            pls = new ArrayList<>();
            gson = new Gson();
            System.out.println("Successfully hosted!");
            gr.start();
            System.out.println("Successfully started!");
            while (true) {
                Socket clientSocket = socket.accept();
                System.out.println("Connecting...");
                dis = new DataInputStream(clientSocket.getInputStream());
                dos = new DataOutputStream(clientSocket.getOutputStream());
                String name = dis.readUTF();
                Player player = new Player(name,gr);
                Integer res = gr.AddPlayer(player);
                dos.writeUTF(res.toString());
                if(res < 0) {
                    System.out.println("Connecting failure: " + res);
                    clientSocket.close();
                    continue;
                }
                player.setNumber(res);
                dos.writeUTF(gson.toJson(new PlayerData(player)));
                System.out.println("Connection success: " + res);
                PlayerListener pl = new PlayerListener();
                pl.setVal(clientSocket,res,new GameData(gr),this);
                pl.start();
                pls.add(pl);
            }
        } catch (IOException e) {
            System.err.println("Run server" + e.getMessage());
        }

    }

    public void delPlayer(int num) {
        gr.dellPlayer(num);
    }
    public void shoot(int n) {
        gr.askShoot(n);
    }
    public void changeReady(int n) {
        gr.askChangeReady(n);
    }
    public void changePause(int n) {
        gr.askChangePause(n);
    }

    public void resetData(GameData gd) {
        for(PlayerListener i : pls) {
            i.newGameData(new GameData(gd));
            if(gd.isWin())
                delPlayer(i.getNumber());
        }
    }

    public static void main(String[] args) {
        new RunServer().start();
    }
}
