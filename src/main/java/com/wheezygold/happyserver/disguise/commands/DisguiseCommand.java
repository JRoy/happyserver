package com.wheezygold.happyserver.disguise.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.disguise.DisguiseManager;
import com.wheezygold.happyserver.util.Color;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@ICommand.PlayerOnly
public class DisguiseCommand extends ICommand {

    private AccountManager accountManager;
    private DisguiseManager disguiseManager;

    public DisguiseCommand(AccountManager accountManager, DisguiseManager disguiseManager) {
        super("disguise", "", "happyserver.cmd", "d");
        this.accountManager = accountManager;
        this.disguiseManager = disguiseManager;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (accountManager.fetch(sender.getName()).getRank().getPower() >= Rank.ADMIN.getPower()) {
            if (args.length == 0) {
                if (disguiseManager.isDisguised(player)) {
                    if (disguiseManager.resetUser(player)) {
                        sender.sendMessage(D.M("Disguise", "You are no longer disguised!"));
                    } else {
                        sender.sendMessage(D.M("Disguise", "Unable to un-disguise you! You should be defaulted to a random skin."));
                    }
                } else {
                    sender.sendMessage(D.M("Disguise", "Correct Usage: " + Color.cWhite + "/disguise <playerName>"));
                }
            } else {
                if (disguiseManager.isDisguised(player)) {
                    sender.sendMessage(D.M("Disguise", "You are already disguised! Please un-disguise with /disguise"));
                    return;
                }
                if (!disguiseManager.isValidName(args[0])) {
                    sender.sendMessage(D.M("Disguise", "This name is already in use!"));
                    return;
                }
                if (disguiseManager.disguiseUser(player, args[0])) {
                    sender.sendMessage(D.M("Disguise", "Successfully Disguised!"));
                } else {
                    sender.sendMessage(D.M("Disguise", "Invalid Disguise Name!"));
                }
            }
        } else {
            sender.sendMessage(Rank.ADMIN.permMsg());
        }
    }
}
