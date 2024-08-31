package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    @Shadow public abstract boolean hurt(@NotNull DamageSource source, float amount);

    @Shadow public abstract boolean isAlive();

    @Shadow public abstract boolean isCurrentlyGlowing();

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);

    @Shadow public abstract ItemStack getOffhandItem();

    @Shadow public abstract boolean addEffect(MobEffectInstance effectInstance);

    @Unique LivingEntity dabaoSword$living = (LivingEntity) (Object) this;

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void damageMixin(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if (this.level() instanceof ServerLevel world) {
            //无敌效果
            if (this.hasEffect(ModItems.INVULNERABLE) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                cir.setReturnValue(false);
            }

            if (source.getDirectEntity() instanceof LivingEntity entity) {

                //决斗等物品虽然手长，但过远时普通伤害无效
                if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && this.distanceTo(entity) > 5) {
                    if (entity.getMainHandItem().getItem() == ModItems.JUEDOU.get() || entity.getMainHandItem().getItem() == ModItems.DISCARD.get()) cir.setReturnValue(false);
                }

                //沈佳宜防御效果
                if (!(entity instanceof Player) && this.hasEffect(ModItems.DEFEND)) {
                    if (Objects.requireNonNull(this.getEffect(ModItems.DEFEND)).getAmplifier() >= 2) {
                        cir.setReturnValue(false);
                    }
                }

                //被乐的生物无法造成普通攻击伤害
                if (entity.hasEffect(ModItems.TOO_HAPPY)) cir.setReturnValue(false);
            }

            if (source.getEntity() instanceof LivingEntity entity) {

                if (!(dabaoSword$living instanceof Player)) {//livingEntity的闪的被动效果
                    boolean hasShan = getOffhandItem().getItem() == ModItems.SHAN.get();
                    boolean shouldShan = !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && hasShan && !hasEffect(ModItems.COOLDOWN2) && !hasEffect(ModItems.INVULNERABLE);
                    if (shouldShan) {
                        cir.setReturnValue(false);
                        dabaoSword$living.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20,0,false,false,false));
                        dabaoSword$living.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 40,0,false,false,false));
                        voice(dabaoSword$living, Sounds.SHAN.get());
                        getOffhandItem().shrink(1);
                        //虽然没有因为杀而触发闪，但如果攻击者的杀处于自动触发状态，则仍会消耗
                        if (source.getDirectEntity() instanceof Player player1 && getShaSlot(player1) != -1) {
                            ItemStack stack = shaStack(player1);
                            if (stack.getItem() == ModItems.SHA.get()) voice(player1, Sounds.SHA.get());
                            if (stack.getItem() == ModItems.FIRE_SHA.get()) voice(player1, Sounds.SHA_FIRE.get());
                            if (stack.getItem() == ModItems.THUNDER_SHA.get()) voice(player1, Sounds.SHA_THUNDER.get());
                            NeoForge.EVENT_BUS.post(new CardUsePostListener(player1, stack, dabaoSword$living));
                        }
                    }
                }

                //翻面的生物（除了玩家）无法造成伤害
                if (!(entity instanceof Player) && entity.hasEffect(ModItems.TURNOVER)) cir.setReturnValue(false);

                if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                    //被乐的生物的弹射物无法造成伤害
                    if (entity.hasEffect(ModItems.TOO_HAPPY)) cir.setReturnValue(false);
                }

                if (entity instanceof Player player) {

                    if (this.isCurrentlyGlowing() && dabaoSword$shouldSha(player)) {//实现铁索连环的效果，大概是好了吧
                        ItemStack stack = player.getMainHandItem().is(Tags.SHA) ? player.getMainHandItem() : shaStack(player);
                        player.addTag("sha");
                        if (stack.getItem() == ModItems.SHA.get()) {
                            voice(player, Sounds.SHA.get());
                            if (!hasTrinket(ModItems.RATTAN_ARMOR.get(), dabaoSword$living)) {
                                dabaoSword$living.invulnerableTime = 0; dabaoSword$living.hurt(source, 5);
                            }
                        }
                        if (stack.getItem() == ModItems.FIRE_SHA.get()) voice(player, Sounds.SHA_FIRE.get());
                        if (stack.getItem() == ModItems.THUNDER_SHA.get()) voice(player, Sounds.SHA_THUNDER.get());
                        AABB box = new AABB(player.getOnPos()).inflate(20); // 检测范围，根据需要修改
                        for (LivingEntity nearbyEntity : world.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isCurrentlyGlowing)) {
                            //处理杀的效果
                            if (stack.getItem() == ModItems.FIRE_SHA.get()) {
                                nearbyEntity.invulnerableTime = 0;
                                nearbyEntity.setRemainingFireTicks(100);
                                nearbyEntity.removeEffect(MobEffects.GLOWING);
                                nearbyEntity.hurt(source, amount);
                            }
                            if (stack.getItem() == ModItems.THUNDER_SHA.get()) {
                                nearbyEntity.invulnerableTime = 0;
                                nearbyEntity.hurt(player.damageSources().magic(), 5);
                                LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                                if (lightningEntity != null) {
                                    lightningEntity.moveTo(nearbyEntity.getX(), nearbyEntity.getY(), nearbyEntity.getZ());
                                    lightningEntity.setVisualOnly(true);
                                    world.addFreshEntity(lightningEntity);
                                }
                                nearbyEntity.removeEffect(MobEffects.GLOWING);
                                nearbyEntity.hurt(source, amount);
                            }
                        }
                        NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, dabaoSword$living));
                    }

                    //绝情效果
                    if (hasTrinket(SkillCards.JUEQING.get(), player)) {
                        cir.setReturnValue(false);
                        this.hurt(world.damageSources().genericKill(), Math.min(6, amount));
                        if (new Random().nextFloat() < 0.5) {voice(player, Sounds.JUEQING1.get(), 1);} else {voice(player, Sounds.JUEQING2.get(), 1);}
                    }

                }

            }

        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        //若方天画戟被触发了，只要左键就可以造成群伤
        Player closestPlayer = level().getNearestPlayer(this, 5);
        if (closestPlayer != null && hasTrinket(ModItems.FANGTIAN.get(), closestPlayer) && !level().isClientSide && isAlive()) {
            ItemStack stack = trinketItem(ModItems.FANGTIAN.get(), closestPlayer);
            int time = getCD(stack);
            if (time > 15 && closestPlayer.swingTime == 1) {
                //给玩家本人一个极短的无敌效果，以防止被误伤
                closestPlayer.addEffect(new MobEffectInstance(ModItems.INVULNERABLE,2,0,false,false,false));
                float i = (float) closestPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE);
                this.hurt(damageSources().playerAttack(closestPlayer), i);
            }
        }

        if (dabaoSword$living instanceof Mob mob && mob.hasEffect(ModItems.TURNOVER)) mob.setTarget(null);
    }

    @Unique
    boolean dabaoSword$shouldSha(Player player) {
        return getShaSlot(player) != -1 && !player.getTags().contains("sha") && !player.getTags().contains("juedou") && !player.getTags().contains("wanjian");
    }
}
