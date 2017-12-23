package com.wheezygold.happyserver.common;

import org.bukkit.ChatColor;

public enum SubRank {

    ADDICT("Gambling Addict", "&dA", "384503288621694976", 4),
    OBSESSIVE("Obsessive", "&6O", "296259032400920578", 3),
    TRYHARD("Tryhard", "&aT", "296257773887553538", 2),
    REGULAR("Regular", "&2R", "277933832240496650", 1),
    NONE("None", "", "", 0);

    private String name;
    private String prefix;
    private String discordId;
    private int power;

    SubRank(String name, String prefix, String discordId, int power) {
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

    public static SubRank fromString(String s) {
        for (SubRank list : values()) {
            if (s.equalsIgnoreCase(list.name())) {
                return list;
            }
        }
        return null;
    }

}
