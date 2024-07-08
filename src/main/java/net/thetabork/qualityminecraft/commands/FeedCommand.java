package net.thetabork.qualityminecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A class that handles the feed command.
 */
public class FeedCommand implements CommandExecutor {
    /**
     * A method that handles the feed command. This command sets the player's hunger and saturation to full.
     * @param commandSender where the command was sent from.
     * @param command idk
     * @param s idk
     * @param strings the args (args are ignored for the time being).
     * @return true and fills hunger and saturation.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("admin") && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.setFoodLevel(20);
            player.setSaturation(20);
        }
        return true;
    }
}