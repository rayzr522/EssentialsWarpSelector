package me.rayzr522.essentialswarpselector.command;

import me.rayzr522.essentialswarpselector.EssentialsWarpSelector;
import me.rayzr522.essentialswarpselector.struct.Warp;
import me.rayzr522.scoreboardmenu.ScoreboardMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandEZWarp implements CommandExecutor {
    private final EssentialsWarpSelector plugin;

    public CommandEZWarp(EssentialsWarpSelector plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.checkPermission(sender, "use", true)) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.fail-only-players"));
            return true;
        }

        Player player = (Player) sender;
        List<Warp> warps = plugin.getWarps(player);

        if (warps.size() < 1) {
            player.sendMessage(plugin.tr("command.ezwarp.no-warps"));
            return true;
        }

        new ScoreboardMenu<Warp>()
                .addOption(null)
                .addAll(warps.toArray(new Warp[0]))
                .setRenderTransformer(warp -> warp != null ? warp.getName() : plugin.tr(false, "menu.cancel"))
                .setTitle(plugin.tr(false, "menu.title"))
                .setSelectedPrefix(plugin.tr(false, "menu.prefix.selected"))
                .setOtherPrefix(plugin.tr(false, "menu.prefix.other"))
                .setCallback(warp -> {
                    if (warp == null) {
                        player.sendMessage(plugin.tr("command.ezwarp.cancelled"));
                    } else {
                        player.teleport(warp.getLocation());
                        player.sendMessage(plugin.tr("command.ezwarp.warping", warp.getName()));
                    }
                })
                .openFor(player);

        player.sendMessage(plugin.tr("command.ezwarp.opening"));

        return true;
    }
}
