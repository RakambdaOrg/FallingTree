package fr.raksrinana.fallingtree.fabric;

import fr.raksrinana.fallingtree.fabric.config.BreakMode;
import fr.raksrinana.fallingtree.fabric.tree.breaking.ITreeBreakingHandler;
import fr.raksrinana.fallingtree.fabric.tree.breaking.InstantaneousTreeBreakingHandler;
import fr.raksrinana.fallingtree.fabric.tree.breaking.ShiftDownTreeBreakingHandler;
import fr.raksrinana.fallingtree.fabric.tree.builder.TreeBuilder;
import fr.raksrinana.fallingtree.fabric.tree.builder.TreeTooBigException;
import fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import static net.minecraft.Util.NIL_UUID;

public class BlockBreakHandler implements PlayerBlockBreakEvents.Before{
	@Override
	public boolean beforeBlockBreak(Level world, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity){
		if(FallingTree.config.getTreesConfiguration().isTreeBreaking() && !world.isClientSide()){
			if(FallingTreeUtils.isPlayerInRightState(player)){
				try{
					return TreeBuilder.getTree(world, blockPos).map(tree -> {
						BreakMode breakMode = FallingTree.config.getTreesConfiguration().getBreakMode();
						return getBreakingHandler(breakMode).breakTree(player, tree);
					}).orElse(true);
				}
				catch(TreeTooBigException e){
					player.sendMessage(new TranslatableComponent("chat.fallingtree.tree_too_big", FallingTree.config.getTreesConfiguration().getMaxSize()), NIL_UUID);
					return true;
				}
			}
		}
		return true;
	}
	
	public static ITreeBreakingHandler getBreakingHandler(BreakMode breakMode){
		if(breakMode == BreakMode.INSTANTANEOUS){
			return InstantaneousTreeBreakingHandler.getInstance();
		}
		return ShiftDownTreeBreakingHandler.getInstance();
	}
}
