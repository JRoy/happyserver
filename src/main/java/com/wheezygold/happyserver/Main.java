package com.wheezygold.happyserver;

import com.wheezygold.happyserver.util.C;
import me.jamesj.icommandframework.CommandFramework;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static CommandFramework framework;

    @Override
    public void onEnable() {
        C.log("Initialized!");
        C.log("Loading Commands...");
        loadCommands();
    }

    private void loadCommands() {
        framework = new CommandFramework(this);
    }

}