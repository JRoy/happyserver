package com.wheezygold.happyserver.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.util.Color;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@ICommand.PlayerOnly
public class StaffChatCommand extends ICommand {

    private AccountManager accountManager;

    public StaffChatCommand(AccountManager accountManager) {
        super("staffchat", "", "happyserver.cmd", "sc", "schat", "staffc");
        this.accountManager = accountManager;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        if (accountManager.fetch(commandSender.getName()).getRank().getPower() >= Rank.HELPER.getPower()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (accountManager.fetch(player.getName()).getRank().getPower() >= Rank.HELPER.getPower()) {
                    player.sendMessage(Color.cAqua + "Staff Chat -> " + Color.cYellow + commandSender.getName() + " " + Color.cWhite + String.join(" ", strings));
                }
            }
        } else {
            commandSender.sendMessage(Rank.HELPER.permMsg());
        }
    }
}
