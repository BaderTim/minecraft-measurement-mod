package io.github.mmm;

import com.mojang.logging.LogUtils;
import io.github.mmm.measurement.MeasurementController;
import io.github.mmm.modconfig.Config;
import io.github.mmm.renderer.GraphBlock;
import io.github.mmm.renderer.GraphBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MMM.MODID)
public class MMM {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mmm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Create Measure object
    public static final MeasurementController MEASUREMENT_CONTROLLER = new MeasurementController();

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> GRAPH_BLOCK = BLOCKS.register(
            "graph_block",
            () -> new GraphBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(-1.0F, 3600000.0F).noLootTable()));

    public static final RegistryObject<BlockEntityType<GraphBlockEntity>> GRAPH_BLOCK_ENTITY = BLOCK_ENTITY_TYPE_REGISTER.register(
            "graph_block",
            () -> BlockEntityType.Builder.of(GraphBlockEntity::new, GRAPH_BLOCK.get()).build(null));

    public MMM() {
        // Register Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC, "mmm-client-config.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

}
