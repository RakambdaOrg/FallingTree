package fr.rakambda.fallingtree.common.tree.breaking;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.AbortedResult;
import fr.rakambda.fallingtree.common.tree.IBreakAttemptResult;
import fr.rakambda.fallingtree.common.tree.SuccessResult;
import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.tree.TreePartType;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import fr.rakambda.fallingtree.common.wrapper.IRandomSource;
import fr.rakambda.fallingtree.common.wrapper.IServerLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Log4j2
@RequiredArgsConstructor
public class FallingAnimationTreeBreakingHandler implements ITreeBreakingHandler{
	public record FallingAnimationTreeBreakingConfig(
			boolean dropLogsAsItems,
			boolean dropLeavesAsItems,
			Function<IRandomSource, Double> vx, Function<IRandomSource, Double> vy, Function<IRandomSource, Double> vz
	){
		public static FallingAnimationTreeBreakingConfig withRandomSpread(boolean dropLogsAsItems, boolean dropLeavesAsItems){
			return new FallingAnimationTreeBreakingConfig(
					dropLogsAsItems,
					dropLeavesAsItems,
					rng -> (rng.nextDouble() - 0.5) * 0.4,
					rng -> 0D,
					rng -> (rng.nextDouble() - 0.5) * 0.4
			);
		}
		
		public static FallingAnimationTreeBreakingConfig straightDown(boolean dropLogsAsItems, boolean dropLeavesAsItems){
			return new FallingAnimationTreeBreakingConfig(
					dropLogsAsItems,
					dropLeavesAsItems,
					rng -> 0D,
					rng -> 0D,
					rng -> 0D
			);
		}
	}
	
	private final static Map<FallingAnimationTreeBreakingConfig, FallingAnimationTreeBreakingHandler> INSTANCE = new ConcurrentHashMap<>();
	
	private final FallingTreeCommon<?> mod;
	private final FallingAnimationTreeBreakingConfig config;
	private final LeafForceBreaker leafForceBreaker;
	
	@Override
	@NonNull
	public IBreakAttemptResult breakTree(boolean isCancellable, @NonNull IPlayer player, @NonNull Tree tree) throws BreakTreeTooBigException, BreakTreeTooSmallException{
		var tool = player.getMainHandItem();
		var level = tree.getLevel();
		if(!(level instanceof IServerLevel serverLevel)){
			return AbortedResult.NOT_SERVER;
		}
		var toolHandler = new ToolDamageHandler(tool,
				mod.getConfiguration().getTools().getDamageMultiplicand(),
				mod.getConfiguration().getTools().getDurabilityMode(),
				tree.getBreakableCount(),
				mod.getConfiguration().getTrees().getMinSize(),
				mod.getConfiguration().getTrees().getMaxSize(),
				mod.getConfiguration().getTrees().getMaxSizeAction(),
				mod.getConfiguration().getTools().getDamageRounding());
		
		if(toolHandler.isPreserveTool()){
			log.info("Didn't break tree at {} as {}'s tool was about to break", tree.getHitPos(), player);
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.prevented_break_tool"));
			return SuccessResult.DO_NOT_CANCEL;
		}
		
		var scannedLeaves = new LinkedList<IBlockPos>();
		var wantToBreakCount = Math.min(tree.getBreakableCount(), toolHandler.getMaxBreakCount());
		var lootHandler = new LootHandler(wantToBreakCount, mod.getConfiguration().getTrees().getTrunkLootPercentage());
		var brokenCount = 0;
		var breakablePartsLeft = wantToBreakCount;
		for(var part : tree.getParts().stream().sorted(mod.getConfiguration().getTrees().getBreakOrder().getComparator()).toList()){
			if(part.treePartType().isBreakable()){
				if(breakablePartsLeft == 0){
					break;
				}
				breakablePartsLeft--;
			}
			else if(part.treePartType() != TreePartType.LOG_START || breakablePartsLeft == 0){
				continue;
			}
			
			var logBlockPos = part.blockPos();
			var logState = level.getBlockState(logBlockPos);
			
			if(!tree.getHitPos().equals(logBlockPos) && !mod.checkCanBreakBlock(level, logBlockPos, logState, player)){
				continue;
			}
			
			player.awardItemUsed(tool.getItem());
			if(config.dropLogsAsItems && (!player.isCreative() || mod.getConfiguration().isLootInCreative())){
				logState.getBlock().playerDestroy(level, player, logBlockPos, logState, level.getBlockEntity(logBlockPos), tool, !part.treePartType().isIncludeInTree() || lootHandler.breakNewTrunk());
			}
			
			var random = level.getRandom();
			serverLevel.fallBlock(
					logBlockPos,
					!config.dropLogsAsItems,
					config.vx.apply(random),
					config.vy.apply(random),
					config.vx.apply(random)
			);
			
			fallLeaf(scannedLeaves, player, serverLevel, 5, logBlockPos.below());
			fallLeaf(scannedLeaves, player, serverLevel, 5, logBlockPos.north());
			fallLeaf(scannedLeaves, player, serverLevel, 5, logBlockPos.east());
			fallLeaf(scannedLeaves, player, serverLevel, 5, logBlockPos.south());
			fallLeaf(scannedLeaves, player, serverLevel, 5, logBlockPos.west());
			fallLeaf(scannedLeaves, player, serverLevel, 5, logBlockPos.above());
			
			if(part.treePartType().isBreakable()){
				brokenCount++;
			}
		}
		
		var toolDamage = toolHandler.getActualDamage(brokenCount) - 1;
		if(toolDamage > 0){
			tool.damage(toolDamage, player);
		}
		
		if(brokenCount >= wantToBreakCount){
			leafForceBreaker.forceBreakDecayLeaves(player, tree, level);
		}
		if(player.isCreative() && mod.getConfiguration().isLootInCreative()){
			tree.getStart().ifPresent(part -> part.blockState().getBlock().playerDestroy(level, player, tree.getHitPos(), part.blockState(), part.blockEntity(), tool, lootHandler.breakNewTrunk()));
		}
		return SuccessResult.DO_NOT_CANCEL;
	}
	
	private void fallLeaf(LinkedList<IBlockPos> scannedLeaves, @NonNull IPlayer player, @NonNull IServerLevel serverLevel, int distance, @NonNull IBlockPos blockPos){
		if(!mod.getConfiguration().getTrees().isLeavesBreaking()){
			return;
		}
		if(distance == 0){
			return;
		}
		
		fallLeaf(scannedLeaves, player, serverLevel, distance - 1, blockPos.below());
		
		if(scannedLeaves.contains(blockPos)){
			return;
		}
		scannedLeaves.add(blockPos);
		
		var blockState = serverLevel.getBlockState(blockPos);
		if(!mod.isLeafBlock(blockState.getBlock())){
			return;
		}
		
		if(config.dropLeavesAsItems && (!player.isCreative() || mod.getConfiguration().isLootInCreative())){
			blockState.getBlock().playerDestroy(serverLevel, player, blockPos, blockState, serverLevel.getBlockEntity(blockPos), mod.getEmptyItemStack(), true);
		}
		var random = serverLevel.getRandom();
		serverLevel.fallBlock(
				blockPos,
				!config.dropLeavesAsItems,
				config.vx.apply(random),
				config.vy.apply(random),
				config.vx.apply(random)
		);
		
		fallLeaf(scannedLeaves, player, serverLevel, distance - 1, blockPos.north());
		fallLeaf(scannedLeaves, player, serverLevel, distance - 1, blockPos.east());
		fallLeaf(scannedLeaves, player, serverLevel, distance - 1, blockPos.south());
		fallLeaf(scannedLeaves, player, serverLevel, distance - 1, blockPos.west());
		fallLeaf(scannedLeaves, player, serverLevel, distance - 1, blockPos.above());
	}
	
	@NonNull
	public static FallingAnimationTreeBreakingHandler getInstance(@NonNull FallingTreeCommon<?> mod, FallingAnimationTreeBreakingConfig config){
		return INSTANCE.computeIfAbsent(config, key -> new FallingAnimationTreeBreakingHandler(mod, config, new LeafForceBreaker(mod)));
	}
}
