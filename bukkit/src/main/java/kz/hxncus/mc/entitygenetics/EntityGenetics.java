package kz.hxncus.mc.entitygenetics;

import kz.hxncus.mc.minesonapi.MinesonAPI;
import kz.hxncus.mc.minesonapi.bukkit.event.EventManager;
import kz.hxncus.mc.minesonapi.random.SimpleRandom;
import kz.hxncus.mc.minesonapi.util.AttributeUtil;
import lombok.Getter;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class EntityGenetics extends JavaPlugin {
    private static EntityGenetics instance;
    
    public EntityGenetics() {
        instance = this;
    }
    
    public static EntityGenetics get() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        registerEvents();
    }

    private void registerEvents() {
        EventManager eventManager = MinesonAPI.get().getEventManager();
        eventManager.register(EntitySpawnEvent.class, event -> {
            if (!(event.getEntity() instanceof LivingEntity livingEntity)) {
                return;
            }
            double randomValue = SimpleRandom.get().nextDouble(-0.5, 0.5);
            AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_SCALE), scaleAttribute -> {
                double multiplier = scaleAttribute.getBaseValue() + randomValue;
                scaleAttribute.setBaseValue(multiplier);
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_ATTACK_DAMAGE));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_SPEED),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_ATTACK_SPEED));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_FALL_DAMAGE_MULTIPLIER));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_JUMP_STRENGTH),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_JUMP_STRENGTH));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_MAX_HEALTH));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_MOVEMENT_SPEED));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_SAFE_FALL_DISTANCE));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.GENERIC_STEP_HEIGHT),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_STEP_HEIGHT));
                AttributeUtil.acceptIfExists(livingEntity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.ZOMBIE_SPAWN_REINFORCEMENTS));
            });
        });
        eventManager.register(PlayerJoinEvent.class, event -> {
            Player player = event.getPlayer();
            if (player.hasPlayedBefore()) {
                return;
            }
            double randomValue = SimpleRandom.get().nextDouble(-0.5, 0.5);
            AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_SCALE), scaleAttribute -> {
                double multiplier = scaleAttribute.getBaseValue() + randomValue;
                scaleAttribute.setBaseValue(multiplier);
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_ATTACK_DAMAGE));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_ATTACK_SPEED));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_FALL_DAMAGE_MULTIPLIER));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_JUMP_STRENGTH));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_MAX_HEALTH),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_MAX_HEALTH));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_MOVEMENT_SPEED));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_SAFE_FALL_DISTANCE));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.GENERIC_STEP_HEIGHT),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.GENERIC_STEP_HEIGHT));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.PLAYER_BLOCK_BREAK_SPEED));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.PLAYER_BLOCK_INTERACTION_RANGE));
                AttributeUtil.acceptIfExists(player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE),
                    maxHealthAttribute -> maxHealthAttribute.setBaseValue(multiplier * AttributeUtil.PLAYER_ENTITY_INTERACTION_RANGE));
            });
        });
    }
}
