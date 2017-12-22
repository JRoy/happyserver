package com.wheezygold.happyserver.account;

import com.wheezygold.happyserver.common.Rank;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HappyPlayer {

    private AccountManager accountManager;
    private UUID uuid;
    private String playerName;
    private Player player;
    private Rank rank;
    private int tokens;

    HappyPlayer(Player player, AccountManager accountManager) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.accountManager = accountManager;
    }

    public HappyPlayer(String playerName, AccountManager accountManager) {
        this.playerName = playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HappyPlayer setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public Rank getRank() {
        return this.rank;
    }

    public HappyPlayer setRank(Rank rank) {
        this.rank = rank;
        return this;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void update() {
        accountManager.update(this);
    }

    public void delete() {
        this.playerName = null;
        this.player = null;
    }

}