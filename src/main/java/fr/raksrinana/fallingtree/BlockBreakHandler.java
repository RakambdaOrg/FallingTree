package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.BreakMode;
import fr.raksrinana.fallingtree.tree.breaking.ITreeBreakingHandler;
import fr.raksrinana.fallingtree.tree.breaking.InstantaneousTreeBreakingHandler;
import fr.raksrinana.fallingtree.tree.breaking.ShiftDownTreeBreakingHandler;
import fr.raksrinana.fallingtree.tree.builder.TreeBuilder;
import fr.raksrinana.fallingtree.tree.builder.TreeTooBigException;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import static fr.raksrinana.fallingtree.config.BreakMode.INSTANTANEOUS;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.isPlayerInRightState;
import static net.minecraft.util.Util.NIL_UUID;

public class BlockBreakHandler implements net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.Before{
	@Override
	public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity){
		if(FallingTree.config.getTreesConfiguration().isTreeBreaking() && !world.isClient()){
			if(isPlayerInRightState(player)){
				try{
					return TreeBuilder.getTree(world, blockPos).map(tree -> {
						BreakMode breakMode = FallingTree.config.getTreesConfiguration().getBreakMode();
						return getBreakingHandler(breakMode).breakTree(player, tree);
					}).orElse(true);
				}
				catch(TreeTooBigException e){
					player.sendSystemMessage(new TranslatableText("chat.falling_tree.tree_too_big", FallingTree.config.getTreesConfiguration().getMaxSize()), NIL_UUID);
					return true;
				}
			}
		}
		return true;
	}
	
	public static ITreeBreakingHandler getBreakingHandler(BreakMode breakMode){
		if(breakMode == INSTANTANEOUS){
			return InstantaneousTreeBreakingHandler.getInstance();
		}
		return ShiftDownTreeBreakingHandler.getInstance();
	}
}
