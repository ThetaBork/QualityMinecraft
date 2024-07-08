package net.thetabork.qualityminecraft;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.thetabork.qualityminecraft.commands.FeedCommand;
import net.thetabork.qualityminecraft.commands.FlyCommand;
import net.thetabork.qualityminecraft.commands.GodCommand;
import net.thetabork.qualityminecraft.listeners.BedLeaveListener;
import net.thetabork.qualityminecraft.listeners.BreedingListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the Quality Minecraft Plugin.
 * This plugin is focused on quality of life changes for small to medium size minecraft servers.
 */
public final class QualityMinecraft extends JavaPlugin implements Listener {
    public static QualityMinecraft getInstance() {
        return getPlugin(QualityMinecraft.class);
    }

    private BreedingListener breedingListener;

    @Override
    public void onEnable() {
        // configs
        SettingsHandler.getInstance().load();

        // listeners
        breedingListener = new BreedingListener(this);


        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(breedingListener, this);
        getServer().getPluginManager().registerEvents(new BedLeaveListener(), this);

        // commands
        getCommand("god").setExecutor(new GodCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("fly").setExecutor(new FlyCommand());
    }

    @Override
    public void onDisable() {
        breedingListener.destroyArmorStands();
    }


    /**
     * This method welcomes the player to the server
     * @param playerJoinEvent an event triggered when a player joins the server.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        playerJoinEvent.joinMessage(MiniMessage.miniMessage().deserialize(SettingsHandler.getInstance().getJoinServerMessage()));
        playerJoinEvent.getPlayer().setInvulnerable(false);
    }
}