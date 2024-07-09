package net.malfact.gamecore.util;

import org.bukkit.Bukkit;

public final class CraftClass {

    private CraftClass() {}

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    public static Class<?> forName(String name) {
        try {
            return Class.forName(CRAFTBUKKIT_PACKAGE + "." + name);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }
}
