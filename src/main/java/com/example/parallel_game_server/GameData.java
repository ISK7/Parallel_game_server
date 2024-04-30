package com.example.parallel_game_server;

import com.example.parallel_game_server.GameRunner;
import com.example.parallel_game_server.Player;

import java.util.ArrayList;

public class GameData {
    ArrayList<PlayerData> pd = new ArrayList<>();
    ArrayList<LeaderData> leaders = new ArrayList<>();
    boolean isPause = false;
    double bigy;
    double smally;
    double bigx;
    double smallx;
    boolean isWin;
    boolean isAskLead;
    String winner;
    public GameData(GameData gd) {
        for(PlayerData p : gd.getPd()) pd.add(new PlayerData(p));
        for(LeaderData l : gd.getLeaders()) leaders.add(new LeaderData(l));
        bigy = gd.getBigy();
        smally = gd.getSmally();
        bigx = gd.getBigx();
        smallx = gd.getSmallx();
        isWin = gd.isWin;
        winner = gd.winner;
        isAskLead = gd.isAskLead;
    }

    public GameData(GameRunner gr) {
        for(Player p : gr.getPlayers()) pd.add(new PlayerData(p));
        bigy = gr.getBigy();
        smally = gr.getSmally();
        bigx = gr.getBigx();
        smallx = gr.getSmallx();
        isWin = gr.hasWinner;
        winner = gr.winner;
        leaders = new ArrayList<>();
        isAskLead = false;
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

    public boolean isAskLead() {
        return isAskLead;
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

    public void setLeaders(ArrayList<LeaderData> leaders) {
        this.leaders = leaders;
    }
    public void setAskLead(boolean b) {this.isAskLead = b;}

    public ArrayList<LeaderData> getLeaders() {
        return leaders;
    }
}
