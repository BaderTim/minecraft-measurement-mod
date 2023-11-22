package io.github.mmm.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;

import static io.github.mmm.MMM.MEASUREMENT_CONTROLLER;
import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GraphRendererOld implements IRenderer {

    @SubscribeEvent
    public static void onRenderLayerPost(TickEvent.RenderTickEvent event) {
        // Only call code once as the tick event is called twice every tick
        if (event.phase == TickEvent.Phase.END) {

            if(MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {

            }

            if (false && MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {
                PoseStack stack = RenderSystem.getModelViewStack();
                IRenderer.startLines(Color.RED, 1.0F, 2.0F, true);
                IRenderer.emitLine(stack, 1, 0, 0, 0, 2, 0);
                IRenderer.endLines(true);
                System.out.println("rendering graph");
            }

            if (false && MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {
                Vector3f view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f();
                Matrix4f projectionMatrix = new Matrix4f();
                Minecraft.getInstance().gameRenderer.getMainCamera().rotation().get(projectionMatrix);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.getBuilder();
                VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
                buffer.vertex(0, 0, 0).color(1f, 1f, 1f, 1f).endVertex();
                buffer.vertex(100, 200, 100).color(1f, 1f, 1f, 1f).endVertex();
                vertexBuffer.bind();
                vertexBuffer.upload(buffer.end());

                PoseStack matrix = RenderSystem.getModelViewStack();
                matrix.pushPose();
                matrix.translate(-view.x, -view.y, -view.z);
                var shader = GameRenderer.getPositionColorShader();
                vertexBuffer.drawWithShader(matrix.last().pose(), projectionMatrix, shader);
                matrix.popPose();

                VertexBuffer.unbind();

                System.out.println("rendering graph");
            }

            if(false && MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tesselator.getBuilder();
                RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
                RenderSystem.lineWidth(5.0f);

                Vector3f viewPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f();
                Vector3f actualPos = new Vector3f(0, 0, 0);

                Vector3f projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
                PoseStack stack = RenderSystem.getModelViewStack();
                stack.pushPose();

                stack.translate(actualPos.x - viewPosition.x, actualPos.y - viewPosition.y, actualPos.z - viewPosition.z);

                Matrix4f matrix4f = stack.last().pose();
                stack.translate(projectedView.x(), projectedView.y(), projectedView.z());
                bufferbuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
                bufferbuilder.vertex(matrix4f,0, 0, 0).color(0, 255, 0, 255).endVertex();
                bufferbuilder.vertex(matrix4f, 0, 2, 2).color(0, 255, 0, 255).endVertex();
                tesselator.end();
                stack.popPose();

                System.out.println("rendering graph");
            }
        }
    }

}
