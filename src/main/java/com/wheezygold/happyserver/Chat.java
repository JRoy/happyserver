package com.wheezygold.happyserver;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.account.HappyPlayer;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.common.SmallPlugin;
import com.wheezygold.happyserver.util.Color;
import com.wheezygold.happyserver.util.D;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Chat extends SmallPlugin implements Listener {

    private AccountManager accountManager;
    private boolean silenced = false;
    private HashMap<Player, String> lastMessages = new HashMap<>();

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

        if (silenced && happyPlayer.getRank().getPower() < Rank.MOD.getPower()) {
            sender.sendMessage(D.M("Chat", "The chat is silenced."));
            event.setCancelled(true);
            return;
        }

        if (lastMessages.containsKey(event.getPlayer()) && lastMessages.get(event.getPlayer()).equals(event.getMessage()) && happyPlayer.getRank().getPower() < Rank.ADMIN.getPower()) {
            sender.sendMessage(D.M("Chat", "Your message is too similar to your last one!"));
            event.setCancelled(true);
            return;
        }

        event.setMessage(event.getMessage());

        String rankDisplay = happyPlayer.getRank().getPrefix();
        String subrankDisplay = happyPlayer.getSubRank().getPrefix();

        if (!subrankDisplay.isEmpty() && happyPlayer.getSubRank().getPower() != 0) {
            subrankDisplay = Color.cAqua + "[" + subrankDisplay + Color.cAqua + "] ";
        }

        if (!rankDisplay.isEmpty() && happyPlayer.getRank().getPower() != 0)
            rankDisplay = rankDisplay + " ";

        event.setFormat(subrankDisplay + rankDisplay + Color.cYellow + senderName + " " + Color.cWhite + "%2$s");

        lastMessages.put(sender, event.getMessage());

    }

    @EventHandler
    public void pingCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/ping")) {
            event.getPlayer().sendMessage(D.M("Ping", "Get Frickin Ping Ponged!"));
            event.setCancelled(true);
        }
    }

    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }

    public boolean isSilenced() {
        return silenced;
    }
}
