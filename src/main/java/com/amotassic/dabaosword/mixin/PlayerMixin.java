package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Gamerule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static com.amotassic.dabaosword.util.ModTools.*;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {super(p_20966_, p_20967_);}

    @Unique Player dabaoSword$player = (Player) (Object) this;

    @Unique private int dabaoSword$tick = 0;
    @Unique private int dabaoSword$tick2 = 0;
    @Unique private int dabaoSword$skillChange = 0;

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel world) {
            int giveCard = world.getGameRules().getInt(Gamerule.GIVE_CARD_INTERVAL) * 20;
            int skill = world.getGameRules().getInt(Gamerule.CHANGE_SKILL_INTERVAL) * 20;
            boolean enableLimit = world.getGameRules().getBoolean(Gamerule.ENABLE_CARDS_LIMIT);

            if (++dabaoSword$tick >= giveCard) { // 每分钟摸两张牌
                dabaoSword$tick = 0;
                if (hasTrinket(ModItems.CARD_PILE.get(), dabaoSword$player) && !dabaoSword$player.isCreative() && !dabaoSword$player.isSpectator()) {
                    if (countAllCard(dabaoSword$player) <= dabaoSword$player.getMaxHealth()) {
                        dabaoSword$player.addItem(new ItemStack(ModItems.GAIN_CARD, 2));
                        dabaoSword$player.displayClientMessage(Component.translatable("dabaosword.draw"),true);
                    } else if (!enableLimit) {//如果不限制摸牌就继续发牌
                        dabaoSword$player.addItem(new ItemStack(ModItems.GAIN_CARD, 2));
                        dabaoSword$player.displayClientMessage(Component.translatable("dabaosword.draw"),true);
                    }
                }
            }

            if (skill != -20) {
                if (++dabaoSword$skillChange >= skill) {//每5分钟可以切换技能
                    dabaoSword$skillChange = 0;
                    dabaoSword$player.addTag("change_skill");
                    if (skill >= 600 && hasTrinket(ModItems.CARD_PILE.get(), dabaoSword$player)) {
                        dabaoSword$player.displayClientMessage(Component.translatable("dabaosword.change_skill").withStyle(ChatFormatting.BOLD),false);
                        dabaoSword$player.displayClientMessage(Component.translatable("dabaosword.change_skill2"),false);
                    }
                }
            }

            if (++dabaoSword$tick2 >= 2) {
                dabaoSword$tick2 = 0;
                dabaoSword$player.getTags().remove("quanji");
                dabaoSword$player.getTags().remove("sha");
                dabaoSword$player.getTags().remove("benxi");
                dabaoSword$player.getTags().remove("juedou");
                dabaoSword$player.getTags().remove("xingshang");

                //牌堆恢复饱食度
                boolean food = world.getGameRules().getBoolean(Gamerule.CARD_PILE_HUNGERLESS);
                if (hasTrinket(ModItems.CARD_PILE.get(), dabaoSword$player) && food) dabaoSword$player.getFoodData().setFoodLevel(20);
            }

            AABB box = new AABB(dabaoSword$player.getOnPos()).inflate(20); // 检测范围，根据需要修改
            for (LivingEntity nearbyPlayer : world.getEntitiesOfClass(Player.class, box, playerEntity -> playerEntity.hasEffect(ModItems.DEFEND))) {
                //实现沈佳宜的效果：若玩家看到的玩家有近战防御效果，则给当前玩家攻击范围缩短效果
                int amplifier = Objects.requireNonNull(nearbyPlayer.getEffect(ModItems.DEFEND)).getAmplifier();
                int attack = (int) dabaoSword$player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
                int defended = Math.min(amplifier, attack);
                if (dabaoSword$player != nearbyPlayer && dabaoSword$isLooking(dabaoSword$player, nearbyPlayer)) {
                    dabaoSword$player.addEffect(new MobEffectInstance(ModItems.DEFENDED, 1,defended,false,false,false));
                }
            }

            int level1 = 0; int level2 = 0; //马术和飞影的效果
            if (dabaoSword$shouldMashu(dabaoSword$player)) {
                if (hasTrinket(ModItems.CHITU.get(), dabaoSword$player)) level1++;
                if (hasTrinket(SkillCards.MASHU.get(), dabaoSword$player)) level1++;
                if (level1 > 0) dabaoSword$player.addEffect(new MobEffectInstance(ModItems.REACH, 10,level1,false,false,true));
            }
            if (hasTrinket(ModItems.DILU.get(), dabaoSword$player)) level2++;
            if (hasTrinket(SkillCards.FEIYING.get(), dabaoSword$player)) level2++;
            if (level2 > 0) dabaoSword$player.addEffect(new MobEffectInstance(ModItems.DEFEND, 10,level2,false,false,true));

            if (this.getTags().contains("px")) {
                this.attackStrengthTicker = 1145;
            }

        }
    }

    @Unique
    boolean dabaoSword$shouldMashu(Player player) {
        return !hasTrinket(SkillCards.BENXI.get(), player) && player.getMainHandItem().getItem() != ModItems.JUEDOU.get() && player.getMainHandItem().getItem() != ModItems.DISCARD.get();
    }

    @Unique
    boolean dabaoSword$isLooking(Player player, Entity entity) {
        Vec3 vec3d = player.getViewVector(1.0f).normalize();
        Vec3 vec3d2 = new Vec3(entity.getX() - player.getX(), entity.getEyeY() - player.getEyeY(), entity.getZ() - player.getZ());
        double d = vec3d2.length();
        double e = vec3d.dot(vec3d2.normalize());
        if (e > 1.0 - 0.25 / d) {
            return player.hasLineOfSight(entity);
        }
        return false;
    }
}
