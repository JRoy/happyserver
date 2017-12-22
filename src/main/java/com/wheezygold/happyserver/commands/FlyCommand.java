package com.wheezygold.happyserver.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends ICommand {

    private AccountManager accountManager;

    public FlyCommand(AccountManager accountManager) {
        super("fly", "", "happyserver.cmd");
        this.accountManager = accountManager;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        if (accountManager.fetch(commandSender.getName()).getRank().getPower() >= Rank.HELPER.getPower()) {
            if (!((Player) commandSender).getAllowFlight()) {
                ((Player) commandSender).setAllowFlight(true);
                commandSender.sendMessage(D.M("Fly", "Enabled flight!"));
                return;
            }
            ((Player) commandSender).setAllowFlight(false);
            commandSender.sendMessage(D.M("Fly", "Disabled flight!"));
        } else {
            commandSender.sendMessage(Rank.HELPER.permMsg());
        }
    }

}
