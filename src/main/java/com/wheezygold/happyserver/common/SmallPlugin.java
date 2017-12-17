package com.wheezygold.happyserver.common;

import com.wheezygold.happyserver.Main;
import com.wheezygold.happyserver.util.D;
import com.wheezygold.happyserver.util.UtilTime;
import me.jamesj.icommandframework.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public abstract class SmallPlugin implements Listener {

    protected String moduleName = "Unknown";
    protected JavaPlugin plugin;
    protected HashMap<String, ICommand> commandHashMap;

    public SmallPlugin(String moduleName, JavaPlugin plugin) {
        this.moduleName = moduleName;
        this.plugin = plugin;

        commandHashMap = new HashMap<>();

        onEnable();
        registerEvents(this);
    }

    public PluginManager getManager() {
        return this.plugin.getServer().getPluginManager();
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public void registerEvents(Listener listener) {
        getManager().registerEvents(listener, this.plugin);
    }

    public void registerThis() {
        registerEvents(this);
    }

    public final void onEnable() {
        long cur = System.currentTimeMillis();
        log("Initializing...");
        enable();
        addCommands();
        log("Enabled in " + UtilTime.diffString(cur, System.currentTimeMillis()) + ".");
    }

    public final void onDisable() {
        disable();
        log("Disabled.");
    }

    public static void addCommand(ICommand command) {
        Main.getFramework().registerCommand(command);
    }

    public void enable() { }

    public void disable() { }

    public void addCommands() { }

    public void log(String info) {
        Bukkit.getLogger().info(D.M(this.moduleName, info));
    }

    public void runAsyncTask(Runnable runnable) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    public void runSyncTask(Runnable runnable) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, runnable);
    }

    public void runSyncTaskLater(Runnable runnable, long later) {
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, runnable, later);
    }

    public void runAsyncTaskLater(Runnable runnable, long later) {
        this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, later);
    }

}