package com.amotassic.dabaosword.mixin.client;

import com.amotassic.dabaosword.api.Card;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {
    @Shadow @Final private PoseStack pose;

    @Shadow abstract void blit(ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"))
    public void drawItemInSlot(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        var sr = ModTools.getSuitAndRank(stack);
        if (sr != null) {
            Card.Suits s = sr.getA(); Card.Ranks r = sr.getB();
            ResourceLocation suit = ResourceLocation.fromNamespaceAndPath("dabaosword", "textures/item/suit/" + getSuitName(s) + ".png");
            ResourceLocation rank = ResourceLocation.fromNamespaceAndPath("dabaosword", "textures/item/rank2/" + getRankName(s, r) + ".png");
            if (stack.getCount() == 1) this.pose.translate(0.0f, 0.0f, 200.0f);
            blit(suit, x, x + 5, y, y + 5, 0, 64, 64, 0, 0, 64, 64);
            blit(rank, x + 5, x + 11, y, y + 5, 0, 64, 64, 0, 0, 64, 64);
        }
    }

    @ModifyArgs(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"))
    private void drawItem(Args args) {
        ItemStack stack = args.get(0);
        if (ModTools.isCard(stack) || stack.is(ModItems.GAIN_CARD)) {
            String path = "card/" + stack.getItem().toString().split(":")[1];
            ModelResourceLocation modelId = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath("dabaosword", path));
            BakedModel model = minecraft.getItemRenderer().getItemModelShaper().getModelManager().getModel(modelId);
            args.set(7, model);
        }
    }

    @SuppressWarnings("all")
    @Unique private String getSuitName(Card.Suits s) {
        return switch (s) {
            case Heart -> "heart";
            case Diamond -> "diamond";
            case Spade -> "spade_w";
            case Club -> "club_w";
        };
    }

    @SuppressWarnings("all")
    @Unique private String getRankName(Card.Suits s, Card.Ranks r) {
        return switch (s) {
            case Heart, Diamond -> switch (r) {
                case Ace -> "ar";
                case Two -> "2r";
                case Three -> "3r";
                case Four -> "4r";
                case Five -> "5r";
                case Six -> "6r";
                case Seven -> "7r";
                case Eight -> "8r";
                case Nine -> "9r";
                case Ten -> "10r";
                case Jack -> "jr";
                case Queen -> "qr";
                case King -> "kr";
            };
            case Spade, Club -> switch (r) {
                case Ace -> "ab";
                case Two -> "2b";
                case Three -> "3b";
                case Four -> "4b";
                case Five -> "5b";
                case Six -> "6b";
                case Seven -> "7b";
                case Eight -> "8b";
                case Nine -> "9b";
                case Ten -> "10b";
                case Jack -> "jb";
                case Queen -> "qb";
                case King -> "kb";
            };
        };
    }
}
