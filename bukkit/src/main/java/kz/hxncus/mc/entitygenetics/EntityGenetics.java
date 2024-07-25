package kz.hxncus.mc.entitygenetics;

import kz.hxncus.mc.entitygenetics.color.ColorManager;
import kz.hxncus.mc.entitygenetics.config.ConfigManager;
import kz.hxncus.mc.entitygenetics.listener.EntityListener;
import kz.hxncus.mc.entitygenetics.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class EntityGenetics extends JavaPlugin {
    private static EntityGenetics instance;
    private ColorManager colorManager;
    private ConfigManager configManager;
    
    public EntityGenetics() {
        instance = this;
    }
    
    public static EntityGenetics get() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        registerManagers();
        registerListeners(getServer().getPluginManager());
    }

    private void registerManagers() {
        this.colorManager = new ColorManager(this);
        this.configManager = new ConfigManager(this);
    }

    private void registerListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(new EntityListener(), this);
        pluginManager.registerEvents(new PlayerListener(), this);
    }
}
