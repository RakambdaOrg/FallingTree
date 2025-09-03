package fr.rakambda.fallingtree.common.tree.breaking;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.IBreakAttemptResult;
import fr.rakambda.fallingtree.common.tree.SuccessResult;
import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.tree.TreePart;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import static java.util.Objects.isNull;

@Log4j2
@RequiredArgsConstructor
public class InstantaneousTreeBreakingHandler implements ITreeBreakingHandler{
	private static InstantaneousTreeBreakingHandler INSTANCE;
	
	private final FallingTreeCommon<?> mod;
	private final LeafForceBreaker leafForceBreaker;
	
	@Override
	@NonNull
	public IBreakAttemptResult breakTree(boolean isCancellable, @NonNull IPlayer player, @NonNull Tree tree) throws BreakTreeTooBigException, BreakTreeTooSmallException{
		var tool = player.getMainHandItem();
		var level = tree.getLevel();
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
		
		var wantToBreakCount = Math.min(tree.getBreakableCount(), toolHandler.getMaxBreakCount());
		var lootHandler = new LootHandler(wantToBreakCount, mod.getConfiguration().getTrees().getTrunkLootPercentage());
		var brokenCount = tree.getBreakableParts().stream()
				.sorted(mod.getConfiguration().getTrees().getBreakOrder().getComparator())
				.limit(wantToBreakCount)
				.map(TreePart::blockPos)
				.mapToInt(logBlockPos -> {
					var logState = level.getBlockState(logBlockPos);
					
					if(!tree.getHitPos().equals(logBlockPos) && !mod.checkCanBreakBlock(level, logBlockPos, logState, player)){
						return 0;
					}
					
					player.awardItemUsed(tool.getItem());
					if(!player.isCreative() || mod.getConfiguration().isLootInCreative()){
						logState.getBlock().playerDestroy(
								level,
								player,
								mod.getConfiguration().getTrees().isSpawnItemsAtBreakPoint() ? tree.getHitPos() : logBlockPos,
								logState,
								level.getBlockEntity(logBlockPos),
								tool,
								lootHandler.breakNewTrunk()
						);
					}
					var isRemoved = level.removeBlock(logBlockPos, false);
					return isRemoved ? 1 : 0;
				})
				.sum();
		
		var toolDamage = toolHandler.getActualDamage(brokenCount) - 1;
		if(toolDamage > 0){
			tool.damage(toolDamage, player);
		}
		
		if(brokenCount >= wantToBreakCount){
			leafForceBreaker.forceBreakDecayLeaves(player, tree, level);
		}
		if(player.isCreative() && mod.getConfiguration().isLootInCreative()){
			tree.getStart().ifPresent(part -> part.blockState().getBlock().playerDestroy(level, player, tree.getHitPos(), part.blockState(), part.blockEntity(), tool, true));
		}
		return SuccessResult.DO_NOT_CANCEL;
	}
	
	@NonNull
	public static InstantaneousTreeBreakingHandler getInstance(@NonNull FallingTreeCommon<?> mod){
		if(isNull(INSTANCE)){
			INSTANCE = new InstantaneousTreeBreakingHandler(mod, new LeafForceBreaker(mod));
		}
		return INSTANCE;
	}
}
