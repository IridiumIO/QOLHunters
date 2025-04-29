package io.iridium.qolhunters.mixin.dungeondoors;

import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.block.DungeonDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DungeonDoorBlock.class)
public class MixinDungeonDoorBlock {


    @Inject(method="use", at=@At("RETURN"))
    public void use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {

        QOLHunters.LOGGER.info("DungeonDoorBlock use method called");
//        DungeonDoorTileEntity tex = (DungeonDoorTileEntity) (state.getValue(HALF) == DoubleBlockHalf.LOWER ? world.getBlockEntity(pos) : world.getBlockEntity(pos.below()));
//
//        if(tex == null) {
//            QOLHunters.LOGGER.info("TileEntity is null");
//        }else{
//            QOLHunters.LOGGER.info("SUCCESS!");
//            QOLHunters.LOGGER.info(tex.getDifficulty().getDisplay().getString());
//        }


    }


}
