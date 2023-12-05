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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Vector3f;

import java.awt.*;

import static io.github.mmm.MMM.SURVEY_CONTROLLER;

public class SurveyRenderer {

    private Tesselator tesselator;
    private BufferBuilder buffer;
    private VertexBuffer vertexBuffer;

    private Color edgeColor;

    public SurveyRenderer() {
        this.edgeColor = Color.RED;
    }

    public void renderGraph(RenderLevelStageEvent event) {
        beginRender();
        //renderVertices(event);
        renderEdges();
        endRender(event);
    }

    private void renderVertices(RenderLevelStageEvent event) {
        // TODO: red dot for current vertex + index as text

        Font fontRenderer = Minecraft.getInstance().font;
        PoseStack matrix = event.getPoseStack();

        Vec3 vector3d = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        double xView = vector3d.x();
        double yView = vector3d.y();
        double zView = vector3d.z();

        for(Vertex vertex : SURVEY_CONTROLLER.getSurvey().getVertices()) {
            Vector3f position = vertex.getPosition();
            String index = String.valueOf(vertex.getIndex());

            matrix.pushPose();
            matrix.translate(position.x() - xView, position.y() - yView, position.z() - zView);

            matrix.translate(-fontRenderer.width(index) * 0.5, 0, 0);

            MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            fontRenderer.drawInBatch(index, 0, 0, 1, false, matrix.last().pose(), buffer, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            buffer.endBatch();
            matrix.popPose();
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

    private float getScale(
            final double maxLen)
    {
        final double maxFontSize = 0.04;
        final double minFontSize = 0.004;

        final double delta = Math.min(1.0, maxLen / 4.0);
        double scale = maxFontSize * delta + minFontSize * (1.0 - delta);

        if (maxLen < 0.25)
        {
            scale = minFontSize;
        }

        return (float) Math.min(maxFontSize, scale);
    }

}
