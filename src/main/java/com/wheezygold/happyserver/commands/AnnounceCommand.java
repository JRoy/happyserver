package com.wheezygold.happyserver.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@ICommand.PlayerOnly
public class AnnounceCommand extends ICommand {

    private AccountManager accountManager;

    public AnnounceCommand(AccountManager accountManager) {
        super("announce", "", "happyserver.cmd");
        this.accountManager = accountManager;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        if (accountManager.fetch(commandSender.getName()).getRank().getPower() >= Rank.ADMIN.getPower()) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.sendTitle("§bAnnouncement", "§e" + String.join(" ", strings));
                p.sendMessage(D.M("Announcement", String.join(" ", strings)));
            });
        } else {
            commandSender.sendMessage(Rank.ADMIN.permMsg());
        }

    }
}
