package com.blazelow.dawnoftimeextras;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class WaterQuadModel extends ForwardingBakedModel {
    private static RenderMaterial translucent;

    public WaterQuadModel(BakedModel wrapped) {
        this.wrapped = wrapped;
    }

    private static RenderMaterial translucent() {
        if (translucent == null) {
            Renderer renderer = RendererAccess.INSTANCE.getRenderer();
            if (renderer != null) {
                translucent = renderer.materialFinder().blendMode(BlendMode.TRANSLUCENT).find();
            }
        }
        return translucent;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                               Supplier<RandomSource> randomSupplier, RenderContext context) {
        RenderMaterial material = translucent();
        if (material == null) {
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            return;
        }
        context.pushTransform(quad -> {
            if (quad.colorIndex() == 0) {
                quad.material(material);
            }
            return true;
        });
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
    }
}
