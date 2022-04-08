package net.darmo_creations.tloz_mod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Collection;

/**
 * Command that sets the maximum health of the targetted players.
 */
public class SetMaxHealthCommand {
  /**
   * Register this command.
   */
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("maxhealth")
        .then(Commands.argument("targets", EntityArgument.players())
            .then(Commands.argument("hearts", IntegerArgumentType.integer(1, 512))
                .executes(SetMaxHealthCommand::setMaxHealth))));
  }

  private static int setMaxHealth(CommandContext<CommandSource> context) throws CommandSyntaxException {
    int amount = IntegerArgumentType.getInteger(context, "hearts");
    Collection<ServerPlayerEntity> targets = EntityArgument.getPlayers(context, "targets");
    for (ServerPlayerEntity player : targets) {
      //noinspection ConstantConditions
      player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(amount * 2);
    }
    return targets.size();
  }
}
