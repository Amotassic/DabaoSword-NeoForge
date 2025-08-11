package com.amotassic.dabaosword.data;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.card.Card;
import com.amotassic.dabaosword.api.card.Rank;
import com.amotassic.dabaosword.api.card.Suit;
import com.amotassic.dabaosword.item.card.CardItem;
import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardSuitAndRank extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final CardSuitAndRank INSTANCE = new CardSuitAndRank();
    // 所有的牌
    public static final List<ItemStack> ALL_CARDS = new ArrayList<>();

    public CardSuitAndRank() {super(GSON, "default_suit_and_rank");}

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ALL_CARDS.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : prepared.entrySet()) {
            // System.out.println(entry.getKey() + " " + entry.getValue());
            ResourceLocation key = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            try {
                Item item = BuiltInRegistries.ITEM.get(key);
                if (item instanceof CardItem cardItem) {
                    try {
                        JsonArray srs = jsonElement.getAsJsonObject().get("suits_and_ranks").getAsJsonArray();
                        for (int j = 0; j < srs.size(); j++) {
                            JsonObject sr = srs.get(j).getAsJsonObject();
                            String suit = sr.get("suit").getAsString();
                            String rank = sr.get("rank").getAsString();

                            Card card = new Card(cardItem, Suit.valueOf(suit), Rank.fromString(rank));
                            ALL_CARDS.add(card.toStack());
                        }
                    } catch (Exception e) {
                        DabaoSword.LOGGER.error("Missing Element: suits_and_ranks");
                    }
                }
            } catch (Exception e) {
                DabaoSword.LOGGER.error("No such card registered: {}", key);
            }
        }
        DabaoSword.LOGGER.info("Loaded {} cards", ALL_CARDS.size());
    }
}
