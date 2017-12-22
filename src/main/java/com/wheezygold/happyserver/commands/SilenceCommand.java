package com.wheezygold.happyserver.commands;

import com.wheezygold.happyserver.Chat;
import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class SilenceCommand extends ICommand {

    private AccountManager accountManager;
    private Chat chat;

    public SilenceCommand(AccountManager accountManager, Chat chat) {
        super("silence", "", "happyserver.cmd");
        this.accountManager = accountManager;
        this.chat = chat;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        if (accountManager.fetch(commandSender.getName()).getRank().getPower() >= Rank.ADMIN.getPower()) {
            if (chat.isSilenced()) {
                chat.setSilenced(false);
                Bukkit.broadcastMessage(D.M("Chat", "The chat is no longer silenced!"));
            } else {
                chat.setSilenced(true);
                Bukkit.broadcastMessage(D.M("Chat", "The chat has been silenced!"));
            }
        } else {
            commandSender.sendMessage(Rank.ADMIN.permMsg());
        }
    }
}
