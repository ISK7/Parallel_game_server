package com.example.parallel_game_server;

public class Player extends Thread{
    int y;
    int x;
    final int startX = 50;
    final int speed = 8;
    Integer scoreCount;
    Integer shootCount;
    boolean shootFree;
    boolean askShoot;
    boolean askPause;
    boolean ready;
    GameRunner parent;
    int number;
    String name;

    public Player(PlayerData pd) {
        x = pd.getX();
        y = pd.getY();
        scoreCount = pd.getScoreCount();
        shootCount = pd.getShootCount();
        shootFree = pd.isShootFree();
        askShoot = false;
        askPause = pd.isAskPause();
        ready = pd.isReady();
        number = pd.getNumber();
    }
    public Player(int X, String Name, GameRunner gr) {
        shootFree = true;
        askPause = false;
        askShoot = false;
        ready = false;
        scoreCount = 0;
        shootCount = 0;
        x = X;
        name = Name;
        parent = gr;
    }

    public boolean isReady() {return ready;}
    public boolean isAskPause() {return askPause;}
    public void tryToShoot() {
        if(shootFree && parent.isAlive()) {
            shootFree = false;
            shootCount++;
            start();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                //пауза
                if (parent.isPause()) {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException ex) {
                        throw ex;
                    }
                }
                //перемещение снаряда
                x += speed;
                //проверка на пересечение
                int c = parent.checkCross(x,y);
                if (c > -1) {
                    scoreCount += c;
                    finShoot();
                    break;
                }
                Thread.sleep(20);
            }
        } catch (InterruptedException ex) {
            finShoot();
            System.out.println(ex);
        }
    }

    private void finShoot() {
        x = startX;
        shootFree = true;
    }
    public void End() {
        shootCount = scoreCount = 0;
        finShoot();
        ready = false;
        interrupt();
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

    public boolean isShootFree() {
        return shootFree;
    }

    public boolean isAskShoot() {
        return askShoot;
    }

    public int getNumber() {
        return number;
    }

    public void setAskShoot(boolean askShoot) {
        this.askShoot = askShoot;
    }

    public void setAskPause(boolean askPause) {
        this.askPause = askPause;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
    public void setY(int Y) {y = Y;}
    public void setNumber(int n) {number = n;}
    public String getPlayerName() {return name;}
}
