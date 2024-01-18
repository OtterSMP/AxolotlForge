package dev.imlukas.axolotlforgeplugin.utils.storage;

import dev.imlukas.axolotlforgeplugin.AxolotlForgePlugin;
import dev.imlukas.axolotlforgeplugin.utils.text.Placeholder;
import dev.imlukas.axolotlforgeplugin.utils.text.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

public class Messages extends YMLBase {

    private final String prefix;
    protected boolean usePrefix;
    private String msg;

    public Messages(AxolotlForgePlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "messages.yml"), true);
        prefix = getConfiguration().getString("messages.prefix");
        usePrefix = getConfiguration().getBoolean("messages.use-prefix");
    }

    private String setMessage(String name, UnaryOperator<String> action) {
        if (!getConfiguration().contains("messages." + name)) {
            return "";
        }

        msg = getMessage(name).replaceAll("%prefix%", prefix);

        if (usePrefix) {
            msg = prefix + " " + getMessage(name);
        }

        msg = action.apply(msg);
        return TextUtils.color(msg);
    }

    public void sendMessage(CommandSender sender, String name) {
        sendMessage(sender, name, (s) -> s);
    }

    @SafeVarargs
    public final <T extends CommandSender> void sendMessage(T sender, String name, Placeholder<T>... placeholders) {
        sendMessage(sender, name, List.of(placeholders));
    }

    public final <T extends CommandSender> void sendMessage(T sender, String name, Collection<Placeholder<T>> placeholders) {
        sendMessage(sender, name, text -> {
            for (Placeholder<T> placeholder : placeholders) {
                text = placeholder.replace(text, sender);
            }

            return text;
        });
    }


    public void sendMessage(CommandSender sender, String name, UnaryOperator<String> action) {
        if (getConfiguration().isList("messages." + name)) {
            for (String message : getConfiguration().getStringList("messages." + name)) {
                msg = message.replace("%prefix%", prefix);
                msg = TextUtils.color(action.apply(msg));
                sender.sendMessage(msg);
            }
            return;
        }

        msg = setMessage(name, action);


        if (getConfiguration().contains(name + "-actionbar") && sender instanceof Player player) {
            player.sendActionBar(TextUtils.toComponent(setMessage(name + "-actionbar", action)));
        } else {
            sender.sendMessage(msg);
        }
    }

    @SafeVarargs
    public final <T extends CommandSender> void sendActionbar(T sender, String name, Placeholder<T>... placeholders) {
        sendActionbar(sender, name, List.of(placeholders));
    }

    public final <T extends CommandSender> void sendActionbar(T sender, String name, Collection<Placeholder<T>> placeholders) {
        sendActionbar(sender, name, (text) -> {
            for (Placeholder<T> placeholder : placeholders) {
                text = placeholder.replace(text, sender);
            }

            return text;
        });
    }

    public void sendActionbar(CommandSender sender, String name, UnaryOperator<String> action) {
        msg = setMessage(name, action);
        if (sender instanceof Player player) {
            player.sendActionBar(TextUtils.toComponent(msg));
        }
    }

    public String getMessage(String name) {
        return getConfiguration().getString("messages." + name);
    }

    public String getMessage(String name, Placeholder<CommandSender>... placeholders) {
        String message = getMessage(name);

        if (message == null) {
            return null;
        }

        for (Placeholder<CommandSender> placeholder : placeholders) {
            message = placeholder.replace(message, null);
        }

        return message;
    }
}

