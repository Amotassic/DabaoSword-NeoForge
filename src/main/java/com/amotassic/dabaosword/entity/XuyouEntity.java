package com.amotassic.dabaosword.entity;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.amotassic.dabaosword.api.event.CardEvents.cardDiscard;
import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class XuyouEntity extends Monster implements RangedAttackMob {
    public static final EntityType<XuyouEntity> TYPE = EntityType.Builder.<XuyouEntity>of(XuyouEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build("xuyou");

    protected XuyouEntity(EntityType<? extends Monster> entityType, Level level) {super(entityType, level);}

    public XuyouEntity(Level level) {this(TYPE, level);}

    private int bbcd = 0;
    private int bbTimes = 0;
    private final RangedAttackGoal bb = new RangedAttackGoal(this, 1.25,10, 7f);

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new UseCardGoal(this));
        this.goalSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_SPEED, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3f);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("bbcd", bbcd);
        nbt.putInt("bbTimes", bbTimes);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        bbcd = nbt.getInt("bbcd");
        bbTimes = nbt.getInt("bbTimes");
    }

    @Override
    protected void customServerAiStep() {
        if (level().getGameTime() % 200 == 0) draw(this);
        if (bbTimes >= 5) {
            goalSelector.removeGoal(bb);
            bbTimes = 0; bbcd = 150;
        }
        if (bbcd > 0) bbcd--; else goalSelector.addGoal(2, bb);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target.distanceTo(this) > 10) return false;
        return super.canAttack(target);
    }

    @Override
    protected SoundEvent getDeathSound() {return Sounds.XUYOU;}

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {return source.type().effects().sound();}

    @Override
    public void die(DamageSource damageSource) {
        for (var stack : allTrinkets(this)) {
            if(isCard(stack)) cardDiscard(this, stack, stack.getCount(), true);
        }
        super.die(damageSource);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float v) {
        bbTimes++;
        target.invulnerableTime = 0;
        target.hurt(getDamageSource(this, DamageTypes.GENERIC), 2);
        voice(this, Sounds.BBJI);
    }
}
