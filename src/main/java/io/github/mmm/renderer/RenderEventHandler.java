package io.github.mmm.renderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.MODID;
import static io.github.mmm.MMM.SURVEY_CONTROLLER;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEventHandler {

    public static final DeviceRenderer DEVICE_RENDERER = new DeviceRenderer();
    public static final SurveyRenderer SURVEY_RENDERER = new SurveyRenderer();

    @SubscribeEvent
    public static void onRenderLayerPost(RenderLevelStageEvent event) {
        if(DEVICE_RENDERER.isVisualizing()) {
            DEVICE_RENDERER.render(event);
        }
        if(SURVEY_CONTROLLER.isCurrentlySurveying()) {
            SURVEY_RENDERER.renderGraph(event);
        }
    }

}