package com.wheezygold.happyserver.account.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.account.HappyPlayer;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.common.SubRank;
import com.wheezygold.happyserver.util.Color;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.command.CommandSender;

public class SubRankCommand extends ICommand {

    private AccountManager accountManager;

    public SubRankCommand(AccountManager accountManager) {
        super("subrank", "", "happyserver.cmd");
        this.accountManager = accountManager;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        HappyPlayer happyPlayer = accountManager.fetch(commandSender.getName());
        if (happyPlayer.getRank().getPower() >= Rank.ADMIN.getPower()) {
            if (strings.length != 2) {
                commandSender.sendMessage(D.M("Client", "Correct Usage: /subrank <player> <rank>"));
                return;
            }
            if (!accountManager.contains(strings[0])) {
                commandSender.sendMessage(D.M("Client", Color.cRed + Color.Bold + "Invalid Player!"));
                return;
            }
            SubRank targetRank = null;
            try {
                targetRank = SubRank.valueOf(strings[1]);
            } catch (IllegalArgumentException | NullPointerException e) {
                commandSender.sendMessage(D.M("Client", Color.cRed + Color.Bold + "Invalid Rank!"));
                return;
            }
            if (happyPlayer.getRank().getPower() < targetRank.getPower()) {
                commandSender.sendMessage(D.M("Client", Color.cRed + Color.Bold + "Insufficient Permission!"));
                return;
            }
            HappyPlayer targetPlayer = accountManager.fetch(strings[0]);
            targetPlayer.setSubRank(targetRank).update();
            commandSender.sendMessage(D.M("Client", "Updated " + strings[0] + "'s sub-rank to " + targetRank.getName() + "!"));
            targetPlayer.getPlayer().sendMessage(D.M("Client", "Your sub-rank has been updated to " + targetRank.getName() + "!"));
        } else {
            commandSender.sendMessage(Rank.ADMIN.permMsg());
        }
    }
}
