package com.wheezygold.happyserver.account;

import com.wheezygold.happyserver.account.commands.RankCommand;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.common.SQLManager;
import com.wheezygold.happyserver.common.SmallPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.HashMap;

public class AccountManager extends SmallPlugin {

    private SQLManager sqlManager;
    private HashMap<String, HappyPlayer> activePlayers = new HashMap<>();

    public AccountManager(SQLManager sqlManager, JavaPlugin plugin) {
        super("Account Manager", plugin);
        this.sqlManager = sqlManager;
    }

    @Override
    public void addCommands() {
        addCommand(new RankCommand(this));
    }

    public boolean addPlayer(Player player) {
        if (activePlayers.containsKey(player.getName())) {
            activePlayers.remove(player.getName());
        }
        HappyPlayer newPlayer = new HappyPlayer(player, this);
        if (!sqlManager.isActive(player.getUniqueId().toString())) {
            if (!sqlManager.createUser(player.getUniqueId().toString(), player.getName())) {
                log("Not able to create user!");
                return false;
            }
        }
        try {
            newPlayer.setRank(Rank.valueOf(sqlManager.getRank(player.getUniqueId().toString())));
        } catch (IllegalArgumentException | NullPointerException e) {
            log("Invalid rank at user: " + player.getName());
            return false;
        }
        newPlayer.setTokens(sqlManager.getTokens(player.getUniqueId().toString()));
        activePlayers.put(player.getName(), newPlayer);
        return true;
    }

    public boolean dropPlayer(String name) {
        if (!activePlayers.containsKey(name))
            return false;
        activePlayers.remove(name);
        return true;
    }

    public boolean contains(String playerName) {
        return activePlayers.containsKey(playerName);
    }

    public HappyPlayer fetch(String playerName) {
        return activePlayers.get(playerName);
    }

    public void update(HappyPlayer target) {
        sqlManager.updateUser(target);
    }

    public Connection getConnection() {
        return sqlManager.connection;
    }

}
