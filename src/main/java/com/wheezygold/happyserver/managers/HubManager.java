package com.wheezygold.happyserver.managers;

import com.wheezygold.happyserver.Chat;
import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.SQLManager;
import com.wheezygold.happyserver.common.SmallPlugin;
import com.wheezygold.happyserver.util.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HubManager extends SmallPlugin implements Listener {

    public JavaPlugin plugin;
    public AccountManager accountManager = null;
    public Chat chatManager = null;
    public SQLManager sqlManager = null;

    public HubManager(JavaPlugin plugin) {
        super("Hub Manager", plugin);
        this.plugin = plugin;
        loadModules();
    }

    public void loadModules() {
        if (sqlManager == null)
            sqlManager = new SQLManager(plugin);
        if (accountManager == null)
            accountManager = new AccountManager(sqlManager, plugin);
        if (chatManager == null)
            chatManager = new Chat(plugin, accountManager);
    }

    @EventHandler
    private void onPing(ServerListPingEvent event) {
        event.setMotd(Color.cYellow + Color.Bold + "happyserver 0.1");
        event.setMaxPlayers(event.getNumPlayers() + 1);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(Color.cDGray + "Join> " + Color.cGray + event.getPlayer().getName());
        if (!accountManager.addPlayer(event.getPlayer())) {
            event.getPlayer().kickPlayer("Error while processing your login data!");
        }
    }

    private void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(Color.cDGray + "Quit> " + Color.cGray + event.getPlayer().getName());
        if (!accountManager.dropPlayer(event.getPlayer().getName()))
            log("Error while removing player from active players!");
    }

}
