package net.thetabork.qualityminecraft.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * A class that handles the fly command
 */
public class FlyCommand implements CommandExecutor {

    private ArrayList<Player> currentlyFlying = new ArrayList<>();

    /**
     * This method handles the fly command. This command enables allowFlight for the caller.
     * @param commandSender where the command came from
     * @param command idk
     * @param s idk
     * @param strings arguments, there should be no arguments since they are ignored
     * @return returns true and sets the player's allow flight to true or false
     * depending on the currently flying list.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("admin") && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!currentlyFlying.contains(player)) {
                player.setAllowFlight(true);
                currentlyFlying.add(player);
                player.sendMessage(Component.text("Flight Enabled").color(NamedTextColor.GREEN));
            } else {
                player.setAllowFlight(false);
                currentlyFlying.remove(player);
                player.sendMessage(Component.text("Flight Disabled").color(NamedTextColor.RED));}
        }

        return true;
    }
}