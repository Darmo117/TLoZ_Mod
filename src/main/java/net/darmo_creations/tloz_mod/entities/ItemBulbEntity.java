package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.blocks.ItemBulbFlowerBlock;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.ItemBulbFlowerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * Entity for the item bulb flower block.
 *
 * @see ItemBulbFlowerBlock
 * @see ItemBulbFlowerTileEntity
 */
public class ItemBulbEntity extends BreakablePickableEntity {
  public ItemBulbEntity(EntityType<? extends ItemBulbEntity> type, World world) {
    super(type, world);
  }

  public ItemBulbEntity(World world, double x, double y, double z, boolean breakInstantly, PlayerEntity picker) {
    super(ModEntities.ITEM_BULB.get(), world, x, y, z, breakInstantly, picker);
  }

  @Override
  protected float getCollisionDamageAmount(Entity entity) {
    return 2; // TODO find out actual amount per entity
  }

  @Override
  protected void playBreakSoundAndAnimation() {
    this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS,
        1, (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F * 0.7F);
    Minecraft.getInstance().particles.addBlockDestroyEffects(this.getPosition(), ModBlocks.ITEM_BULB.getDefaultState());
  }

  @Override
  protected List<ItemStack> getDrops() {
    // TODO randomize loot depending on player’s available weapons and ammo amount
    return Collections.singletonList(new ItemStack(Items.ARROW));
  }
}
