package com.example.parallel_game_server;

import javafx.application.Platform;

import java.util.ArrayList;

public class GameRunner extends Thread {
    ArrayList<Player> players = new ArrayList<>();
    int bigTeeSpeed = 2;
    int smallTeeSpeed = 2 * bigTeeSpeed;
    boolean isPause = false;
    boolean isActive = false;

    double maxy = 350;
    double miny = 0;
    double bigy;
    double smally;
    double bigx = 340;
    double smallx = 400;
    int bigr = 30;
    int smallr = 15;
    RunServer parent;
    boolean hasWinner = false;
    String winner;

    public GameRunner(RunServer rs) {
        bigy = 0;
        smally = 0;
        bigx -= bigr;
        smallx -= smallr;
        parent = rs;
    }

    @Override
    public void run() {
        cycle();
    }

    private void toNonActive() {
        isActive = false;
        isPause = false;
        hasWinner = false;
        winner = "";
        bigy = 0;
        smally = 0;
    }
    public void addVinner(String wn) {
        winner = wn;
        hasWinner = true;
        DBSessionManager.addWin(wn);
    }

    public void cycle() {
        try {
            while (true) {
                if(!isActive) {
                    Thread.sleep(20);
                    continue;
                }
                if(hasWinner) {
                    parent.resetData(new GameData(this));
                    toNonActive();
                    Thread.sleep(20);
                    continue;
                }
                if (isPause) {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException ex) {
                        throw ex;
                    }
                }
                //Перемещение большей мишени
                bigy += bigTeeSpeed;
                if (bigy > maxy - 2 * bigr && bigTeeSpeed > 0) bigTeeSpeed *= -1;
                if (bigy < miny && bigTeeSpeed < 0) bigTeeSpeed *= -1;
                //Перемещение меньшей мишени
                smally += smallTeeSpeed;
                if (smally > maxy - 2 * smallr && smallTeeSpeed > 0) smallTeeSpeed *= -1;
                if (smally < miny && smallTeeSpeed < 0) smallTeeSpeed *= -1;
                parent.resetData(new GameData(this));
                Thread.sleep(20);
            }
        } catch (InterruptedException ex) {
            for(Player i : players) i.End();
            bigy = miny + bigr;
            smally = miny + smallr;
            System.out.println(ex);
        }
    }

    public boolean isPause() {
        return isPause;
    }
    public int checkCross(int x, int y) {
        if (Math.abs((x - bigx - bigr) * (x - bigx - bigr) + (y - bigy - bigr) * (y - bigy - bigr)) < bigr * bigr) {
            return 1;
        }
        if (Math.abs((x - smallx - smallr) * (x - smallx - smallr) + (y - smally - smallr) * (y - smally - smallr)) < smallr * smallr) {
            return 2;
        }
        if (x > smallx + smallr + 5) {
            return 0;
        }
        return -1;
    }

    public void Pause() {
        if (isPause) {
            synchronized (this) {
                notifyAll();
            }
            isPause = false;
            return;
        }
        isPause = true;
    }

    public int AddPlayer(Player p) {
        //if(!DBSessionManager.newPlayerInGame(p.name, p.getPassword())) return -4;
        if(!DBSessionManager.newPlayerInGame(p.name,"placeholder")) return -4;
        if(players.size() >= 4) return -3;
        if(isActive) return -2;
        for(Player pl : players) {
            String pn = p.getPlayerName();
            String pln = pl.getPlayerName();
            if(pn.equals(pln))
                return -1;
        }
        players.add(p);
        int distance = (int)(maxy / (players.size()+1));
        for(int i = 0; i < players.size(); i++) {
            players.get(i).setY(distance*(i+1));
        }
        return players.size()-1;
    }

    public void dellPlayer(int num) {
        try {
            if (players.get(num) != null) {
                Player pl = players.get(num);
                players.remove(pl);
                pl.setIsActive(false);
            }
        } catch (Exception ex){

        }
    }
    public void askShoot(int n) {
        players.get(n).tryToShoot();
    }
    public void askChangeReady(int n) {
        players.get(n).setReady(!players.get(n).isReady());
        for(Player p : players) {
            if(!p.isReady()) return;
        }
        isActive = true;
    }
    public void askChangePause(int n) {
        players.get(n).setAskPause(!players.get(n).isAskPause());
        for(Player p : players) {
            if (p.isAskPause()) {
                isPause = true;
                return;
            }
        }
        this.Pause();
    }

    public ArrayList<LeaderData> askLeaders() {
        return DBSessionManager.getWinners();
    }

    public ArrayList<Player> getPlayers() {
        return players;
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

    public int getBigr() {
        return bigr;
    }

    public int getSmallr() {
        return smallr;
    }
}
