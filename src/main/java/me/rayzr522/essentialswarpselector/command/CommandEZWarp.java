package me.rayzr522.essentialswarpselector.command;

import me.rayzr522.essentialswarpselector.EssentialsWarpSelector;
import me.rayzr522.essentialswarpselector.struct.Warp;
import me.rayzr522.scoreboardmenu.ScoreboardMenu;
import org.bukkit.ChatColor;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this!");
            return true;
        }

        Player player = (Player) sender;
        List<Warp> warps = plugin.getWarps(player);

        if (warps.size() < 1) {
            player.sendMessage(ChatColor.RED + "No warps available.");
            return true;
        }

        new ScoreboardMenu<Warp>()
                .addAll(warps.toArray(new Warp[0]))
                .setRenderTransformer(Warp::getName)
                .setTitle(ChatColor.GOLD + "Choose Warp")
                .setSelectedPrefix(ChatColor.YELLOW.toString())
                .setOtherPrefix(ChatColor.RED.toString())
                .setCallback(warp -> player.teleport(warp.getLocation()))
                .openFor(player);

        player.sendMessage(ChatColor.GOLD + "EZWarp " + ChatColor.DARK_GRAY + "\u00bb" + ChatColor.YELLOW + " Opened warp selector.");

        return true;
    }
}
