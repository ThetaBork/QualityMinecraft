package net.thetabork.qualityminecraft.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * A class that implements the logic for the countdown timers for both animal growth and animal breeding
 */
public class BreedingListener implements Listener {
    private double armorStandHeight = .3;
    private int breedingCooldown = 300;
    private NamedTextColor ageColor = NamedTextColor.YELLOW;

    private Map<UUID, Integer> breedingCooldowns = new HashMap<>();
    private Map<UUID, ArmorStand> armorStands = new HashMap<>();

    private List<Ageable> babies = new ArrayList<>();

    /**
     * Constructor
     * @param plugin should be passed the main plugin
     */
    public BreedingListener(JavaPlugin plugin) {
        new BreedingCooldownTask().runTaskTimer(plugin, 20L, 20L);  // Run every second
        new NameTagTask().runTaskTimer(plugin, 1L, 1L);
        new BabiesTask().runTaskTimer(plugin, 20L, 20L);
    }

    /**
     * An event handler called when an animal is breed.
     * @param event the breeding event
     */
    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        setBreedingCooldown(event.getFather().getUniqueId(), breedingCooldown);
        setBreedingCooldown(event.getMother().getUniqueId(), breedingCooldown);
        if (event.getEntity() instanceof Ageable) {
            Ageable child = (Ageable) event.getEntity();
            babies.add(child);
        }
    }

    /**
     * Maps an entity ID to its breeding cooldown.
     * @param entityId
     * @param cooldown
     */
    private void setBreedingCooldown(UUID entityId, int cooldown) {
        breedingCooldowns.put(entityId, cooldown);
    }

    /**
     * Returns an adventure component based on the cooldown number
     * @param cooldown the time left to breed in seconds
     * @return An adventure Component with the text "Cooldown %cooldownnumber%" with coloring.
     */
    private Component getCooldownMessage(int cooldown) {
        NamedTextColor countdownColor;
        if (cooldown <= breedingCooldown / 5) {
            countdownColor = NamedTextColor.GREEN;
        } else if (cooldown <= breedingCooldown / 2) {
            countdownColor = NamedTextColor.BLUE;
        } else {
            countdownColor = NamedTextColor.RED;
        }

        return Component.text("Cooldown ", NamedTextColor.GREEN).append(Component.text(cooldown, countdownColor));
    }

    /**
     * Gets an adventure Component based on the age of an entity
     * @param age the age of the entity
     * @return An adventure component in the format of "Baby %-age%"
     */
    private Component getAgeMessage(int age) {
        return Component.text("Baby ", NamedTextColor.GREEN).append(Component.text(age * -1, ageColor));
    }

    /**
     * Displays the current countdown or age of an entity
     * @param entity either a parent or a child
     * @param message the message to display
     */
    private void displayMessage(LivingEntity entity, Component message) {
        ArmorStand armorStand = armorStands.get(entity.getUniqueId());
        if (message != null) {
            if (armorStand == null || armorStand.isDead()) {
                armorStand = entity.getWorld().spawn(entity.getEyeLocation().add(0, armorStandHeight, 0), ArmorStand.class);
                armorStand.setVisible(false);
                armorStand.setCustomNameVisible(true);
                armorStand.setMarker(true);
                armorStand.setSmall(true);
                armorStand.setGravity(false);  // Add this to ensure it doesn't fall and affect positioning
                armorStand.setInvulnerable(true);  // To prevent accidental damage
                armorStands.put(entity.getUniqueId(), armorStand);
            }
            armorStand.customName(message);
        } else {
            if (armorStand != null) {
                armorStand.remove();
                armorStands.remove(entity.getUniqueId());
            }
        }
    }

    /**
     * Destroys the all armor stands for the counddowns. This should be called when the server shuts down.
     */
    public void destroyArmorStands() {
        for (ArmorStand armorStand : armorStands.values()) {
            armorStand.remove();
        }
    }

    /**
     * A class that updates breeding cooldowns
     */
    private class BreedingCooldownTask extends BukkitRunnable {
        @Override
        public void run() {
            Iterator<Map.Entry<UUID, Integer>> iterator = breedingCooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, Integer> entry = iterator.next();
                UUID entityId = entry.getKey();
                int cooldown = entry.getValue();
                LivingEntity entity = (LivingEntity) Bukkit.getEntity(entityId);
                if (entity != null) {
                    displayMessage(entity, getCooldownMessage(cooldown));
                }

                if (cooldown <= 0) {
                    ArmorStand a = armorStands.get(entityId);
                    if (a != null) {
                        a.remove();
                        armorStands.remove(entityId);
                    }
                    iterator.remove();
                } else {
                    breedingCooldowns.put(entityId, cooldown - 1);
                }
            }
        }
    }

    /**
     * A class that updates the positions of all the tags that display cooldowns
     */
    private class NameTagTask extends BukkitRunnable {
        @Override
        public void run() {
            Iterator<Map.Entry<UUID, ArmorStand>> iterator = armorStands.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, ArmorStand> entry = iterator.next();
                UUID entityId = entry.getKey();
                ArmorStand armorStand = entry.getValue();
                try {
                    LivingEntity entity = (LivingEntity) Bukkit.getEntity(entityId);
                    if (entity != null) {
                        armorStand.teleport(entity.getEyeLocation().add(0, armorStandHeight, 0));
                    } else {
                        armorStand.remove();
                        iterator.remove();
                    }
                } catch (Exception e) {
                    armorStand.remove();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * A class that updates the age of baby mobs.
     */
    private class BabiesTask extends BukkitRunnable {
        @Override
        public void run() {
            Iterator<Ageable> iterator = babies.iterator();
            while (iterator.hasNext()) {
                Ageable baby = iterator.next();
                if (baby.getAge() >= 0 || baby.isDead()) {
                    ArmorStand a = armorStands.remove(baby.getUniqueId());
                    // null check fixes warning
                    if (a != null) {
                        a.remove();
                    }
                    iterator.remove();
                    continue;
                }
                displayMessage(baby, getAgeMessage(baby.getAge()));
            }
        }
    }
}