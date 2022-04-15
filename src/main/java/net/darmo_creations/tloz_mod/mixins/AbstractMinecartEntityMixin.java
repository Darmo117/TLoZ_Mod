package net.darmo_creations.tloz_mod.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@SuppressWarnings("UnusedMixin") // Intellij does not detect JSON file
@Mixin(value = AbstractMinecartEntity.class, priority = 500)
public abstract class AbstractMinecartEntityMixin extends Entity {
  public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
    super(entityType, world);
  }

  /**
   * @author Darmo
   * @reason Prevent minecarts from being pushed.
   */
  @Override
  @Overwrite
  public boolean canBePushed() {
    return false;
  }
}
