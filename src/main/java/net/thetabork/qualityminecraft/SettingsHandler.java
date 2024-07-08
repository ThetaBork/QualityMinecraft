package net.thetabork.qualityminecraft;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsHandler {
    private final static SettingsHandler instance = new SettingsHandler();
    private File file;
    private YamlConfiguration config;

    private String joinServerMessage;


    private SettingsHandler() {

    }

    // singleton
    public static SettingsHandler getInstance() {
        return instance;
    }

    public void load() {
        boolean noFile = false;
        file = new File(QualityMinecraft.getInstance().getDataFolder(), "settings.yml");

        if (!file.exists()) {
            QualityMinecraft.getInstance().saveResource("settings.yml", false);
            createDefaultConfigWithComments();
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // defaults
//        if (noFile) {
//            set("join-server-message", "<red>Welcome to the Test Server<red>");
//        }

        joinServerMessage = config.getString("join-server-message");
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public String getJoinServerMessage() {
        return joinServerMessage;
    }

    public void setJoinServerMessage(String joinServerMessage) {
        set("join-server-message", joinServerMessage);
    }

    private void createDefaultConfigWithComments() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("# Settings for QualityMinecraft plugin\n");
            writer.write("# This message will be displayed to players when they join the server\n");
            writer.write("# Use MiniMessage syntax to customize the color and style of your join message.\n");
            writer.write("# Learn more about MiniMessage here https://docs.advntr.dev/minimessage/format.html\n");
            writer.write("# Practice in the web preview https://webui.advntr.dev/\n");
            writer.write("join-server-message: '<red>Welcome to the Test Server<red>'\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}