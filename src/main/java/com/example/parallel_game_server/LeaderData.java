package com.example.parallel_game_server;

public class LeaderData {
    private String name;
    private int count;

    public LeaderData(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public LeaderData(LeaderData ld) {
        this.name = ld.name;
        this.count = ld.count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
