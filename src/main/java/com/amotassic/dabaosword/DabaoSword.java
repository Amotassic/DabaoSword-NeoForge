package com.amotassic.dabaosword;

import com.amotassic.dabaosword.command.InfoCommand;
import com.amotassic.dabaosword.entity.ModEntity;
import com.amotassic.dabaosword.entity.XuyouEntity;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.AllRegs;
import com.amotassic.dabaosword.util.Gamerule;
import com.amotassic.dabaosword.util.MODConfig;
import com.amotassic.dabaosword.util.Tags;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.slf4j.Logger;

import static com.amotassic.dabaosword.util.AllRegs.Other.ZZRS;
import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

@Mod(DabaoSword.MODID)
public class DabaoSword {
    public static final String MODID = "dabaosword";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MinecraftServer server;

    public DabaoSword(IEventBus modEventBus, ModContainer container) {
        LOGGER.info("Ciallo～(∠·ω< )⌒★");
        container.registerConfig(ModConfig.Type.COMMON, MODConfig.SPEC);

        ModEntity.ENTITIES.register(modEventBus);
        AllRegs.Skills.ITEMS.register(modEventBus);
        AllRegs.Items.ITEMS.register(modEventBus);
        AllRegs.Effects.EFFECTS.register(modEventBus);
        AllRegs.Other.DATA_COMPONENT.register(modEventBus);
        AllRegs.Other.MENU.register(modEventBus);
        AllRegs.Other.TABS.register(modEventBus);
        Gamerule.registerGamerules();
        Tags.Tag();

        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, InfoCommand::registerCommands);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerMobAttributes);
        modEventBus.addListener(this::spawnRestriction);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(Items.NETHERITE_SWORD.getDefaultInstance(), ModItems.GUDINGDAO.getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.EGG.getDefaultInstance(), ModItems.ARROW_RAIN.getDefaultInstance(), PARENT_AND_SEARCH_TABS);
        }

        if (event.getTabKey() == ZZRS) {
            var lookup = event.getParameters().holders().lookup(Registries.ENCHANTMENT).orElseThrow();
            ItemStack smile = new ItemStack(ModItems.SUNSHINE_SMILE);
            smile.enchant(lookup.getOrThrow(ModItems.CRIT), 1);
            event.insertAfter(ModItems.LET_ME_CC.getDefaultInstance(), smile, PARENT_AND_SEARCH_TABS);
        }
    }

    private void registerMobAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntity.XUYOU.get(), XuyouEntity.createAttributes().build());
    }

    private void spawnRestriction(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntity.XUYOU.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }
}
