package net.thetabork.qualityminecraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

/**
 * A class that heals players after they sleep.
 */
public class BedLeaveListener implements Listener {
    /**
     * Event handler that is triggered when a player leaves a bed.
     * This method sets the player's health to full (20 health points).
     *
     * @param e the PlayerBedLeaveEvent triggered when a player leaves a bed.
     */
    @EventHandler
    public void onLeaveBed(PlayerBedLeaveEvent e) {
        e.getPlayer().setHealth(20f);
    }
}