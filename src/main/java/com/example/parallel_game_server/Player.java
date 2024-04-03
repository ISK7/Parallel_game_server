package com.example.parallel_game_server;

public class Player extends Thread{
    int y;
    int x;
    final int startX = 70;
    final int speed = 8;
    Integer scoreCount;
    Integer shootCount;
    boolean shootFree;
    boolean askShoot;
    boolean askPause;
    boolean ready;
    GameRunner parent;
    int number;
    boolean isActive;
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
        isActive = true;
    }
    public Player(String Name, GameRunner gr) {
        shootFree = true;
        askPause = false;
        askShoot = false;
        isActive = true;
        ready = false;
        scoreCount = 0;
        shootCount = 0;
        x = startX;
        name = Name;
        parent = gr;
        start();
    }

    public boolean isReady() {return ready;}
    public boolean isAskPause() {return askPause;}
    public void tryToShoot() {
        if(shootFree && parent.isAlive() && ready) {
            shootFree = false;
            shootCount++;
        }
    }

    @Override
    public void run() {
        try {
            while(isActive) {
                //пауза
                if (parent.isPause()) {
                    Thread.sleep(20);
                    continue;
                }
                if(shootFree) {
                    Thread.sleep(20);
                    continue;
                }
                //перемещение снаряда
                x += speed;
                //проверка на пересечение
                int c = parent.checkCross(x,y);
                if (c > -1) {
                    scoreCount += c;
                    finShoot();
                    if(scoreCount >= 6) parent.addVinner(name);
                }
                Thread.sleep(20);
            }
        } catch (InterruptedException ex) {
            finShoot();
            System.out.println(ex);
        }
        System.out.println("thread stop");
    }

    public void setIsActive(boolean a) {
        isActive = a;
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
