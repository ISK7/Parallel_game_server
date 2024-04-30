package com.example.parallel_game_server;

import com.example.parallel_game_server.Player;

public class PlayerData {
    int y;
    int x;
    String name;
    String password;
    Integer scoreCount;
    Integer shootCount;
    boolean askPause;
    boolean ready;
    boolean shootFree;
    boolean askShoot;
    boolean askLeaders;
    int number;
    public PlayerData(PlayerData pd) {
        x = pd.x;
        y = pd.y;
        name = pd.name;
        scoreCount = pd.scoreCount;
        shootCount = pd.shootCount;
        askPause = pd.askPause;
        ready = pd.ready;
        shootFree = pd.shootFree;
        askShoot = pd.askShoot;
        number = pd.number;
        askLeaders = pd.askLeaders;
        password = pd.password;
    }
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
        askLeaders = false;
        password = null;
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

    public void setAskPause(boolean pause) {
        askPause = pause;
    }

    public void setReady(boolean askReady) {
        ready = askReady;
    }

    public void setShootFree(boolean askShootFree) {
        shootFree = askShootFree;
    }
    public void setAskShoot(boolean ask) {
        askShoot = ask;
    }
    public boolean getAskShoot() {
        return askShoot;
    }
    public void setAskLeaders(boolean ask) {
        askLeaders = ask;
    }
    public boolean isAskLeaders() {return askLeaders;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
