package com.amotassic.dabaosword.entity.client;

import com.amotassic.dabaosword.entity.XuyouEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class XuyouModel<T extends XuyouEntity> extends HumanoidModel<T> {
    public XuyouModel(ModelPart root) {super(root);}

    public static LayerDefinition getTexturedModelData() {
        return LayerDefinition.create(createMesh(CubeDeformation.NONE, 0), 64, 64);
    }
}
