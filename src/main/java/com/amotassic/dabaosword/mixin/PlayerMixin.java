package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Gamerule;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract boolean isCreative();

    @Shadow public abstract boolean hurt(DamageSource p_36154_, float p_36155_);

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {super(p_20966_, p_20967_);}

    @Unique Player player = (Player) (Object) this;

    @Inject(method = "hurt",at = @At("HEAD"), cancellable = true)
    private void damagemixin(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if (this.level() instanceof ServerLevel world) {

            if (source.getEntity() instanceof LivingEntity entity) {
                //若攻击者主手没有物品，则无法击穿藤甲
                if (inrattan(player)) {
                    if (entity.getMainHandItem().isEmpty()) cir.setReturnValue(false);
                    else if (getShanSlot(player) != -1 && !this.hasEffect(ModItems.COOLDOWN2)) {
                        cir.setReturnValue(false);
                        shan(player,false);//闪的额外判断
                    }
                }
            }
            //弹射物对藤甲无效
            if (source.is(DamageTypeTags.IS_PROJECTILE) && inrattan(player)) {cir.setReturnValue(false);}

            if (source.getEntity() instanceof Wolf dog && dog.hasEffect(ModItems.INVULNERABLE)) {
                //被南蛮入侵的狗打中可以消耗杀以免疫伤害
                if (dog.getOwner() != this) {
                    if (getShaSlot(player) != -1) {
                        ItemStack stack = shaStack(player);
                        cir.setReturnValue(false);
                        if (stack.getItem() == ModItems.SHA.get()) voice(player, Sounds.SHA.get());
                        if (stack.getItem() == ModItems.FIRE_SHA.get()) voice(player, Sounds.SHA_FIRE.get());
                        if (stack.getItem() == ModItems.THUNDER_SHA.get()) voice(player, Sounds.SHA_THUNDER.get());
                        stack.shrink(1);
                    }
                    dog.setHealth(0);
                }
            }

            if (source.getEntity() instanceof LivingEntity) {

                final boolean trigger = baguaTrigger(player);
                boolean hasShan = getShanSlot(player) != -1 || trigger;
                boolean shouldShan = !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !getTags().contains("juedou") && hasShan && !isCreative() && !hasEffect(ModItems.COOLDOWN2) && !hasEffect(ModItems.INVULNERABLE) && !hasTrinket(SkillCards.LIULI.get(), player) && !hasTrinket(ModItems.RATTAN_ARMOR.get(), player);

                //闪的被动效果
                if (shouldShan) {
                    cir.setReturnValue(false);
                    shan(player, trigger);
                    //虽然没有因为杀而触发闪，但如果攻击者的杀处于自动触发状态，则仍会消耗
                    if (source.getDirectEntity() instanceof Player player1 && getShaSlot(player1) != -1) {
                        ItemStack stack = shaStack(player1);
                        if (stack.getItem() == ModItems.SHA.get()) voice(player1, Sounds.SHA.get());
                        if (stack.getItem() == ModItems.FIRE_SHA.get()) voice(player1, Sounds.SHA_FIRE.get());
                        if (stack.getItem() == ModItems.THUNDER_SHA.get()) voice(player1, Sounds.SHA_THUNDER.get());
                        benxi(player1);
                        if (!player1.isCreative()) stack.shrink(1);
                    }
                }

            }

            //流离
            if (hasTrinket(SkillCards.LIULI.get(), player) && source.getEntity() instanceof LivingEntity attacker && hasItemInTag(Tags.CARD, player) && !this.hasEffect(ModItems.INVULNERABLE) && !this.isCreative()) {
                ItemStack stack = stackInTag(Tags.CARD, player);
                AABB box = new AABB(this.getOnPos()).inflate(10);
                int i = 0;
                for (LivingEntity nearbyEntity : world.getEntitiesOfClass(LivingEntity.class, box, LivingEntity -> LivingEntity != attacker && LivingEntity != this)) {
                    if (nearbyEntity != null) {
                        cir.setReturnValue(false);
                        this.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 10,0,false,false,false));
                        this.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 10,0,false,false,false));
                        stack.shrink(1);
                        if (new Random().nextFloat() < 0.5) {voice(player, Sounds.LIULI1.get());} else {voice(player, Sounds.LIULI2.get());}
                        nearbyEntity.invulnerableTime = 0;
                        nearbyEntity.hurt(source, amount); i++; break;
                    }
                }
                //避免闪自动触发，因此在这里额外判断
                if (i == 0 && !this.hasEffect(ModItems.COOLDOWN2)) {
                    final boolean trigger = baguaTrigger(player);
                    boolean hasShan = getShanSlot(player) != -1 || trigger;
                    if (hasShan) {
                        cir.setReturnValue(false);
                        shan(player, trigger);
                    }
                }
            }

        }
    }

    @Unique private int tick = 0;
    @Unique private int tick2 = 0;
    @Unique private int skillChange = 0;

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel world) {
            int giveCard = world.getGameRules().getInt(Gamerule.GIVE_CARD_INTERVAL) * 20;
            int skill = world.getGameRules().getInt(Gamerule.CHANGE_SKILL_INTERVAL) * 20;
            boolean enableLimit = world.getGameRules().getBoolean(Gamerule.ENABLE_CARDS_LIMIT);

            if (++tick >= giveCard) { // 每分钟摸两张牌
                tick = 0;
                if (hasTrinket(ModItems.CARD_PILE.get(), player) && !player.isCreative() && !player.isSpectator()) {
                    if (count(player, Tags.CARD) + count(player, ModItems.GAIN_CARD.get()) <= player.getMaxHealth()) {
                        player.addItem(new ItemStack(ModItems.GAIN_CARD, 2));
                        player.displayClientMessage(Component.translatable("dabaosword.draw"),true);
                    } else if (!enableLimit) {//如果不限制摸牌就继续发牌
                        player.addItem(new ItemStack(ModItems.GAIN_CARD, 2));
                        player.displayClientMessage(Component.translatable("dabaosword.draw"),true);
                    }
                }
            }

            if (skill != -20) {
                if (++skillChange >= skill) {//每5分钟可以切换技能
                    skillChange = 0;
                    player.addTag("change_skill");
                    if (skill >= 600) {
                        player.displayClientMessage(Component.translatable("dabaosword.change_skill").withStyle(ChatFormatting.BOLD),false);
                        player.displayClientMessage(Component.translatable("dabaosword.change_skill2"),false);
                    }
                }
            }

            if (++tick2 >= 2) {
                tick2 = 0;
                player.getTags().remove("quanji");
                player.getTags().remove("sha");
                player.getTags().remove("benxi");
                player.getTags().remove("juedou");
                player.getTags().remove("xingshang");
                player.getTags().remove("kill_entity");

                //牌堆恢复饱食度
                boolean food = world.getGameRules().getBoolean(Gamerule.CARD_PILE_HUNGERLESS);
                if (hasTrinket(ModItems.CARD_PILE.get(), player) && food) {player.getFoodData().setFoodLevel(20);}
            }

            AABB box = new AABB(player.getOnPos()).inflate(20); // 检测范围，根据需要修改
            for (LivingEntity nearbyPlayer : world.getEntitiesOfClass(Player.class, box, playerEntity -> playerEntity.hasEffect(ModItems.DEFEND))) {
                //实现沈佳宜的效果：若玩家看到的玩家有近战防御效果，则给当前玩家攻击范围缩短效果
                int amplifier = Objects.requireNonNull(nearbyPlayer.getEffect(ModItems.DEFEND)).getAmplifier();
                int attack = (int) player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
                int defensed = Math.min(amplifier, attack);
                if (player != nearbyPlayer && islooking(player, nearbyPlayer)) {
                    player.addEffect(new MobEffectInstance(ModItems.DEFENDED, 1,defensed,false,false,false));
                }
            }

            //马术和飞影的效果
            if (shouldMashu(player)) {
                if (hasTrinket(ModItems.CHITU.get(), player) && hasTrinket(SkillCards.MASHU.get(), player)) {
                    player.addEffect(new MobEffectInstance(ModItems.REACH, 10,2,false,false,true));
                } else if (hasTrinket(ModItems.CHITU.get(), player) || hasTrinket(SkillCards.MASHU.get(), player)) {
                    player.addEffect(new MobEffectInstance(ModItems.REACH, 10,1,false,false,true));
                }
            }
            if (hasTrinket(ModItems.DILU.get(), player) && hasTrinket(SkillCards.FEIYING.get(), player)) {
                player.addEffect(new MobEffectInstance(ModItems.DEFEND, 10,2,false,false,true));
            } else if (hasTrinket(ModItems.DILU.get(), player) || hasTrinket(SkillCards.FEIYING.get(), player)) {
                player.addEffect(new MobEffectInstance(ModItems.DEFEND, 10,1,false,false,true));
            }

            if (this.getTags().contains("px")) {
                this.attackStrengthTicker = 1145;
            }

        }
    }

    @Unique
    boolean shouldMashu(Player player) {
        return !hasTrinket(SkillCards.BENXI.get(), player) && player.getMainHandItem().getItem() != ModItems.JUEDOU.get() && player.getMainHandItem().getItem() != ModItems.DISCARD.get();
    }

    @Unique
    boolean islooking(Player player, Entity entity) {
        Vec3 vec3d = player.getViewVector(1.0f).normalize();
        Vec3 vec3d2 = new Vec3(entity.getX() - player.getX(), entity.getEyeY() - player.getEyeY(), entity.getZ() - player.getZ());
        double d = vec3d2.length();
        double e = vec3d.dot(vec3d2.normalize());
        if (e > 1.0 - 0.25 / d) {
            return player.hasLineOfSight(entity);
        }
        return false;
    }

    @Unique boolean inrattan(Player player) {
        return hasTrinket(ModItems.RATTAN_ARMOR.get(), player);
    }

    @Unique boolean baguaTrigger(Player player) {
        return hasTrinket(ModItems.BAGUA.get(), player) && new Random().nextFloat() < 0.5;
    }
    @Unique
    void shan(Player player, boolean bl) {
        ItemStack stack = bl ? trinketItem(ModItems.BAGUA.get(), player) : shanStack(player);
        int cd = bl ? 60 : 40;
        player.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20,0,false,false,false));
        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, cd,0,false,false,false));
        voice(player, Sounds.SHAN.get());
        benxi(player);
        if (bl) player.displayClientMessage(Component.translatable("dabaosword.bagua"),true);
        else stack.shrink(1);
    }

    @Unique
    int getShanSlot(Player player) {
        for (int i = 0; i < 18; ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || stack.getItem() != ModItems.SHAN.get()) continue;
            return i;
        }
        return -1;
    }

    @Unique
    ItemStack shanStack(Player player) {
        return player.getInventory().getItem(getShanSlot(player));
    }
}
