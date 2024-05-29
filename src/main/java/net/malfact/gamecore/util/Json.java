package net.malfact.gamecore.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Json {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .registerTypeAdapter(Location.class, new LocationTypeAdapter())
        .create();

    public static void write(Plugin plugin, @NotNull String fileName, @NotNull Object object) {
        Path path = Paths.get(plugin.getDataFolder() + "/" + fileName + ".json");

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            plugin.getComponentLogger().error(Component.text(e.getMessage()));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            GSON.toJson(object, writer);
        } catch (IOException e) {
            plugin.getComponentLogger().error(Component.text(e.getMessage()));
        }
    }

    public static <T> T read(Plugin plugin, @NotNull String fileName, @NotNull Type type) {
        Path path = Paths.get(plugin.getDataFolder() + "/" + fileName + ".json");

        if (!Files.exists(path))
            return null;

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonReader jsonReader = new JsonReader(reader);
            return GSON.fromJson(jsonReader, type);
        } catch (IOException e) {
            plugin.getComponentLogger().error(Component.text(e.getMessage()));
            return null;
        }
    }

    public static void delete(Plugin plugin, @NotNull String fileName) {
        Path path = Paths.get(plugin.getDataFolder() + "/" + fileName + ".json");

        if (!Files.exists(path) || Files.isDirectory(path))
            return;

        try {
            Files.delete(path);
        } catch (IOException e) {
            plugin.getComponentLogger().error(Component.text(e.getMessage()));
        }
    }
}
