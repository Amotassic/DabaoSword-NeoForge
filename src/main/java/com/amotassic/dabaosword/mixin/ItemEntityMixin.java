package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> entityType, Level level) {super(entityType, level);}

    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract void setItem(ItemStack stack);

    @Shadow public abstract void setNoPickUpDelay();

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci) {
        if (ModTools.isCard(this.getItem())) this.setNoPickUpDelay();

        ItemStack stack = this.getItem();
        if (stack.getItem() == Items.ARROW && stack.getCount() == 64) {
            var entity = dabaoSword$getClosestEntity(this);
            if (entity instanceof ItemEntity item && item.getItem().getItem() == Items.BOW) {
                item.setItem(new ItemStack(ModItems.ARROW_RAIN));
                this.discard();
            }
        }

        if (stack.getItem() == Items.EMERALD && stack.getCount() == 64) {
            var entity = dabaoSword$getClosestEntity(this);
            if (entity instanceof Villager villager && villager.getVillagerData().getProfession() == VillagerProfession.NITWIT) {
                this.setItem(new ItemStack(ModItems.GIFT_BOX, 1));
            }
        }
    }

    @Unique private @Nullable Entity dabaoSword$getClosestEntity(ItemEntityMixin entity) {
        if (entity.level() instanceof ServerLevel world) {
            AABB box = new AABB(entity.getOnPos()).inflate(0.2);
            List<Entity> entities = world.getEntitiesOfClass(Entity.class, box, entity1 -> entity1 != entity);
            if (!entities.isEmpty()) {
                Map<Float, Entity> map = new HashMap<>();
                for (var e : entities) {map.put(e.distanceTo(entity), e);}
                float min = Collections.min(map.keySet());
                return map.values().stream().toList().get(map.keySet().stream().toList().indexOf(min));
            }
        }
        return null;
    }
}
