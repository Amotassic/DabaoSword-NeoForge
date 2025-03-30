package com.amotassic.dabaosword.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static com.amotassic.dabaosword.util.ModTools.openFullInv;

public class InfoCommand {
    public static void registerCommands(RegisterCommandsEvent evt) {
        register(evt.getDispatcher());
        DabaoSwordCommand.register(evt.getDispatcher(), evt.getBuildContext());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("information")
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes(c -> run(EntityArgument.getEntity(c, "target"), c, false))
                        .then(Commands.argument("editable", BoolArgumentType.bool())
                                .requires(source -> source.hasPermission(2))
                                .executes(c -> run(EntityArgument.getEntity(c, "target"), c, BoolArgumentType.getBool(c, "editable")))
                        )
                )
        );
    }

    private static int run(Entity entity, CommandContext<CommandSourceStack> context, boolean editable) {
        var player = context.getSource().getPlayer();
        if (player != null) {
            if (entity instanceof LivingEntity target) openFullInv(player, target, editable);
            else player.displayClientMessage(Component.translatable("info.fail").withStyle(ChatFormatting.RED), false);
        }
        return 1;
    }
}
