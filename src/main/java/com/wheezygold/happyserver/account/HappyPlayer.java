package com.wheezygold.happyserver.account;

import com.wheezygold.happyserver.common.Rank;
import org.bukkit.entity.Player;

public class HappyPlayer {

    private String playerName;
    private Player player;
    private Rank rank;

    HappyPlayer(Player player) {
        this.player = player;
        this.playerName = player.getName();
    }

    public HappyPlayer(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void delete() {
        this.playerName = null;
        this.player = null;
    }


    public Rank getRank() {
        return this.rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

}