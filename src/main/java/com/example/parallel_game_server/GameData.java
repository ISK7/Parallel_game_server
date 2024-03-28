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

    public GameData(GameRunner gr) {
        for(Player p : gr.getPlayers()) pd.add(new PlayerData(p));
        bigy = gr.getBigy();
        smally = gr.getSmally();
        bigx = gr.getBigx();
        smallx = gr.getSmallx();
    }

    public ArrayList<PlayerData> getPd() {
        return pd;
    }

    public boolean isPause() {
        return isPause;
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
