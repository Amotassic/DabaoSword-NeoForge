package com.amotassic.dabaosword.util;

import net.minecraft.world.level.GameRules;

public class Gamerule {
    public static final GameRules.Key<GameRules.BooleanValue> FIRE_ATTACK_BREAKS_BLOCK =
            GameRules.register("fire_attack_breaks_block", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
    public static final GameRules.Key<GameRules.BooleanValue> CARD_PILE_HUNGERLESS =
            GameRules.register("card_pile_hungerless", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
    public static final GameRules.Key<GameRules.BooleanValue> CLEAR_CARDS_AFTER_DEATH =
            GameRules.register("clear_cards_after_death", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.IntegerValue> GIVE_CARD_INTERVAL = GameRules.register(
            "give_card_interval", GameRules.Category.MISC, GameRules.IntegerValue.create(60));
    public static final GameRules.Key<GameRules.IntegerValue> CHANGE_SKILL_INTERVAL = GameRules.register(
            "change_skill_interval", GameRules.Category.MISC, GameRules.IntegerValue.create(300));
    public static final GameRules.Key<GameRules.BooleanValue> ENABLE_CARDS_LIMIT =
            GameRules.register("enable_cards_limit", GameRules.Category.MISC, GameRules.BooleanValue.create(true));

    public static void registerGamerules() {}
}
