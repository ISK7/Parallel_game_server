package com.example.parallel_game_server;

import com.example.parallel_game_server.Player;

public class PlayerData {
    int y;
    int x;
    String name;
    Integer scoreCount;
    Integer shootCount;
    boolean askPause;
    boolean ready;
    boolean shootFree;
    boolean askShoot;
    int number;
    public PlayerData(Player p) {
        x = p.getX();
        y = p.getY();
        scoreCount = p.getScoreCount();
        shootCount = p.getShootCount();
        askPause = p.isAskPause();
        ready = p.isReady();
        shootFree = p.isShootFree();
        number = p.getNumber();
        name = p.getPlayerName();
        askShoot = p.isAskShoot();
    }

    public PlayerData() {
        x = 0;
        y = 0;
        scoreCount = 0;
        shootCount = 0;
        askPause = false;
        ready = false;
        shootFree = false;
        number = 0;
        name = "";
        askShoot = false;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Integer getScoreCount() {
        return scoreCount;
    }

    public Integer getShootCount() {
        return shootCount;
    }

    public boolean isAskPause() {
        return askPause;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isShootFree() {
        return shootFree;
    }

    public int getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }

    public void setAskPause(boolean askPause) {
        this.askPause = askPause;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setShootFree(boolean shootFree) {
        this.shootFree = shootFree;
    }
    public void setAskShoot(boolean ask) {
        askShoot = ask;
    }
    public boolean getAskShoot() {
        return askShoot;
    }
}
