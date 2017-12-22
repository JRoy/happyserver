package com.wheezygold.happyserver.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.wheezygold.happyserver.Chat;
import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.account.HappyPlayer;
import com.wheezygold.happyserver.commands.*;
import com.wheezygold.happyserver.common.Rank;
import com.wheezygold.happyserver.common.SQLManager;
import com.wheezygold.happyserver.common.SmallPlugin;
import com.wheezygold.happyserver.util.Color;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HubManager extends SmallPlugin implements Listener {

    private JavaPlugin plugin;
    private AccountManager accountManager = null;
    private Chat chatManager = null;
    private SQLManager sqlManager = null;

    public HubManager(JavaPlugin plugin) {
        super("Hub Manager", plugin);
        this.plugin = plugin;
        loadModules();
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, ListenerPriority.NORMAL,
            Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final List<WrappedGameProfile> names = new ArrayList<WrappedGameProfile>();
                names.add(new WrappedGameProfile("1", Color.cAqua + "--------------------------" + Color.cGreen + "=" + Color.cAqua + "--------------------------"));
                names.add(new WrappedGameProfile("2", Color.cYellow + "                   Happyheart's Official Server"));
                names.add(new WrappedGameProfile("3", Color.cBlue + "        Join the official discord at: discord.gg/smZmhKa"));
                names.add(new WrappedGameProfile("4", Color.cGold + "   Support happyheart on Patreon: patreon.com/happyheart"));
                names.add(new WrappedGameProfile("5", Color.cAqua + "--------------------------" + Color.cGreen + "=" + Color.cAqua + "--------------------------"));
                event.getPacket().getServerPings().read(0).setPlayers(names);
                event.getPacket().getServerPings().read(0).setVersionName("happyserver");
            }
        });
//        new DisguiseFactory(plugin);
    }

    private void loadModules() {
        if (sqlManager == null)
            sqlManager = new SQLManager(plugin);
        if (accountManager == null)
            accountManager = new AccountManager(sqlManager, plugin);
        if (chatManager == null)
            chatManager = new Chat(plugin, accountManager);
        loadGeneralCommands();
    }

    private void loadGeneralCommands() {
        addCommand(new GamemodeCommand(accountManager));
        addCommand(new SilenceCommand(accountManager, chatManager));
        addCommand(new AnnounceCommand(accountManager));
        addCommand(new BroadcastCommand(accountManager));
        addCommand(new FlyCommand(accountManager));
        addCommand(new StaffChatCommand(accountManager));
    }

    @EventHandler
    private void onPing(ServerListPingEvent event) {
        event.setMotd(Color.cYellow + Color.Bold + "happyserver 0.1");
        event.setMaxPlayers(event.getNumPlayers() + 1);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        PermissionAttachment permissionAttachment = event.getPlayer().addAttachment(plugin);
        permissionAttachment.setPermission("happyserver.cmd", true);
        event.setJoinMessage(Color.cDGray + "Join> " + Color.cGray + event.getPlayer().getName());
        if (!accountManager.addPlayer(event.getPlayer())) {
            event.getPlayer().kickPlayer("Error while processing your login data!");
        }

        event.getPlayer().setGameMode(GameMode.SURVIVAL);

        HappyPlayer happyPlayer = accountManager.fetch(event.getPlayer().getName());

        if (happyPlayer.getRank() == Rank.NONE) {
            event.getPlayer().setPlayerListName(Color.cWhite + event.getPlayer().getName());
        } else {
            event.getPlayer().setPlayerListName(happyPlayer.getRank().getPrefix() + Color.cWhite + " " + event.getPlayer().getName());
        }

        event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());

        IChatBaseComponent tabHead = IChatBaseComponent.ChatSerializer.a("{\"text\": \"§e§lhappyserver\"}");
        IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"§d§l<insert funny message here>\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field hF = packet.getClass().getDeclaredField("a");
            hF.setAccessible(true);
            hF.set(packet, tabHead);
            hF.setAccessible(!hF.isAccessible());

            Field fF = packet.getClass().getDeclaredField("b");
            fF.setAccessible(true);
            fF.set(packet, tabFoot);
            fF.setAccessible(!hF.isAccessible());
            ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(Color.cDGray + "Quit> " + Color.cGray + event.getPlayer().getName());
        if (!accountManager.dropPlayer(event.getPlayer().getName()))
            log("Error while removing player from active players!");
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    private void onPvp(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onWeather(WeatherChangeEvent event) {
        event.getWorld().setStorm(false);
    }
}
