package net.thetabork.qualityminecraft.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A class that toggles invulnerability.
 */
public class GodCommand implements CommandExecutor {

    /**
     *
     * @param commandSender the person who sent the command
     * @param command not sure tbh
     * @param s not sure tbh
     * @param strings the arguments sent with the command
     * @return return true if a player has called the command. False otherwise
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("admin") && commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (!p.isInvulnerable()) {
                p.setInvulnerable(true);
                p.sendMessage(Component.text("God Enabled").color(NamedTextColor.GREEN));
            } else {
                p.setInvulnerable(false);
                p.sendMessage(Component.text("God Disabled").color(NamedTextColor.RED));
            }

            return true;
        }

        commandSender.sendMessage(Component.text("This command can only be run by a player.")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true));
        return false;
    }
}