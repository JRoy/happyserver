package com.wheezygold.happyserver.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@ICommand.PlayerOnly
public class GamemodeCommand extends ICommand {

    private AccountManager accountManager;

    public GamemodeCommand(AccountManager accountManager) {
        super("gm", "", "happyserver.cmd", "gamemode");
        this.accountManager = accountManager;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        if (accountManager.fetch(commandSender.getName()).getRank().getPower() >= Rank.ADMIN.getPower()) {
            Player player = (Player) commandSender;
            if (player.getGameMode() == GameMode.SURVIVAL) {
                player.setGameMode(GameMode.CREATIVE);
                commandSender.sendMessage(D.M("GameMode", "Toggled your gamemode!"));
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                commandSender.sendMessage(D.M("GameMode", "Toggled your gamemode!"));
            }
        } else {
            commandSender.sendMessage(Rank.ADMIN.permMsg());
        }
    }
}
