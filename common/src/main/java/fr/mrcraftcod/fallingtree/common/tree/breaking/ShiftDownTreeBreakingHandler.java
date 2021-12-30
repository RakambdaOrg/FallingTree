package fr.mrcraftcod.fallingtree.common.tree.breaking;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.mrcraftcod.fallingtree.common.tree.Tree;
import fr.mrcraftcod.fallingtree.common.tree.TreePart;
import fr.mrcraftcod.fallingtree.common.wrapper.IItemStack;
import fr.mrcraftcod.fallingtree.common.wrapper.ILevel;
import fr.mrcraftcod.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.List;
import static fr.mrcraftcod.fallingtree.common.tree.TreePartType.NETHER_WART;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class ShiftDownTreeBreakingHandler implements ITreeBreakingHandler{
	private static ShiftDownTreeBreakingHandler INSTANCE;
	
	private final FallingTreeCommon<?> mod;
	
	@Override
	public boolean breakTree(@NotNull IPlayer player, @NotNull Tree tree) throws BreakTreeTooBigException{
		var tool = player.getMainHandItem();
		var treePartOptional = tree.getLastSequencePart();
		if(treePartOptional.isEmpty()){
			return false;
		}
		
		var treePart = treePartOptional.get();
		var level = tree.getLevel();
		if(treePart.treePartType() == NETHER_WART && mod.getConfiguration().getTrees().isInstantlyBreakWarts()){
			return breakElements(tree, level, player, tool, tree.getWarts());
		}
		else{
			return breakElements(tree, level, player, tool, List.of(treePart));
		}
	}
	
	private boolean breakElements(@NotNull Tree tree, @NotNull ILevel level, @NotNull IPlayer player, @NotNull IItemStack tool, @NotNull Collection<TreePart> parts) throws BreakTreeTooBigException{
		var count = parts.size();
		var damageMultiplicand = mod.getConfiguration().getTools().getDamageMultiplicand();
		var toolHandler = new ToolDamageHandler(tool,
				damageMultiplicand,
				mod.getConfiguration().getTools().isPreserve(),
				count,
				mod.getConfiguration().getTrees().getMaxSize(),
				mod.getConfiguration().getTrees().getMaxSizeAction(),
				mod.getConfiguration().getTools().getDamageRounding());
		
		if(toolHandler.getMaxBreakCount() <= 0){
			log.debug("Didn't break tree at {} as {}'s tool was about to break", tree.getHitPos(), player);
			mod.notifyPlayer(player, mod.translate("chat.fallingtree.prevented_break_tool"));
			return false;
		}
		
		var breakCount = parts.stream()
				.limit(toolHandler.getMaxBreakCount())
				.mapToInt(wart -> breakPart(tree, wart, level, player, tool))
				.sum();
		
		var damage = toolHandler.getActualDamage(breakCount);
		if(damage > 0){
			tool.damage(damage, player);
		}
		return true;
	}
	
	private int breakPart(@NotNull Tree tree, @NotNull TreePart treePart, @NotNull ILevel level, @NotNull IPlayer player, @NotNull IItemStack tool){
		var blockPos = treePart.blockPos();
		var logState = level.getBlockState(blockPos);
		
		if(!mod.checkCanBreakBlock(level, blockPos, logState, player)){
			return 0;
		}
		
		player.awardItemUsed(tool.getItem());
		logState.getBlock().playerDestroy(level, player, tree.getHitPos(), logState, level.getBlockEntity(blockPos), tool);
		level.removeBlock(blockPos, false);
		return 1;
	}
	
	@NotNull
	public static ShiftDownTreeBreakingHandler getInstance(@NotNull FallingTreeCommon<?> common){
		if(isNull(INSTANCE)){
			INSTANCE = new ShiftDownTreeBreakingHandler(common);
		}
		return INSTANCE;
	}
}
