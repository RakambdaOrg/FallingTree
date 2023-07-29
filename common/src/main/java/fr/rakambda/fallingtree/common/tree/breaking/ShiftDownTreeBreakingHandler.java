package fr.rakambda.fallingtree.common.tree.breaking;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.tree.TreePart;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import fr.rakambda.fallingtree.common.wrapper.IServerLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static fr.rakambda.fallingtree.common.tree.TreePartType.NETHER_WART;
import static java.util.Objects.isNull;

@Log4j2
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
		if(treePart.treePartType() == NETHER_WART && mod.getConfiguration().getTrees().isBreakNetherTreeWarts()){
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

        if (level instanceof IServerLevel serverLevel) {
            serverLevel.spawnParticle(tree.getHitPos(), level.getBlockState(tree.getHitPos()), 10, 1, 1, 1, 5);
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
		if(!player.isCreative() || mod.getConfiguration().isLootInCreative()){
			logState.getBlock().playerDestroy(level, player, tree.getHitPos(), logState, level.getBlockEntity(blockPos), tool);
		}
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
