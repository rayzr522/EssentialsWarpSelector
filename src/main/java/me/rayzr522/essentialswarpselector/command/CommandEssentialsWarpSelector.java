package me.rayzr522.essentialswarpselector.command;

import me.rayzr522.essentialswarpselector.EssentialsWarpSelector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandEssentialsWarpSelector implements CommandExecutor {
    private EssentialsWarpSelector plugin;

    public CommandEssentialsWarpSelector(EssentialsWarpSelector plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.checkPermission(sender, "admin", true)) {
            return true;
        }

        if (args.length < 1) {
            showUsage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "version":
                sender.sendMessage(plugin.tr("command.essentialswarpselector.version", plugin.getDescription().getVersion()));
                break;
            case "reload":
                plugin.reload();
                sender.sendMessage(plugin.tr("command.essentialswarpselector.reloaded"));
                break;
            default:
                showUsage(sender);
        }

        return true;
    }

    private void showUsage(CommandSender sender) {
        sender.sendMessage(plugin.tr(false, "command.essentialswarpselector.usage"));
    }
}
