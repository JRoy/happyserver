package com.wheezygold.happyserver.commands.fun;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.account.HappyPlayer;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.util.Color;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.command.CommandSender;

public class SmiteCommand extends ICommand {

    private AccountManager accountManager;

    public SmiteCommand(AccountManager accountManager) {
        super("smite", "", "happyserver.cmd");
        this.accountManager = accountManager;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        if (accountManager.fetch(commandSender.getName()).getRank().getPower() >= Rank.ADMIN.getPower()) {
            if (strings.length != 1) {
                commandSender.sendMessage(D.M("Smite", "Correct Usage: " + Color.cWhite + "/smite <user>"));
                return;
            }
            if (!accountManager.contains(strings[0])) {
                commandSender.sendMessage(D.M("Smite", "Correct Usage: " + Color.cWhite + "/smite <user>"));
                return;
            }
            HappyPlayer target = accountManager.fetch(strings[0]);
            target.getPlayer().getWorld().strikeLightning(target.getPlayer().getLocation());
            target.getPlayer().sendMessage(D.M("Smite", "you got the smite. lol gg."));
            commandSender.sendMessage(D.M("Smite", "Smite the target!"));
        } else {
            commandSender.sendMessage(Rank.ADMIN.permMsg());
        }
    }
}
