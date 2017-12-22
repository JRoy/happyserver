package com.wheezygold.happyserver.common;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class DisguiseFactory extends SmallPlugin {

    private PacketAdapter adapter;

    private volatile Collection<WrappedSignedProperty> properties;

    public DisguiseFactory(JavaPlugin plugin)
    {
        super("Disguise Factory", plugin);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                realRun();
                if(properties == null)
                {
                    plugin.getLogger().warning("Failed to load the skin, player skins won't be affected by this plugin (Yes, that's bad).");
                }
            }

            void realRun()
            {
                WrappedGameProfile profile = WrappedGameProfile.fromOfflinePlayer( Bukkit.getOfflinePlayer("Notch"));
                Object handle = profile.getHandle();
                Object sessionService = getSessionService();
                try
                {
                    Method method = getFillMethod(sessionService);
                    method.invoke(sessionService, handle, true);
                }
                catch (IllegalAccessException | InvocationTargetException ex)
                {
                    ex.printStackTrace();
                    return;
                }
                profile = WrappedGameProfile.fromHandle(handle);
                properties = profile.getProperties().get("textures");
            }
        });

        this.adapter = new PacketAdapter(plugin, PacketType.Play.Server.PLAYER_INFO)
        {
            @Override
            public void onPacketSending(PacketEvent event)
            {
                if(properties == null)
                {
                    return;
                }
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerInfoAction action = packet.getPlayerInfoAction().read(0);
                if(action != EnumWrappers.PlayerInfoAction.ADD_PLAYER)
                {
                    return;
                }
                List<PlayerInfoData> data = packet.getPlayerInfoDataLists().read(0);
                for(PlayerInfoData pid : data)
                {
                    WrappedGameProfile profile = pid.getProfile();
                    profile.getProperties().removeAll("textures");
                    profile.getProperties().putAll("textures", properties);
                }
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(this.adapter);
    }

    private Object getSessionService()
    {
        Server server = Bukkit.getServer();
        try
        {
            Object mcServer = server.getClass().getDeclaredMethod("getServer").invoke(server);
            for (Method m : mcServer.getClass().getMethods())
            {
                if (m.getReturnType().getSimpleName().equalsIgnoreCase("MinecraftSessionService"))
                {
                    return m.invoke(mcServer);
                }
            }
        }
        catch (Exception ex)
        {
            throw new IllegalStateException("An error occurred while trying to get the session service", ex);
        }
        throw new IllegalStateException("No session service found :o");
    }

    private Method getFillMethod(Object sessionService)
    {
        for(Method m : sessionService.getClass().getDeclaredMethods())
        {
            if(m.getName().equals("fillProfileProperties"))
            {
                return m;
            }
        }
        throw new IllegalStateException("No fillProfileProperties method found in the session service :o");
    }

}