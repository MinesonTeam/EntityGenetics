package kz.hxncus.mc.entitygenetics.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.SplittableRandom;

public class EntityListener implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        SplittableRandom random = new SplittableRandom();
        double randomValue = random.nextDouble(-0.5, 0.5);
        AttributeInstance attribute = livingEntity.getAttribute(Attribute.GENERIC_SCALE);
        if (attribute != null) {
            attribute.setBaseValue(attribute.getBaseValue() + randomValue);
        }
    }
}
