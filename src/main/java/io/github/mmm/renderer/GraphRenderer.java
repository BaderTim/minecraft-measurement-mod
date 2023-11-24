package io.github.mmm.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.DEVICE_CONTROLLER;
import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GraphRenderer {

    @SubscribeEvent
    public static void onRenderLayerPost(RenderLevelStageEvent event) {

        if(DEVICE_CONTROLLER.isCurrentlyMeasuring()) {
            Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

            RenderSystem.enableDepthTest();

            var tesselator = Tesselator.getInstance();
            var buffer = tesselator.getBuilder();


            VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            buffer.vertex(0, 0, 0).color(1f, 1f, 1f, 1f).endVertex();
            buffer.vertex(10, 10, 10).color(1f, 1f, 1f, 1f).endVertex();
            vertexBuffer.bind();
            vertexBuffer.upload(buffer.end());

            PoseStack matrix = event.getPoseStack();
            matrix.pushPose();
            matrix.translate(-view.x, -view.y, -view.z);
            var shader = GameRenderer.getPositionColorShader();
            vertexBuffer.drawWithShader(matrix.last().pose(), event.getProjectionMatrix(), shader);
            matrix.popPose();

            VertexBuffer.unbind();

        }

    }

}
