package com.wheezygold.happyserver.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BroadcastCommand extends ICommand {

    private AccountManager accountManager;

    public BroadcastCommand(AccountManager accountManager) {
        super("broadcast", "", "happyserver.cmd", "s", "bc");
        this.accountManager = accountManager;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        if (accountManager.fetch(commandSender.getName()).getRank().getPower() >= Rank.MOD.getPower()) {
            Bukkit.broadcastMessage(D.M("Broadcast", String.join(" ", strings)));
        } else {
            commandSender.sendMessage(Rank.MOD.permMsg());
        }
    }
}
