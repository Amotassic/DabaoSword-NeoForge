package com.amotassic.dabaosword.command;

import com.amotassic.dabaosword.DabaoSword;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static com.amotassic.dabaosword.util.ModTools.trinketItem;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@EventBusSubscriber(modid = DabaoSword.MODID)
public class TriggerSkillCommand {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent evt) {
        register(evt.getDispatcher(), evt.getBuildContext());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext access) {
        dispatcher.register(literal("dabaosword")
                .requires(source -> source.getEntity() != null)
                .then(argument("skill", ItemArgument.item(access))
                        .executes(c -> execute(c, ItemArgument.getItem(c, "skill"), 0))
                        .then(argument("value", IntegerArgumentType.integer())
                                .executes(c -> execute(c, ItemArgument.getItem(c, "skill"), IntegerArgumentType.getInteger(c, "value")))
                        )
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, ItemInput stack, int value) {
        LivingEntity entity = (LivingEntity) ctx.getSource().getEntity();
        ItemStack skill = trinketItem(stack.getItem(), entity);
        if (skill.getItem() instanceof CSkill s) s.triggerSkill(entity, skill, value);
        return 1;
    }

    public interface CSkill {
        default void triggerSkill(LivingEntity entity, ItemStack stack, int value) {}
    }
}
