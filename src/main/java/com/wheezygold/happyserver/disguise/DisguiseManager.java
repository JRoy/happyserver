package com.wheezygold.happyserver.disguise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.wheezygold.happyserver.account.AccountManager;
import com.wheezygold.happyserver.common.SmallPlugin;
import com.wheezygold.happyserver.disguise.commands.DisguiseCommand;
import com.wheezygold.happyserver.util.UtilWeb;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class DisguiseManager extends SmallPlugin {

    private HashMap<Player, String> disguisedPlayers = new HashMap<>();

    public DisguiseManager(JavaPlugin plugin) {
        super("Disguise Manager", plugin);
    }

    @Override
    public void addCommands() {
        addCommand(new DisguiseCommand(AccountManager.getInstance(), this));
    }

    public boolean disguiseUser(Player player, String targetPlayer) {
        boolean valid = changeSkin(player, targetPlayer);
        if (valid) {
            disguisedPlayers.put(player, targetPlayer);
            return true;
        } else {
            return false;
        }
    }

    public boolean resetUser(Player player) {
        disguisedPlayers.remove(player);
        return changeSkin(player, player.getName());
    }

    public boolean isValidName(String name) {
        return !Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name)) && !disguisedPlayers.containsValue(name);
    }

    public boolean isDisguised(Player player) {
        return disguisedPlayers.containsKey(player);
    }

    private JSONObject fetchTextures(String uuid) {
        try {
            return (JSONObject) ((JSONObject) UtilWeb.getJSONResponse("https://api.mineskin.org/generate/user/" + uuid).get("data")).get("texture");
        } catch (NullPointerException e) {
            log("NPE while fetching textures, will attempt to recover!");
            return null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private boolean changeSkin(Player p, String targetSkin) {
        boolean requestedSkin = true;
        String value;
        String signature;

        JSONObject uuidResponse = UtilWeb.getJSONResponse("https://api.mojang.com/users/profiles/minecraft/" + targetSkin);
        JSONObject textureData = fetchTextures(uuidResponse.get("id").toString());
        if (textureData == null) {
            requestedSkin = false;
            value = "eyJ0aW1lc3RhbXAiOjE0OTIzNDI0NzE3NjcsInByb2ZpbGVJZCI6ImY4NGM2YTc5MGE0ZTQ1ZTA4NzliY2Q0OWViZDRjNGUyIiwicHJvZmlsZU5hbWUiOiJIZXJvYnJpbmUiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JkZDNjYzhhZTI5YTRkZmU1NjVkZGNkN2E2NjMzODhkOGVhZDdmNWJjOGRiYTVhNDkzMTQ4YjI0NTk4NGQifX19";
            signature = "J1537dRxCJT1Up2LjBnM8sR2HERJCIZ1LWSUFYpMLSq7QBO/ij4Wi++SlerneUB3aOAAP3CC4cI1oWkJF3B4pDohooQo9B+7QAKFa0QFkaTIev9+x2oUYe9L/jbSanGDNzKjQE62FgN66yIlNOiO9j1off8anpjmq8kDGAJRR6OFOepFjJLUl2l/G3VWXgRK2KfG/5sdEJKOyk/R9LYLr64ER9bIsBScvaGykgOrCwe6sBU4AFZ9iyQDdSYtZ79aw1788aMzWn9m4O6qGHodzAORiQ3uDD1IR7bq4lMCCY4UyHH21jvl3VrsvR8rjL0vPJN0M3Giz4OubacpZirea/GMFyjOWu7IqUi4OUwwJ1UcxWtNZKZb+stg7i3kycnxpjWg1uE7cXTDXbuTqKDzuPzRDJNUgeWlVKt45/VEIkUCmjxrOzmFGxUbq79p3DbA+pIFbsS/ePpbpWq9TYEuV9lP0DMW7EzkDg/oar3s/pHrtTbkuBIfpD4UqwAT6LezWxOQef//EBba64PI5CP97KkcrTkTu7zAXTq9faNyD9y9VA/Bteh/avwdMTYhFiRxAVGc8tSk6MBatII4mEEtdhxEIvFXL9PmEnK0InHp4tkLGLZBq4/elq+X46So4sFuYG9hz5b/NgJt2sEsKLGRDWXSjklcapxW9RjUKHxuUvM=";
        } else {
            value = textureData.get("value").toString();
            signature = textureData.get("signature").toString();
        }


//            JSONParser parser = new JSONParser();
//            JSONObject uuidResponse = (JSONObject) parser.parse(UtilWeb.getResponse("https://api.mojang.com/users/profiles/minecraft/" + targetSkin));
//            String uuid = uuidResponse.get("id").toString();
//            JSONObject textureData = (JSONObject) ((JSONObject) ((JSONObject) parser.parse(UtilWeb.getResponse("https://api.mineskin.org/generate/user/" + uuid))).get("data")).get("texture");
//            value = textureData.get("value").toString();
//            signature = textureData.get("signature").toString();

        if (value == null || signature == null)
            return false;

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl == p) continue;
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)p).getHandle()));
            GameProfile gp = ((CraftPlayer) p).getProfile();
            gp.getProperties().removeAll("textures");
            gp.getProperties().put("textures", new Property("textures", value, signature));
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)p).getHandle()));
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(p.getEntityId()));
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));
        }

        EnumWrappers.NativeGameMode gamemode = EnumWrappers.NativeGameMode.fromBukkit(p.getGameMode());
        EnumWrappers.Difficulty difficulty = EnumWrappers.getDifficultyConverter().getSpecific(p.getWorld().getDifficulty());

        WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(p);

        PacketContainer removeInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        removeInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        WrappedChatComponent displayName = WrappedChatComponent.fromText(p.getPlayerListName());
        PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, 0, gamemode, displayName);
        removeInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(playerInfoData));

        PacketContainer respawn = new PacketContainer(PacketType.Play.Server.RESPAWN);
        respawn.getIntegers().write(0, p.getWorld().getEnvironment().getId());
        respawn.getDifficulties().write(0, difficulty);
        respawn.getGameModes().write(0, gamemode);
        respawn.getWorldTypeModifier().write(0, p.getWorld().getWorldType());

        PacketContainer addInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        addInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        addInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(playerInfoData));

        Location location = p.getLocation().clone();

        PacketContainer teleport = new PacketContainer(PacketType.Play.Server.POSITION);
        teleport.getModifier().writeDefaults();
        teleport.getDoubles().write(0, location.getX());
        teleport.getDoubles().write(1, location.getY());
        teleport.getDoubles().write(2, location.getZ());
        teleport.getFloat().write(0, location.getYaw());
        teleport.getFloat().write(1, location.getPitch());
        teleport.getIntegers().writeSafely(0, -1337);

        PacketContainer health = new PacketContainer(PacketType.Play.Server.UPDATE_HEALTH);
        health.getFloat().write(0, (float) p.getHealth());
        health.getFloat().write(1, p.getSaturation());
        health.getIntegers().write(0, p.getFoodLevel());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, removeInfo);
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, addInfo);
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, respawn);
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, teleport);
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, health);
        } catch (InvocationTargetException e) {
            return false;
        }
        return requestedSkin;
    }

    private Method getFillMethod(Object sessionService) {
        for(Method m : sessionService.getClass().getDeclaredMethods())
        {
            if(m.getName().equals("fillProfileProperties"))
            {
                return m;
            }
        }
        throw new IllegalStateException("No fillProfileProperties method found in the session service :o");
    }

    private Object getSessionService() {
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

}
