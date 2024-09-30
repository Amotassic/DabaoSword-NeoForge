package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.amotassic.dabaosword.util.ModTools.*;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow public abstract void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {super(entityType, level);}

    @Unique Mob dabaoSword$mob = (Mob) (Object) this;

    @Inject(method = "populateDefaultEquipmentSlots", at = @At(value = "TAIL"))
    protected void initEquipment(RandomSource random, DifficultyInstance localDifficulty, CallbackInfo ci) {
        if (!level().isClientSide && new Random().nextFloat() < dabaoSword$getChance()) dabaoSword$initCards();
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(CallbackInfo ci) {
        if (getOffhandItem().getItem() == ModItems.PEACH && getHealth() <= getMaxHealth() - 5) {
            heal(5);
            voice(dabaoSword$mob, Sounds.RECOVER);
            getOffhandItem().shrink(1);
        }
    }

    @Inject(method = "doHurtTarget", at = @At(value = "HEAD"))
    public void tryAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (getMainHandItem().is(Tags.CARD) && target instanceof LivingEntity) dabaoSword$tryUseCard(getMainHandItem(), (LivingEntity) target);
    }

    @Unique
    private void dabaoSword$tryUseCard(ItemStack stack, LivingEntity target) {
        if (stack.getItem() == ModItems.SHA) {
            if (!hasTrinket(ModItems.RATTAN_ARMOR, target)) {
                target.invulnerableTime = 0; target.hurt(dabaoSword$mob.damageSources().mobAttack(dabaoSword$mob), 5);
            }
            voice(dabaoSword$mob, Sounds.SHA); stack.shrink(1);
        }

        if (stack.getItem() == ModItems.BINGLIANG_ITEM) {
            if (target instanceof Player player && hasItem(player, ModItems.WUXIE)) {
                cardUsePost(player, getItem(player, ModItems.WUXIE), null);
                voice(player, Sounds.WUXIE);
            } else target.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION,1));
            voice(dabaoSword$mob, Sounds.BINGLIANG); stack.shrink(1);
        }

        if (stack.getItem() == ModItems.TOO_HAPPY_ITEM) {
            if (target instanceof Player player) {
                if (hasItem(player, ModItems.WUXIE)) {
                    cardUsePost(player, getItem(player, ModItems.WUXIE), null);
                    voice(player, Sounds.WUXIE);
                } else player.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
            } else target.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 15));
            voice(dabaoSword$mob, Sounds.LEBU); stack.shrink(1);
        }

        if (stack.getItem() == ModItems.DISCARD) {
            if (target instanceof Player player) {//如果是玩家则弃牌
                if (hasItem(player, ModItems.WUXIE)) {
                    cardUsePost(player, getItem(player, ModItems.WUXIE), null);
                    voice(player, Sounds.WUXIE);
                    voice(dabaoSword$mob, Sounds.GUOHE); stack.shrink(1);
                } else {
                    List<ItemStack> candidate = new ArrayList<>();
                    //把背包中的卡牌添加到待选物品中
                    NonNullList<ItemStack> inventory = player.getInventory().items;
                    List<Integer> cardSlots = IntStream.range(0, inventory.size()).filter(j -> isCard(inventory.get(j))).boxed().toList();
                    for (Integer slot : cardSlots) {candidate.add(inventory.get(slot));}
                    //把饰品栏的卡牌添加到待选物品中
                    int equip = 0; //用于标记装备区牌的数量
                    var component = CuriosApi.getCuriosInventory(player);
                    if(component.isPresent()) {
                        var allEquipped = component.get().getEquippedCurios();
                        for(int j = 0; j < allEquipped.getSlots(); j++) {
                            ItemStack stack1 = allEquipped.getStackInSlot(j);
                            if (stack1.is(Tags.CARD)) candidate.add(stack1); equip++;
                        }
                    }
                    if(!candidate.isEmpty()) {
                        int index = new Random().nextInt(candidate.size()); ItemStack chosen = candidate.get(index);
                        player.displayClientMessage(Component.literal(dabaoSword$mob.getScoreboardName()).append(Component.translatable("dabaosword.discard")).append(chosen.getDisplayName()), false);
                        cardDiscard(player, chosen, 1, index > candidate.size() - equip);
                        voice(dabaoSword$mob, Sounds.GUOHE); stack.shrink(1);
                    }
                }
            } else {//如果不是玩家则随机弃置它的主副手物品和装备
                List<ItemStack> candidate = new ArrayList<>();
                if (!target.getMainHandItem().isEmpty()) candidate.add(target.getMainHandItem());
                if (!target.getOffhandItem().isEmpty()) candidate.add(target.getOffhandItem());
                for (ItemStack armor : target.getArmorSlots()) {if (!armor.isEmpty()) candidate.add(armor);}
                if(!candidate.isEmpty()) {
                    int index = new java.util.Random().nextInt(candidate.size()); ItemStack chosen = candidate.get(index);
                    chosen.shrink(1);
                    voice(dabaoSword$mob, Sounds.GUOHE); stack.shrink(1);
                }
            }
        }

        if (stack.getItem() == ModItems.FIRE_ATTACK) {
            ServerLevel world = (ServerLevel) level();
            Vec3 momentum = dabaoSword$mob.getForward().scale(3);
            LargeFireball fireballEntity = new LargeFireball(world, dabaoSword$mob, momentum, 3);
            fireballEntity.setPos(dabaoSword$mob.getX(), dabaoSword$mob.getY(0.5) + 0.5, dabaoSword$mob.getZ());
            world.addFreshEntity(fireballEntity);
            voice(dabaoSword$mob, Sounds.HUOGONG); stack.shrink(1);
        }

        if (stack.getItem() == ModItems.JIEDAO) {
            ItemStack stack1 = target.getMainHandItem();
            if (!stack1.isEmpty()) {
                if (target instanceof Player player && hasItem(player, ModItems.WUXIE)) {
                    cardUsePost(player, getItem(player, ModItems.WUXIE), null);
                    voice(player, Sounds.WUXIE);
                } else {
                    dabaoSword$mob.setItemInHand(InteractionHand.MAIN_HAND, stack1.copy());
                    stack1.setCount(0);
                }
                voice(dabaoSword$mob, Sounds.JIEDAO);
            }
        }

        if (stack.getItem() == ModItems.NANMAN) {//暂时不想做，摆————

        }

        if (stack.getItem() == ModItems.WANJIAN) {
            dabaoSword$mob.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 15,1,false,false,false));
            voice(dabaoSword$mob, Sounds.WANJIAN); stack.shrink(1);
        }

        if (stack.getItem() == ModItems.TIESUO) {
            dabaoSword$mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, MobEffectInstance.INFINITE_DURATION, 0, false, true,false));
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, MobEffectInstance.INFINITE_DURATION, 0, false, true,false));
            voice(dabaoSword$mob, Sounds.TIESUO); stack.shrink(1);
        }
    }

    @Unique private float dabaoSword$getChance() {
        Difficulty difficulty = level().getDifficulty();
        if (difficulty == Difficulty.EASY) return 0.3f;
        if (difficulty == Difficulty.NORMAL) return 0.6f;
        if (difficulty == Difficulty.HARD) return 0.9f;
        return 0;
    }

    @Unique
    private void dabaoSword$initCards() {
        if (getMainHandItem().isEmpty()) setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(dabaoSword$getMainCard(), (int)(3 * Math.random()) + 1));
        if (getOffhandItem().isEmpty()) setItemInHand(InteractionHand.OFF_HAND, new ItemStack(dabaoSword$getOffCard(), (int)(2 * Math.random()) + 1));
    }

    @Unique private Item dabaoSword$getMainCard() {
        if (new Random().nextFloat() > 0.33) {
            Item[] items = {ModItems.BINGLIANG_ITEM, ModItems.TOO_HAPPY_ITEM, ModItems.DISCARD, ModItems.FIRE_ATTACK, ModItems.JIEDAO, ModItems.WANJIAN, ModItems.TIESUO};
            int index = new java.util.Random().nextInt(items.length);
            return Arrays.stream(items).toList().get(index);
        }
        return ModItems.SHA;
    }

    @Unique private Item dabaoSword$getOffCard() {
        if (new Random().nextFloat() < 0.5) return ModItems.SHAN;
        return ModItems.PEACH;
    }
}
