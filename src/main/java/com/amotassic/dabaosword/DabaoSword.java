package com.amotassic.dabaosword;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.AllRegs;
import com.amotassic.dabaosword.util.Gamerule;
import com.amotassic.dabaosword.util.Tags;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(DabaoSword.MODID)
public class DabaoSword {
    public static final String MODID = "dabaosword";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DabaoSword(IEventBus modEventBus) {
        LOGGER.info("Ciallo～(∠·ω< )⌒★");
        AllRegs.Skills.ITEMS.register(modEventBus);
        AllRegs.Items.ITEMS.register(modEventBus);
        AllRegs.Effects.EFFECTS.register(modEventBus);
        AllRegs.Other.DATA_COMPONENT.register(modEventBus);
        AllRegs.Other.MENU.register(modEventBus);
        AllRegs.Items.TABS.register(modEventBus);
        AllRegs.Sounds.SOUNDS.register(modEventBus);
        Gamerule.registerGamerules();
        Tags.Tag();

        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(Items.NETHERITE_SWORD.getDefaultInstance(), ModItems.GUDINGDAO.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.EGG.getDefaultInstance(), ModItems.ARROW_RAIN.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
