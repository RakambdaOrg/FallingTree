package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.config.validator.Max;
import fr.raksrinana.fallingtree.config.validator.Min;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.minecraft.block.Block;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static fr.raksrinana.fallingtree.FallingTreeUtils.getAsBlocks;

@SuppressWarnings("FieldCanBeLocal")
public class TreeConfiguration{
	@Comment("Additional list of blocks considered as logs and that will be destroyed by the mod. " +
			"INFO: Blocks marked with the log tag will already be whitelisted.")
	public List<String> whitelistedLogs = new ArrayList<>();
	@Comment("List of blocks that should not be considered as logs. " +
			"INFO: This wins over the whitelist.")
	public List<String> blacklistedLogs = new ArrayList<>();
	@Comment("Additional list of blocks considered as leaves. " +
			"INFO: Blocks marked with the leaves tag will already be whitelisted.")
	public List<String> whitelistedLeaves = new ArrayList<>();
	@Comment("List of blocks that should not be considered as leaves. " +
			"INFO: This wins over the whitelist.")
	public List<String> blacklistedLeaves = new ArrayList<>();
	@Comment("How to break the tree. " +
			"Instantaneous will break it in one go. " +
			"Shift down will make the tree fall down as you cut it, so you still have to break x blocks but don't have to climb the tree for them.")
	@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
	public BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Comment("The maximum size of a tree. If there's more logs than this value the tree won't be cut. " +
			"INFO: Only in INSTANTANEOUS mode.")
	@Min(1)
	public int maxSize = 100;
	@Comment("The minimum amount of leaves that needs to be around the top most log in order for the mod to consider it a tree.")
	@Min(0)
	@Max(5)
	public int minimumLeavesAroundRequired = 3;
	@Comment("When set to true, leaves that should naturally break will be broken instantly. " +
			"INFO: Only in INSTANTANEOUS mode.")
	public boolean leavesBreaking = true;
	@Comment("Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed. " +
			"The radius is applied from one of the top most log blocks. " +
			"INFO: break_leaves must be activated for this to take effect. " +
			"INFO: Only in INSTANTANEOUS mode.")
	@Min(0)
	@Max(10)
	public int leavesBreakingForceRadius = 0;
	
	public Collection<Block> getBlacklistedLeaves(){
		return getAsBlocks(blacklistedLeaves);
	}
	
	public Collection<Block> getBlacklistedLogs(){
		return getAsBlocks(blacklistedLogs);
	}
	
	public BreakMode getBreakMode(){
		return breakMode;
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
}
