package net.malfact.gamecore;

import com.google.common.base.Charsets;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Messages {

    /**
     * Gets and deserializes a Message from 'messages.yml'
     * @param key The message key to find
     * @param replacements The replacement values
     * @return the deserialized message
     */
    public static Component get(final String key, Object... replacements) {
        final String message = GameCore.getMessages().getMessagesConfig().getString(key, key);

        if (replacements != null && replacements.length > 0) {
            TagResolver[] tagResolvers = new TagResolver[replacements.length];
            for (int i = 0; i < replacements.length; i++) {
                if (replacements[i] == null)
                    continue;
                tagResolvers[i] = Placeholder.parsed("" + (i + 1), replacements[i].toString());
            }

            return MiniMessage.miniMessage().deserialize(message, tagResolvers);
        }

        return MiniMessage.miniMessage().deserialize(message);
    }

    private final Plugin plugin;
    private final File messagesFile;
    private FileConfiguration messages = null;

    Messages(Plugin plugin) {
        this.plugin = plugin;
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
    }

    public void saveDefaultMessages() {
        if (!messagesFile.exists())
            plugin.saveResource("messages.yml", false);
    }

    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        final InputStream defMessagesStream = plugin.getResource("messages.yml");
        if (defMessagesStream == null)
            return;

        messages.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defMessagesStream, Charsets.UTF_8)));
    }

    public void saveMessages() {
        try {
            getMessagesConfig().save(messagesFile);
        } catch (IOException exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not save Messages to " + messagesFile, exception);
        }
    }

    public FileConfiguration getMessagesConfig() {
        if (messages == null)
            reloadMessages();

        return messages;
    }
}
