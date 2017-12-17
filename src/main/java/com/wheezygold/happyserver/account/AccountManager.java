package com.wheezygold.happyserver.account;

import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.common.SQLManager;
import com.wheezygold.happyserver.common.SmallPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class AccountManager extends SmallPlugin {

    private SQLManager sqlManager;
    private HashMap<String, HappyPlayer> activePlayers = new HashMap<>();

    public AccountManager(SQLManager sqlManager, JavaPlugin plugin) {
        super("Account Manager", plugin);
        this.sqlManager = sqlManager;
    }

    public boolean addPlayer(Player player) {
        if (activePlayers.containsKey(player.getName())) {
            activePlayers.remove(player.getName());
        }
        HappyPlayer newPlayer = new HappyPlayer(player);
        if (!sqlManager.isActive(player.getUniqueId().toString())) {
            sqlManager.createUser(player.getUniqueId().toString(), player.getName());
        }
        try {
            newPlayer.setRank(Rank.valueOf(sqlManager.getRank(player.getUniqueId().toString())));
        } catch (IllegalArgumentException | NullPointerException e) {
            log("Invalid rank at user: " + player.getName());
            return false;
        }
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

}
