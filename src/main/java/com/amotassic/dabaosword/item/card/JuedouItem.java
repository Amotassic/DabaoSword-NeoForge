package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.util.ModTools.*;

public class JuedouItem extends CardItem {
    public JuedouItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && selected && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(ModItems.REACH, 10,114,false,false,false));
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && entity.isAlive()) {
            user.addTag("juedou");
            if (entity instanceof Player player && hasItem(player, ModItems.WUXIE)) {
                cardUsePost(player, getItem(player, ModItems.WUXIE), null);
                voice(player, Sounds.WUXIE);
            } else {
                if (entity instanceof Player target) {
                    TagKey<Item> tag = Tags.SHA;
                    int userSha = count(user, tag);
                    int targetSha = count(target, tag);
                    if (userSha >= targetSha) {
                        target.addEffect(new MobEffectInstance(ModItems.COOLDOWN2,2,0,false,false,false));
                        target.invulnerableTime = 0;
                        target.hurt(user.damageSources().sonicBoom(user),5f);
                        target.displayClientMessage(Component.literal(user.getScoreboardName()).append(Component.translatable("dabaosword.juedou2")),false);
                    } else { target.addTag("juedou"); //防止决斗触发杀、闪
                        user.addEffect(new MobEffectInstance(ModItems.COOLDOWN2,2,0,false,false,false));
                        user.invulnerableTime = 0;
                        user.hurt(target.damageSources().sonicBoom(target),5f);
                        user.displayClientMessage(Component.translatable("dabaosword.juedou1"),false);
                        if (targetSha != 0) { //如果目标的杀比使用者的杀多，反击使用者，则目标减少一张杀
                            ItemStack sha = stackInTag(tag, target);
                            cardUsePost(target, sha, user);
                        }
                    }
                } else { entity.addTag("juedou");
                    entity.invulnerableTime = 0;
                    entity.hurt(user.damageSources().sonicBoom(user),5f);}
            }
            cardUsePost(user, stack, entity);
            voice(user, Sounds.JUEDOU);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
