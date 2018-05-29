package me.rayzr522.essentialswarpselector;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Warps;
import com.earth2me.essentials.commands.WarpNotFoundException;
import me.rayzr522.essentialswarpselector.command.CommandEZWarp;
import me.rayzr522.essentialswarpselector.command.CommandEssentialsWarpSelector;
import me.rayzr522.essentialswarpselector.config.Localization;
import me.rayzr522.essentialswarpselector.struct.Warp;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EssentialsWarpSelector extends JavaPlugin {
    private Essentials essentials;
    private Localization localization;

    @Override
    public void onEnable() {
        if (!loadEssentials()) {
            getLogger().severe("Failed to connect to Essentials! This plugin will now be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("ezwarp").setExecutor(new CommandEZWarp(this));
        getCommand("essentialswarpselector").setExecutor(new CommandEssentialsWarpSelector(this));
        reload();
    }

    private boolean loadEssentials() {
        Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
        if (ess instanceof Essentials) {
            essentials = (Essentials) ess;
        }
        return essentials != null;
    }

    public void reload() {
        saveDefaultConfig();
        localization = new Localization(getConfig().getConfigurationSection("messages"));
    }

    public List<Warp> getWarps(Player player) {
        Warps warps = essentials.getWarps();
        return warps.getList().stream()
                .filter(warp -> !essentials.getSettings().getPerWarpPermission() || player.hasPermission(String.format("essentials.warp.%s", warp)))
                .map(warp -> {
                    try {
                        return new Warp(warp, warps.getWarp(warp));
                    } catch (WarpNotFoundException | InvalidWorldException ignore) {
                        // well whoops :^)
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public String tr(String key, Object... strings) {
        return localization.tr(key, strings);
    }

    public String tr(boolean usePrefix, String key, Object... strings) {
        return localization.tr(usePrefix, key, strings);
    }

    public boolean checkPermission(CommandSender sender, String permission, boolean sendMessage) {
        String realPermission = String.format("%s.%s", getName(), permission);
        if (!sender.hasPermission(realPermission)) {
            if (sendMessage) {
                sender.sendMessage(tr("command.fail.no-permission", realPermission));
            }
            return false;
        }
        return true;
    }
}
