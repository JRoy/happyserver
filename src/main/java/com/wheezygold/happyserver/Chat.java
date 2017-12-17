package com.wheezygold.happyserver;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.account.HappyPlayer;
import com.wheezygold.happyserver.common.SmallPlugin;
import com.wheezygold.happyserver.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Chat extends SmallPlugin implements Listener {

    private AccountManager accountManager;

    public Chat(JavaPlugin plugin, AccountManager accountManager) {
        super("Chat", plugin);
        this.accountManager = accountManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        Player sender = event.getPlayer();
        String senderName = sender.getName();
        HappyPlayer happyPlayer = accountManager.fetch(senderName);

        event.setMessage(event.getMessage());

        String rankDisplay = happyPlayer.getRank().getPrefix();

        if (!rankDisplay.isEmpty())
            rankDisplay = rankDisplay + " ";

        event.setFormat(rankDisplay + Color.cYellow + senderName + " " + Color.cWhite + "%2$s");


    }

}
