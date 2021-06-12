package fr.raksrinana.fallingtree.fabric.config;

import fr.raksrinana.fallingtree.fabric.config.validator.BlockId;
import fr.raksrinana.fallingtree.fabric.config.validator.Min;
import fr.raksrinana.fallingtree.fabric.config.validator.MinMax;
import lombok.Getter;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class TreeConfiguration{
	@Tooltip(count = 5)
	@Comment("Additional list of blocks considered as logs and that will be destroyed by the mod. \n" +
			"INFO: Blocks marked with the log tag will already be whitelisted.")
	@BlockId
	public List<String> whitelistedLogs = new ArrayList<>();
	@Tooltip(count = 3)
	@Comment("List of blocks that should not be considered as logs. \n" +
			"INFO: This wins over the whitelist.")
	@BlockId
	public List<String> blacklistedLogs = new ArrayList<>();
	@Tooltip(count = 5)
	@Comment("Additional list of blocks considered as leaves (decay naturally). \n" +
			"INFO: Blocks marked with the leaves tag will already be whitelisted.")
	@BlockId
	public List<String> whitelistedLeaves = new ArrayList<>();
	@Tooltip(count = 2)
	@Comment("Additional list of blocks considered as leaves but that doesn't decay (need to be broken by tool).")
	@BlockId
	public List<String> whitelistedNonDecayLeaves = new ArrayList<>();
	@Tooltip(count = 3)
	@Comment("List of blocks that should not be considered as leaves. \n" +
			"INFO: This wins over the whitelist.")
	@BlockId
	public List<String> blacklistedLeaves = new ArrayList<>();
	@Tooltip(count = 7)
	@Comment("""
			How to break the tree.\s
			INSTANTANEOUS will break it in one go.\s
			SHIFT_DOWN will make the tree fall down as you cut it, so you still have to break x blocks but don't have to climb the tree for them.""")
	@EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
	public BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Tooltip(count = 9)
	@Comment("""
			What part of the tree should be cut.\s
			WHOLE_TREE will break the whole tree.\s
			ABOVE_CUT will break only blocks that are connected from above the cut point.\s
			ABOVE_Y will break only blocks that are above the y value of the cut point.""")
	@EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
	public DetectionMode detectionMode = DetectionMode.WHOLE_TREE;
	@Tooltip(count = 2)
	@Comment("The maximum size of a tree. If there's more logs than this value the tree won't be cut.")
	@Min(1)
	public int maxSize = 100;
	@Tooltip(count = 4)
	@Comment("The minimum amount of leaves that needs to be around the top most log in order for the mod to consider it a tree. \n" +
			"INFO: Only in INSTANTANEOUS mode.")
	@MinMax(min = 0, max = 5)
	public int minimumLeavesAroundRequired = 1;
	@Tooltip
	@Comment("When set to true, the mod be applied when cutting trunks.")
	public boolean treeBreaking = true;
	@Tooltip(count = 2)
	@Comment("When set to true, leaves that should naturally break will be broken instantly.")
	public boolean leavesBreaking = true;
	@Tooltip(count = 8)
	@Comment("""
			Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed.\s
			The radius is applied from one of the top most log blocks.\s
			INFO: break_leaves must be activated for this to take effect.\s
			INFO: Only in INSTANTANEOUS mode.""")
	@MinMax(min = 0, max = 10)
	public int leavesBreakingForceRadius = 0;
	@Tooltip(count = 4)
	@Comment("When set to true this allow to have any kind of log in a tree trunk. \n" +
			"Otherwise (false) the trunk will be considered as being only one kind of log.")
	public boolean allowMixedLogs = false;
	@Tooltip(count = 2)
	@Comment("When set to true nether tree warts (leaves) will be broken along with the trunk.")
	public boolean breakNetherTreeWarts = true;
	@Tooltip(count = 5)
	@Comment("""
			This defines the area in which the tree is searched. If any branch is going out of this area it won't be cut.\s
			This value is the radius of the area.\s
			i.e. Setting a value of 2 will result on an area of 3x3 centered on the log broken.\s
			If this value is set to a negative number then no area restriction will be applied.""")
	public int searchAreaRadius = -1;
	@Tooltip(count = 9)
	@Comment("List the blocks that can be against the tree. If something else is adjacent then the tree won't be cut. \n" +
			"INFO: Use adjacentStopMode to define how we stop the search for the tree.")
	@BlockId
	public List<String> whitelistedAdjacentBlocks = new ArrayList<>();
	@Tooltip(count = 9)
	@Comment("""
			What to do when an non whitelisted adjacent block is found.\s
			STOP_ALL will stop the search and nothing will be cut.\s
			STOP_BRANCH will stop the current branch only. The rest of the tree will be cut."""
	)
	@EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
	public AdjacentStopMode adjacentStopMode = AdjacentStopMode.STOP_ALL;
	
	public Collection<Block> getBlacklistedLeaveBlocks(){
		return ConfigCache.getInstance().getBlacklistedLeaves(this::getBlacklistedLeaves);
	}
	
	public Collection<Block> getBlacklistedLogBlocks(){
		return ConfigCache.getInstance().getBlacklistedLogs(this::getBlacklistedLogs);
	}
	
	public Collection<Block> getWhitelistedLeaveBlocks(){
		return ConfigCache.getInstance().getWhitelistedLeaves(this::getWhitelistedLeaves);
	}
	
	public Collection<Block> getWhitelistedNonDecayLeaveBlocks(){
		return ConfigCache.getInstance().getWhitelistedNonDecayLeaves(this::getWhitelistedNonDecayLeaves);
	}
	
	public Collection<Block> getWhitelistedAdjacentBlockBLocks(){
		return ConfigCache.getInstance().getWhitelistedAdjacentBlocks(this::getWhitelistedAdjacentBlocks);
	}
	
	public Collection<Block> getWhitelistedLogBlocks(){
		return ConfigCache.getInstance().getWhitelistedLogs(this::getWhitelistedLogs);
	}
}
