package com.amotassic.dabaosword;

import com.amotassic.dabaosword.command.DabaoSwordCommand;
import com.amotassic.dabaosword.damage_type.ModDT;
import com.amotassic.dabaosword.data.CardSuitAndRank;
import com.amotassic.dabaosword.entity.ModEntity;
import com.amotassic.dabaosword.entity.XuyouEntity;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Gamerule;
import com.amotassic.dabaosword.util.MODConfig;
import com.amotassic.dabaosword.util.Tags;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.slf4j.Logger;

import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

@Mod(DabaoSword.MODID)
public class DabaoSword {
    public static final String MODID = "dabaosword";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MinecraftServer server;

    public DabaoSword(IEventBus modEventBus, ModContainer container, Dist dist) {
        LOGGER.info("Ciallo～(∠·ω< )⌒★");
        container.registerConfig(ModConfig.Type.COMMON, MODConfig.SPEC);
        if (dist.isClient()) {
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        commonSetup();

        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, DabaoSwordCommand::registerCommands);
        NeoForge.EVENT_BUS.addListener(AddReloadListenerEvent.class, DabaoSword::loadData);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerMobAttributes);
        modEventBus.addListener(this::spawnRestriction);
    }

    private static void commonSetup() {
        ModEntity.register();
        ModItems.register();
        long start = System.currentTimeMillis();
        SkillCards.register();
        LOGGER.info("Loaded all skills in {}ms", System.currentTimeMillis() - start);
        Gamerule.registerGamerules();
        ModDT.init();
        Tags.Tag();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(Items.NETHERITE_SWORD.getDefaultInstance(), ModItems.GUDINGDAO.getDefaultInstance(), PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.EGG.getDefaultInstance(), ModItems.ARROW_RAIN.getDefaultInstance(), PARENT_AND_SEARCH_TABS);
        }
    }

    private void registerMobAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntity.XUYOU, XuyouEntity.createAttributes().build());
    }

    private void spawnRestriction(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntity.XUYOU, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    private static void loadData(AddReloadListenerEvent event) {
        event.addListener(CardSuitAndRank.INSTANCE);
    }
}
