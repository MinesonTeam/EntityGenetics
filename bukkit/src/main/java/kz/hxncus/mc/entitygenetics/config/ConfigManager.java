package kz.hxncus.mc.entitygenetics.config;

import kz.hxncus.mc.entitygenetics.EntityGenetics;
import lombok.EqualsAndHashCode;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@EqualsAndHashCode
public class ConfigManager {
    private final EntityGenetics plugin;
    private final YamlConfiguration defaultSettings;
    private final YamlConfiguration defaultLanguage;
    private YamlConfiguration settings;
    private YamlConfiguration languages;

    public ConfigManager(EntityGenetics plugin) {
        this.plugin = plugin;
        this.defaultSettings = extractDefault("settings.yml");
        this.defaultLanguage = extractDefault("languages/" + Settings.LANGUAGE + ".yml");
        this.validateConfigs();
    }

    public YamlConfiguration getSettings() {
        return settings == null ? defaultSettings : settings;
    }

    public File getSettingsFile() {
        return new File(plugin.getDataFolder(), "settings.yml");
    }

    public YamlConfiguration getLanguages() {
        return languages == null ? defaultLanguage : languages;
    }

    public File getLanguagesFile() {
        return new File(plugin.getDataFolder(), "language.yml");
    }

    public File getDataFile() {
        return new File(plugin.getDataFolder(), "data.yml");
    }
    private YamlConfiguration extractDefault(String source) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(plugin.getResource(source))) {
            return YamlConfiguration.loadConfiguration(inputStreamReader);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to extract default file: " + source);
            if (Settings.DEBUG.toBool()) {
                e.printStackTrace();
            }
            throw new RuntimeException();
        }
    }

    public void validateConfigs() {
        settings = validate("settings.yml", defaultSettings);
        languages = validate(plugin.getDataFolder().toPath().resolve("settings.yml").toString(), defaultLanguage);
    }

    private YamlConfiguration validate(String configName, YamlConfiguration defaultConfiguration) {
        File file = extractConfiguration(configName);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        boolean updated = false;
        for (String key : defaultConfiguration.getKeys(true)) {
            if (configuration.get(key) == null) {
                updated = true;
                plugin.getServer().getConsoleSender().sendMessage(getLanguages().getString(Messages.UPDATING_CONFIG_KEY.toPath(), "Languages file not found"));
                configuration.set(key, defaultConfiguration.get(key));
            }
        }
        for (String key : configuration.getKeys(false)) {
            if (defaultConfiguration.get(key) == null) {
                updated = true;
                plugin.getServer().getConsoleSender().sendMessage(getLanguages().getString(Messages.REMOVING_CONFIG_KEY.toPath(), "Languages file not found"));
                configuration.set(key, null);
            }
        }
        if (updated) {
            try {
                configuration.save(file);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to save updated configuration file: " + file.getName());
                if (Settings.DEBUG.toBool()) {
                    e.printStackTrace();
                }
            }
        }
        return configuration;
    }

    public File extractConfiguration(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            this.plugin.saveResource(fileName, false);
        }
        return file;
    }

    public void saveSettingsConfig() {
        try {
            getSettings().save(getSettingsFile());
        } catch (IOException e) {
            if (Settings.DEBUG.toBool()) {
                e.printStackTrace();
            }
        }
    }

    public void saveLanguageConfig() {
        try {
            getLanguages().save(getLanguagesFile());
        } catch (IOException e) {
            if (Settings.DEBUG.toBool()) {
                e.printStackTrace();
            }
        }
    }
}
