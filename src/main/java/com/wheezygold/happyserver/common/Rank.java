package com.wheezygold.happyserver.common;

import com.wheezygold.happyserver.util.Color;
import com.wheezygold.happyserver.util.D;
import org.bukkit.ChatColor;

@SuppressWarnings("unused")
public enum Rank {

    OWNER("Owner", "&e&lOwner", "264560287183667202", 100),
    DEV("Developer", "&d&lDeveloper", "317386352763207702", 90),
    MANAGER("Channel Manager", "&4&lManager", "368172637182230549", 35),
    SADMIN("Super Admin", "&1&lS.Admin", "317386352763207702", 30),
    ADMIN("Admin", "&9&lAdmin", "295673671203291147", 25),
    MOD("Moderator", "&2&lMod", "264964563517046784", 20),
    HELPER("Helper", "&b&lHelper", "264965005949009920", 15),
    PATRON("Patron Boys", "&6&lPatron", "300081997878132736", 10),
    OG("OG", "&c&lOG", "350986983004307456", 5),
    NONE("No Rank", "", "264964418796781568", 0);

    private String name;
    private String prefix;
    private String discordId;
    private int power;

    Rank(String name, String prefix, String discordId, int power) {
        this.name = name;
        this.prefix = prefix;
        this.discordId = discordId;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public int getPower() {
        return power;
    }

    public String getDiscordId() {
        return discordId;
    }

    public String getColor() {
        return getPrefix().substring(0, 2);
    }

    public static Rank fromString(String s) {
        for (Rank list : values()) {
            if (s.equalsIgnoreCase(list.name())) {
                return list;
            }
        }
        return null;
    }

    public static String perm(Rank rank) {
        return D.M("Permissions", "This requires Permission Rank [" + Color.cBlue + rank.getName().toUpperCase() + Color.cGray + "].");
    }
}
