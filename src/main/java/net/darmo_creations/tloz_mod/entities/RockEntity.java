package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.blocks.RockBlock;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.darmo_creations.tloz_mod.tile_entities.RockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * Entity for the {@link RockBlock}.
 *
 * @see RockBlock
 * @see RockTileEntity
 */
public class RockEntity extends BreakablePickableEntity {
  public RockEntity(EntityType<? extends RockEntity> type, World world) {
    super(type, world);
  }

  public RockEntity(World world, double x, double y, double z, boolean breakInstantly, PlayerEntity picker) {
    super(ModEntities.ROCK.get(), world, x, y, z, breakInstantly, picker);
  }

  @Override
  protected float getCollisionDamageAmount(Entity entity) {
    return 2; // TODO find out actual amount per entity
  }

  @Override
  protected void playBreakSoundAndAnimation() {
    this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_ANCIENT_DEBRIS_BREAK, SoundCategory.BLOCKS,
        1, (1 + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
    Minecraft.getInstance().particles.addBlockDestroyEffects(this.getPosition(), ModBlocks.ROCK.getDefaultState());
  }

  @Override
  protected List<ItemStack> getDrops() {
    // TODO randomize loot depending on player’s available weapons and ammo amount
    return Collections.singletonList(new ItemStack(ModItems.HEART));
  }
}
