package io.github.mmm.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.mmm.MMM;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;

public class KeyDefinitions {

    private static final Component CATEGORY = Component.translatable("gui." + MMM.MODID + ".controls.category.measurement");
    private static final Component SETTINGS = Component.translatable("gui." + MMM.MODID + ".controls.settings");
    private static final Component VISUALIZE = Component.translatable("gui." + MMM.MODID + ".controls.visualize");
    private static final Component DEVICE_MEASURE = Component.translatable("gui." + MMM.MODID + ".controls.device");
    private static final Component SURVEY_MODE = Component.translatable("gui." + MMM.MODID + ".controls.survey");
    private static final Component SURVEY_MEASURE = Component.translatable("gui." + MMM.MODID + ".controls.measure");

    public static final Lazy<KeyMapping> SETTINGS_MAPPING = Lazy.of(() -> new KeyMapping(
            SETTINGS.getString(),
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_Z,
            CATEGORY.getString()
    ));

    public static final Lazy<KeyMapping> VISUALIZE_MAPPING = Lazy.of(() -> new KeyMapping(
            VISUALIZE.getString(),
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_V,
            CATEGORY.getString()
    ));

    public static final Lazy<KeyMapping> DEVICE_MEASURE_MAPPING = Lazy.of(() -> new KeyMapping(
            DEVICE_MEASURE.getString(),
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_N,
            CATEGORY.getString()
    ));

    public static final Lazy<KeyMapping> SURVEY_MODE_MAPPING = Lazy.of(() -> new KeyMapping(
            SURVEY_MODE.getString(),
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_B,
            CATEGORY.getString()
    ));

    public static final Lazy<KeyMapping> SURVEY_MEASURE_MAPPING = Lazy.of(() -> new KeyMapping(
            SURVEY_MEASURE.getString(),
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_M,
            CATEGORY.getString()
    ));

}
