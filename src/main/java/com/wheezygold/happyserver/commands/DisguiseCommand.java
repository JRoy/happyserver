package com.wheezygold.happyserver.commands;

import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.DisguiseFactory;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.command.CommandSender;

public class DisguiseCommand extends ICommand {

    private AccountManager accountManager;
    private DisguiseFactory disguiseFactory;

    public DisguiseCommand(AccountManager accountManager, DisguiseFactory disguiseFactory) {
        super("disguise", "", "happyserver.cmd", "d");
        this.accountManager = accountManager;
        this.disguiseFactory = disguiseFactory;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] args) {
        //lol
    }
}
