package io.github.mmm.measurement.survey;


import io.github.mmm.MMM;
import io.github.mmm.measurement.survey.objects.Survey;
import io.github.mmm.measurement.survey.objects.graph.Edge;
import io.github.mmm.measurement.survey.objects.graph.Vertex;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.github.mmm.MMM.MMM_ROOT_PATH;
import static io.github.mmm.Utils.saveStringToFile;

public class SurveyController {

    private Boolean currentlySurveying;
    private String savePath;

    private Survey survey;

    public SurveyController() {
        this.currentlySurveying = false;
    }

    public void startSurvey() {
        this.survey = new Survey();

        String startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
        this.savePath = "survey_measurements/" + startTime;
        try {
            Files.createDirectories(Paths.get(MMM_ROOT_PATH + this.savePath));
        } catch (Exception e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }

        saveStringToFile("index;xPosition;yPosition;zPosition", this.savePath, "vertices.csv");
        saveStringToFile("index;startVertexIndex;endVertexIndex", this.savePath, "edges.csv");

        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".survey.start"), false);

        this.currentlySurveying = true;
    }

    public void stopSurvey() {
        saveSurvey();
        this.survey = null;
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".survey.stop"), false);
        this.currentlySurveying = false;
    }

    public void measure() {
        if (!this.currentlySurveying) return;

        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        assert level != null && player != null;

        Vec3 startPos = player.getEyePosition();
        Vec3 endPos = startPos.add(player.getLookAngle().scale(256));

        ClipContext context = new ClipContext(
                startPos,
                endPos,
                ClipContext.Block.OUTLINE, // check block outlines
                ClipContext.Fluid.NONE, // ignore fluids
                player
        );
        Vector3f targetPosition = level.clip(context).getLocation().toVector3f();
        Vertex newVertex = new Vertex(targetPosition, this.survey.getVertices().size());
        this.survey.addVertex(newVertex);
        if (!this.survey.getVertices().isEmpty()) { // if not first vertex
            Vertex positionVertex = this.survey.getVertices().get(this.survey.getPositionVertexIndex());
            Edge newEdge = new Edge(positionVertex, newVertex, this.survey.getEdges().size());
            this.survey.addEdge(newEdge);
            this.survey.setPositionVertexIndex(newVertex.getIndex());
        }
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".survey.measure"), false);
    }

    private void saveSurvey() {
        String verticesString = "";
        for (Vertex vertex : this.survey.getVertices()) {
            verticesString += vertex.getIndex() + ";" + vertex.getPosition().x + ";" + vertex.getPosition().y + ";" + vertex.getPosition().z + "\n";
        }
        String edgesString = "";
        for (Edge edge : this.survey.getEdges()) {
            edgesString += edge.getIndex() + ";" + edge.getStartVertex().getIndex() + ";" + edge.getEndVertex().getIndex() + "\n";
        }
        saveStringToFile(verticesString, this.savePath, "vertices.csv");
        saveStringToFile(edgesString, this.savePath, "edges.csv");
    }

    public Boolean isCurrentlySurveying() {
        return this.currentlySurveying;
    }

    public Survey getSurvey() {
        return this.survey;
    }
}
