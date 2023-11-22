package io.github.mmm.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class GraphBlockRenderer implements BlockEntityRenderer<GraphBlockEntity> {

    @Override
    public void render(GraphBlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        System.out.println("rendering");
    }
}
