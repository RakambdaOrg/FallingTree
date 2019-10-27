package fr.raksrinana.fallingtree.tree;

import fr.raksrinana.fallingtree.FallingTree;
import fr.raksrinana.fallingtree.config.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class TreeHandler{
	public static boolean isTreeBlock(@Nonnull IWorld world, @Nonnull BlockPos blockPos){
		final Block block = world.getBlockState(blockPos).getBlock();
		return Config.COMMON.getWhitelistedLogs().anyMatch(log -> log.equals(block));
	}
	
	@Nonnull
	public static Optional<Tree> getTree(@Nonnull IWorld world, @Nonnull BlockPos blockPos){
		if(!isTreeBlock(world, blockPos)){
			return Optional.empty();
		}
		Queue<BlockPos> toAnalyzePos = new LinkedList<>();
		Set<BlockPos> analyzedPos = new HashSet<>();
		Block logBlock = world.getBlockState(blockPos).getBlock();
		Tree tree = new Tree(world, blockPos);
		toAnalyzePos.add(blockPos);
		while(!toAnalyzePos.isEmpty()){
			FallingTree.LOGGER.debug("To analyze: {}", toAnalyzePos.size());
			BlockPos analyzingPos = toAnalyzePos.remove();
			tree.addLog(analyzingPos);
			analyzedPos.add(analyzingPos);
			Collection<BlockPos> nearbyPos = neighborLogs(world, logBlock, analyzingPos, analyzedPos);
			nearbyPos.removeAll(analyzedPos);
			toAnalyzePos.addAll(nearbyPos.stream().filter(pos -> !toAnalyzePos.contains(pos)).collect(Collectors.toList()));
		}
		return Optional.of(tree);
	}
	
	@Nonnull
	private static Collection<BlockPos> neighborLogs(@Nonnull IWorld world, @Nonnull Block logBlock, @Nonnull BlockPos blockPos, @Nonnull Collection<BlockPos> analyzedPos){
		List<BlockPos> neighborLogs = new LinkedList<>();
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				for(int y = -1; y <= 1; y++){
					BlockPos pos = blockPos.add(x, y, z);
					if(!analyzedPos.contains(pos) && isSameLog(world, pos, logBlock)){
						neighborLogs.add(blockPos.add(x, y, z));
					}
				}
			}
		}
		neighborLogs.addAll(analyzedPos);
		return neighborLogs;
	}
	
	private static boolean isSameLog(@Nonnull IWorld world, @Nonnull BlockPos blockPos, @Nullable Block logBlock){
		return world.getBlockState(blockPos).getBlock().equals(logBlock);
	}
	
	public static void destroy(@Nonnull Tree tree, @Nonnull PlayerEntity player, @Nonnull ItemStack tool){
		tree.getLogs().forEach(logBlock -> {
			if(!Config.COMMON.ignoreDurabilityLoss.get() && tree.getWorld() instanceof World){
				tool.onBlockDestroyed((World) tree.getWorld(), tree.getWorld().getBlockState(logBlock), logBlock, player);
			}
			tree.getWorld().destroyBlock(logBlock, true);
		});
	}
	
	public static boolean canPlayerBreakTree(@Nonnull PlayerEntity player){
		final ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
		return Config.COMMON.getWhitelistedTools().anyMatch(tool -> tool.equals(heldItem.getItem()));
	}
}
