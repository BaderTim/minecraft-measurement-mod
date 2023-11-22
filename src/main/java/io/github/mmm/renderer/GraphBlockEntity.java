package io.github.mmm.renderer;

import io.github.mmm.MMM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GraphBlockEntity extends BlockEntity {

    public GraphBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(MMM.GRAPH_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }
}
