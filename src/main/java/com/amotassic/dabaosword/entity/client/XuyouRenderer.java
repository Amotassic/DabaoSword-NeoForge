package com.amotassic.dabaosword.entity.client;

import com.amotassic.dabaosword.entity.XuyouEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class XuyouRenderer extends HumanoidMobRenderer<XuyouEntity, XuyouModel<XuyouEntity>> {
    public XuyouRenderer(EntityRendererProvider.Context context) {
        this(context, ModModelLayers.XUYOU, ModelLayers.PLAYER_INNER_ARMOR, ModelLayers.PLAYER_OUTER_ARMOR);
    }

    public XuyouRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation layer, ModelLayerLocation legArmorLayer, ModelLayerLocation bodyArmorLayer) {
        super(ctx, new XuyouModel<>(ctx.bakeLayer(layer)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this, new XuyouModel<>(ctx.bakeLayer(legArmorLayer)), new XuyouModel<>(ctx.bakeLayer(bodyArmorLayer)), ctx.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(XuyouEntity xuyouEntity) {
        return ResourceLocation.fromNamespaceAndPath("dabaosword" ,"textures/entity/xuyou.png");
    }
}
