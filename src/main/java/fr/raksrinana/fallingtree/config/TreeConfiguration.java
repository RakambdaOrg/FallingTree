package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.config.validator.Max;
import fr.raksrinana.fallingtree.config.validator.Min;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.block.Block;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsBlocks;

@SuppressWarnings("FieldCanBeLocal")
public class TreeConfiguration{
	@Comment("Additional list of blocks considered as logs and that will be destroyed by the mod. \n" +
			"INFO: Blocks marked with the log tag will already be whitelisted.")
	public List<String> whitelistedLogs = new ArrayList<>();
	@Comment("List of blocks that should not be considered as logs. \n" +
			"INFO: This wins over the whitelist.")
	public List<String> blacklistedLogs = new ArrayList<>();
	@Comment("Additional list of blocks considered as leaves. \n" +
			"INFO: Blocks marked with the leaves tag will already be whitelisted.")
	public List<String> whitelistedLeaves = new ArrayList<>();
	@Comment("List of blocks that should not be considered as leaves. \n" +
			"INFO: This wins over the whitelist.")
	public List<String> blacklistedLeaves = new ArrayList<>();
	@Comment("How to break the tree. \n" +
			"Instantaneous will break it in one go. \n" +
			"Shift down will make the tree fall down as you cut it, so you still have to break x blocks but don't have to climb the tree for them.")
	@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
	public BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Comment("What part of the tree should be cut. \n" +
			"Whole tree will break the whole tree. \n" +
			"Above cut will break only blocks that are connected from above the cut point. \n" +
			"Above y will break only blocks that are above the y value of the cut point.")
	@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
	public DetectionMode detectionMode = DetectionMode.WHOLE_TREE;
	@Comment("The maximum size of a tree. If there's more logs than this value the tree won't be cut. \n" +
			"INFO: Only in INSTANTANEOUS mode.")
	@Min(1)
	public int maxSize = 100;
	@Comment("The minimum amount of leaves that needs to be around the top most log in order for the mod to consider it a tree. \n" +
			"INFO: Only in INSTANTANEOUS mode.")
	@Min(0)
	@Max(5)
	public int minimumLeavesAroundRequired = 1;
	@Comment("When set to true, the mod be applied when cutting trunks.")
	public boolean treeBreaking = true;
	@Comment("When set to true, leaves that should naturally break will be broken instantly.")
	public boolean leavesBreaking = true;
	@Comment("Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed. \n" +
			"The radius is applied from one of the top most log blocks. \n" +
			"INFO: break_leaves must be activated for this to take effect. \n" +
			"INFO: Only in INSTANTANEOUS mode.")
	@Min(0)
	@Max(10)
	public int leavesBreakingForceRadius = 0;
	@Comment("When set to true this allow to have any kind of log in a tree trunk. \n" +
			"Otherwise (false) the trunk will be considered as being only one kind of log.")
	public boolean allowMixedLogs = false;
	@Comment("When set to true nether tree warts (leaves) will be broken along with the trunk.")
	public boolean breakNetherTreeWarts = true;
	
	public Collection<Block> getBlacklistedLeaves(){
		return getAsBlocks(blacklistedLeaves);
	}
	
	public Collection<Block> getBlacklistedLogs(){
		return getAsBlocks(blacklistedLogs);
	}
	
	public BreakMode getBreakMode(){
		return breakMode;
	}
	
	public DetectionMode getDetectionMode(){
		return detectionMode;
	}
	
	public int getLeavesBreakingForceRadius(){
		return this.leavesBreakingForceRadius;
	}
	
	public int getMaxSize(){
		return this.maxSize;
	}
	
	public int getMinimumLeavesAroundRequired(){
		return this.minimumLeavesAroundRequired;
	}
	
	public Collection<Block> getWhitelistedLeaves(){
		return getAsBlocks(whitelistedLeaves);
	}
	
	public Collection<Block> getWhitelistedLogs(){
		return getAsBlocks(whitelistedLogs);
	}
	
	public boolean isLeavesBreaking(){
		return this.leavesBreaking;
	}
	
	public boolean isTreeBreaking(){
		return this.treeBreaking;
	}
	
	public boolean isAllowMixedLogs(){
		return this.allowMixedLogs;
	}
	
	public boolean isBreakNetherTreeWarts(){
		return breakNetherTreeWarts;
	}
}
