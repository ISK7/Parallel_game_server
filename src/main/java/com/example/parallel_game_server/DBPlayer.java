package com.example.parallel_game_server;
import java.io.Serializable;
import java.io.StringReader;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class DBPlayer {
    @Id
    @Column(name = "player_id")
    private int id;

    @Column(name = "player_win_count")
    private int wins;

    @Column(name = "player_name")
    private String name;

    @Column(name = "player_password")
    private String password;

    public DBPlayer() {}

    public int getId() {
        return id;
    }

    public int getWins() {
        return wins;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
