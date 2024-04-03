package com.example.parallel_game_server;

import com.example.parallel_game_server.GameRunner;
import com.example.parallel_game_server.Player;

import java.util.ArrayList;

public class GameData {
    ArrayList<PlayerData> pd = new ArrayList<>();
    boolean isPause = false;
    double bigy;
    double smally;
    double bigx;
    double smallx;
    boolean isWin;
    String winner;
    public GameData(GameData gd) {
        for(PlayerData p : gd.getPd()) pd.add(new PlayerData(p));
        bigy = gd.getBigy();
        smally = gd.getSmally();
        bigx = gd.getBigx();
        smallx = gd.getSmallx();
        isWin = gd.isWin;
        winner = gd.winner;
    }

    public GameData(GameRunner gr) {
        for(Player p : gr.getPlayers()) pd.add(new PlayerData(p));
        bigy = gr.getBigy();
        smally = gr.getSmally();
        bigx = gr.getBigx();
        smallx = gr.getSmallx();
        isWin = gr.hasWinner;
        winner = gr.winner;
    }

    public ArrayList<PlayerData> getPd() {
        return pd;
    }

    public boolean isPause() {
        return isPause;
    }

    public boolean isWin() {
        return isWin;
    }

    public String getWinner() {
        return winner;
    }

    public double getBigy() {
        return bigy;
    }

    public double getSmally() {
        return smally;
    }

    public double getBigx() {
        return bigx;
    }

    public double getSmallx() {
        return smallx;
    }
}
