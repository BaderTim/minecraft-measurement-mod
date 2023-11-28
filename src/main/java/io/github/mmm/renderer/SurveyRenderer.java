package io.github.mmm.renderer;

import com.mojang.blaze3d.vertex.*;
import io.github.mmm.measurement.survey.objects.graph.Edge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
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
        this.edgeColor = Color.WHITE;
    }

    public void renderGraph(RenderLevelStageEvent event) {
        beginRender();
        renderVertices();
        renderEdges();
        endRender(event);
    }

    private void renderVertices() {
        // TODO: red dot for current vertex + index as text
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
    }

}
