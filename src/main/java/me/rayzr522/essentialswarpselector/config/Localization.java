package me.rayzr522.essentialswarpselector.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A simple Localization system. Taken from DecoHeads.
 *
 * @author Rayzr522
 */
public class Localization {

    /**
     * Matches any valid YAML path inside of double square brackets.
     * Example: [[some.path.here]]
     */
    private static final Pattern VAR_SUBSTITUTION = Pattern.compile("\\[\\[([a-z_.-]+)]]");

    private Map<String, String> messages;

    public Localization(ConfigurationSection config) {
        // First run: load all messages
        Map<String, String> raw = config.getKeys(true).stream()
                .filter(config::isString)
                .collect(Collectors.toMap(key -> key, config::getString));

        // Second run: parse them for special things
        messages = raw.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> parse(raw, entry)));
    }

    private static String basename(String key) {
        return !key.contains(".") ? "" : key.substring(0, key.lastIndexOf("."));
    }

    private String parse(Map<String, String> raw, Entry<String, String> entry) {
        // 1. Color the text
        String output = colorize(entry.getValue());
        Matcher matcher;

        // 2. Variable substitution
        matcher = VAR_SUBSTITUTION.matcher(output);
        while (matcher.find()) {
            output = output.replaceAll(matcher.group(), raw.getOrDefault(matcher.group(1), matcher.group(1)));
        }

        return output;
    }

    private String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * @param key     the key of the message
     * @param strings the strings to use for substitution
     * @return The message, or the key itself if no message was found for that
     * key
     */
    public String tr(String key, Object... strings) {
        return tr(true, key, strings);
    }

    /**
     * @param usePrefix Whether or not to prepend the prefix to the message
     * @param key       the key of the message
     * @param strings   the strings to use for substitution
     * @return The message, or the key itself if no message was found for that
     * key
     */
    public String tr(boolean usePrefix, String key, Object... strings) {
        if (!messages.containsKey(key)) {
            return key;
        }
        String message = messages.get(key);
        for (int i = 0; i < strings.length; i++) {
            message = message.replace("{" + i + "}", Objects.toString(strings[i]));
        }
        return (usePrefix ? resolvePrefix(key) : "") + colorize(message);
    }

    private String resolvePrefix(String key) {
        String parent = basename(key);
        String prefix = messages.getOrDefault(parent + ".prefix", messages.get("prefix"));
        String prefixAddon = messages.getOrDefault(parent + ".prefix-addon", "");
        return colorize(prefix + prefixAddon);
    }

}
