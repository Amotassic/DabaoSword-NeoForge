package com.amotassic.dabaosword.command;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = DabaoSword.MODID)
public class InfoCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent evt) {
        register(evt.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("info")
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

    public static void openFullInv(Player player, LivingEntity target, boolean editable) {
        if (!player.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider(((i, inv, player1) -> new FullInvScreenHandler(i, inv, fullInv(target, editable), target)), target.getName()), (buf -> buf.writeInt(target.getId())));
        }
    }

    public static Container fullInv(LivingEntity target, boolean ebitable) {
        SimpleContainer inv = new SimpleContainer(64);
        return updateInv(inv, target, ebitable);
    }

    public static Container updateInv(Container inventory, LivingEntity target, boolean editable) {
        //物品栏
        if (editable) {
            inventory.setItem(61, new ItemStack(ModItems.BBJI.get()));
            if (target instanceof Player player) {
                NonNullList<ItemStack> inv = player.getInventory().items;
                for (var stack : inv) {
                    inventory.setItem(inv.indexOf(stack), stack);
                }
            } else if (target instanceof Villager villager) {
                NonNullList<ItemStack> inv = villager.getInventory().getItems();
                for (var stack : inv) {
                    inventory.setItem(inv.indexOf(stack), stack);
                }
            } else inventory.setItem(0, target.getMainHandItem());
        }

        List<ItemStack> armors = List.of(target.getItemBySlot(EquipmentSlot.HEAD), target.getItemBySlot(EquipmentSlot.CHEST), target.getItemBySlot(EquipmentSlot.LEGS), target.getItemBySlot(EquipmentSlot.FEET));
        for (ItemStack stack : armors) {
            inventory.setItem(armors.indexOf(stack) + 36, stack);
        } //盔甲栏

        inventory.setItem(40, target.getOffhandItem()); //副手

        var component = CuriosApi.getCuriosInventory(target);
        if (component.isPresent()) {//饰品栏
            var trinkets = component.get().getCurios();
            List<ItemStack> stacks = new ArrayList<>();
            for (var trinket : trinkets.values()) {
                for (int i = 0; i < trinket.getSlots(); i++) {
                    stacks.add(trinket.getStacks().getStackInSlot(i));
                }
            }
            for (var stack : stacks) {
                inventory.setItem(stacks.indexOf(stack) + 41, stack);
            }
        }
        return inventory;
    }
}
