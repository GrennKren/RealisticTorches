package com.chaosthedude.realistictorches.client.renderer;

import com.chaosthedude.realistictorches.blockentity.RealisticCampfireBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;

public class RealisticCampfireRenderer implements BlockEntityRenderer<RealisticCampfireBlockEntity> {

    private final ItemRenderer itemRenderer;

    public RealisticCampfireRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(
            RealisticCampfireBlockEntity entity,
            float tickDelta,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay
    ) {
        Direction direction = entity.getBlockState().getValue(CampfireBlock.FACING);
        NonNullList<ItemStack> items = entity.getItems();

        for (int i = 0; i < items.size(); ++i) {
            ItemStack itemstack = items.get(i);
            if (!itemstack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.44921875, 0.5);
                Direction direction1 = Direction.from2DDataValue((i + direction.get2DDataValue()) % 4);
                float f = -direction1.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(f));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.translate(-0.3125, -0.3125, 0.0);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderStatic(
                        itemstack,
                        ItemDisplayContext.FIXED,
                        light,
                        overlay,
                        poseStack,
                        buffer,
                        entity.getLevel(),
                        (int) entity.getBlockPos().asLong()
                );
                poseStack.popPose();
            }
        }
    }
}