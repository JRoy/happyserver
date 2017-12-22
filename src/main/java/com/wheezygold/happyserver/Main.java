package com.wheezygold.happyserver;

import com.wheezygold.happyserver.managers.HubManager;
import com.wheezygold.happyserver.util.C;
import com.wheezygold.happyserver.util.D;
import me.jamesj.icommandframework.CommandFramework;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static CommandFramework framework;

    @Override
    public void onEnable() {
        C.log("Initialized!");
        C.log("Loading Commands...");
        loadCommands();
        loadConfig();
        HubManager hubManager = new HubManager(this);
        ICommand.Language.PLAYER_ONLY = D.M("Command", "This command is a player-only command!");
        ICommand.Language.COMMAND_DISABLED = D.M("Command", "This command is currently disabled!");
        framework.registerCommands();
    }

    private void loadConfig() {
        getConfig().addDefault("sqlPass", "password");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void loadCommands() {
        framework = new CommandFramework(this);
    }

    public static CommandFramework getFramework() { return framework; }

}