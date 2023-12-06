package io.github.mmm.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.mmm.measurement.survey.objects.graph.Edge;
import io.github.mmm.measurement.survey.objects.graph.Vertex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;

import static io.github.mmm.MMM.SURVEY_CONTROLLER;

public class SurveyRenderer {

    private Tesselator tesselator;
    private BufferBuilder buffer;
    private VertexBuffer vertexBuffer;

    private Color edgeColor;
    private Color vertexColor;

    public SurveyRenderer() {
        this.edgeColor = Color.RED;
        this.vertexColor = Color.WHITE;
    }

    public void renderGraph(RenderLevelStageEvent event) {
        renderVerticesNew(event);
        beginRender();
        renderEdges();
        endRender(event);
    }

    private void renderVerticesNew(RenderLevelStageEvent event) {

        Font fontRenderer = Minecraft.getInstance().font;
        PoseStack matrixStack = event.getPoseStack();

        Vec3 viewerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Quaternionf viewerRot = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();

        for(Vertex vertex : SURVEY_CONTROLLER.getSurvey().getVertices()) {
            Vector3f position3f = vertex.getPosition();
            Vec3 position = new Vec3(position3f.x(), position3f.y(), position3f.z());
            String text = String.valueOf(vertex.getIndex());

            double distance = viewerPos.distanceTo(position);

            if (distance < 64) { // Only render if within a certain distance
                matrixStack.pushPose();
                matrixStack.translate(
                        position.x() - viewerPos.x(),
                        position.y() - viewerPos.y(),
                        position.z() - viewerPos.z()
                );
                matrixStack.mulPose(viewerRot);
                matrixStack.scale(-0.025F, -0.025F, 0.025F);
                MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                matrixStack.translate(-fontRenderer.width(text) / 2.0F, 0.0F, 0.0F);
                fontRenderer.drawInBatch(
                        text,
                        0,
                        0,
                        vertexColor.getRGB(),
                        true,
                        matrixStack.last().pose(),
                        buffer,
                        Font.DisplayMode.SEE_THROUGH,
                        0,
                        Mth.ceil(distance)
                );
                buffer.endBatch();
                matrixStack.popPose();
            }
        }
    }


    private void renderEdges() {
        for(Edge edge : SURVEY_CONTROLLER.getSurvey().getEdges()) {
            Vector3f start = edge.getStartVertex().getPosition();
            Vector3f end = edge.getEndVertex().getPosition();
            buffer.vertex(start.x, start.y, start.z).color(edgeColor.getRed(), edgeColor.getGreen(), edgeColor.getBlue(), 255).endVertex();
            buffer.vertex(end.x, end.y, end.z).color(edgeColor.getRed(), edgeColor.getGreen(), edgeColor.getBlue(), 255).endVertex();
        }
    }

    private void beginRender() {
        this.tesselator = Tesselator.getInstance();
        this.buffer = tesselator.getBuilder();
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
        RenderSystem.enableDepthTest();
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
    }

    private void endRender(RenderLevelStageEvent event) {
        this.vertexBuffer.bind();
        this.vertexBuffer.upload(buffer.end());
        Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        PoseStack matrix = event.getPoseStack();
        matrix.pushPose();
        matrix.translate(-view.x, -view.y, -view.z);
        ShaderInstance shader = GameRenderer.getPositionColorShader();
        this.vertexBuffer.drawWithShader(matrix.last().pose(), event.getProjectionMatrix(), shader);
        matrix.popPose();
        VertexBuffer.unbind();
        RenderSystem.disableDepthTest();
    }

}
