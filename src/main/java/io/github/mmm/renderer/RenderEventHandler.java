package io.github.mmm.renderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEventHandler {

    public static final DeviceRenderer DEVICE_RENDERER = new DeviceRenderer();

    @SubscribeEvent
    public static void onRenderLayerPost(RenderLevelStageEvent event) {
        if(DEVICE_RENDERER.isVisualizing()) {
            DEVICE_RENDERER.render(event);
        }

    }

}