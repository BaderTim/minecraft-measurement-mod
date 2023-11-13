package io.github.mmm.keymappings;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.*;
import static io.github.mmm.keymappings.KeyDefinitions.MEASURE_MAPPING;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyEvents {

    static Tesselator tesselator = Tesselator.getInstance();
    static BufferBuilder bufferBuilder = tesselator.getBuilder();

    // Listen to Key Bindings
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Only call code once as the tick event is called twice every tick
        if (event.phase == TickEvent.Phase.END) {
            if (MEASURE_MAPPING.get().consumeClick()) {
                LOGGER.info("MEASURE_START_STOP_MAPPING is pressed");
                if(MEASUREMENT_MANAGER.isCurrentlyMeasuring()) {
                    MEASUREMENT_MANAGER.stopMeasure();
                } else {
                    MEASUREMENT_MANAGER.startMeasure();
                }
            }

            if(MEASUREMENT_MANAGER.isCurrentlyMeasuring()) {
                // reference: https://github.com/cabaletta/baritone/blob/e183dcba1707c991fea1ba55be052dc3ee5d71a6/src/main/java/baritone/utils/IRenderer.java#L149

                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO
                );

                RenderSystem.depthMask(false);
                RenderSystem.disableCull();
                RenderSystem.lineWidth(1f);

                RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);

                bufferBuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

                float x1 = 0;
                float y1 = 0;
                float z1 = 0;

                float x2 = 0;
                float y2 = 2;
                float z2 = 2;

                final double dx = x2 - x1;
                final double dy = y2 - y1;
                final double dz = z2 - z1;

                final double invMag = 1.0 / Math.sqrt(dx * dx + dy * dy + dz * dz);
                final float nx = (float) (dx * invMag);
                final float ny = (float) (dy * invMag);
                final float nz = (float) (dz * invMag);



                PoseStack poseStack = RenderSystem.getModelViewStack();
                Matrix4f matrix4f = poseStack.last().pose();
                Matrix3f matrix3f = poseStack.last().normal();


                bufferBuilder.vertex(matrix4f, x1, y1, z1).color(255, 0, 0, 255).normal(matrix3f, nx, ny, nz).endVertex();
                bufferBuilder.vertex(matrix4f, x2, y2, z2).color(255, 0, 0, 255).normal(matrix3f, nx, ny, nz).endVertex();

                tesselator.end();

                RenderSystem.enableCull();
                RenderSystem.depthMask(true);
                RenderSystem.disableBlend();
            }
        }
    }

}
