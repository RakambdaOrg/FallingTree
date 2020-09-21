package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static fr.raksrinana.fallingtree.FallingTreeUtils.getAsBlocks;

public class TreeConfiguration{
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedLogs;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedLogs;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedLeaves;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedLeaves;
	private final ForgeConfigSpec.ConfigValue<BreakMode> breakMode;
	private final ForgeConfigSpec.IntValue maxSize;
	private final ForgeConfigSpec.IntValue minimumLeavesAroundRequired;
	private final ForgeConfigSpec.BooleanValue leavesBreaking;
	private final ForgeConfigSpec.IntValue leavesBreakingForceRadius;
	private final ForgeConfigSpec.BooleanValue allowMixedLogs;
	
	public TreeConfiguration(ForgeConfigSpec.Builder builder){
		breakMode = builder.comment("How to break the tree.",
				"Instantaneous will break it in one go.",
				"Shift down will make the tree fall down as you cut it, so you still have to break x blocks but don't have to climb the tree for them.")
				.defineEnum("break_mode", BreakMode.INSTANTANEOUS);
		whitelistedLogs = builder.comment("Additional list of blocks considered as logs and that will be destroyed by the mod.",
				"INFO: Blocks marked with the log tag will already be whitelisted.")
				.defineList("logs_whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklistedLogs = builder.comment("List of blocks that should not be considered as logs.",
				"INFO: This wins over the whitelist.")
				.defineList("logs_blacklisted", Lists.newArrayList(), Objects::nonNull);
		whitelistedLeaves = builder.comment("Additional list of blocks considered as leaves.",
				"INFO: Blocks marked with the leaves tag will already be whitelisted.")
				.defineList("logs_whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklistedLeaves = builder.comment("List of blocks that should not be considered as leaves.",
				"INFO: This wins over the whitelist.")
				.defineList("logs_blacklisted", Lists.newArrayList(), Objects::nonNull);
		maxSize = builder.comment("The maximum size of a tree. If there's more logs than this value the tree won't be cut.",
				"INFO: Only in INSTANTANEOUS mode.")
				.defineInRange("logs_max_count", 100, 1, Integer.MAX_VALUE);
		leavesBreaking = builder.comment("When set to true, leaves that should naturally break will be broken instantly.")
				.define("leaves_breaking", true);
		leavesBreakingForceRadius = builder.comment("Radius to force break leaves. If another tree is still holding the leaves they'll still be broken. If the leaves are persistent (placed by player) they'll also be destroyed.",
				"The radius is applied from one of the top most log blocks.",
				"INFO: break_leaves must be activated for this to take effect.",
				"INFO: Only in INSTANTANEOUS mode.")
				.defineInRange("leaves_breaking_force_radius", 0, 0, 10);
		minimumLeavesAroundRequired = builder.comment("The minimum amount of leaves that needs to be around the top most log in order for the mod to consider it a tree.",
				"INFO: Only in INSTANTANEOUS mode.")
				.defineInRange("minimum_leaves_around_required", 0, 0, 5);
		allowMixedLogs = builder.comment("When set to true this allow to have any kind of log in a tree trunk.",
				"Otherwise (false) the trunk will be considered as being only one kind of log.")
				.define("allow_mixed_logs", false);
	}
	
	public Collection<Block> getBlacklistedLeaves(){
		return getAsBlocks(blacklistedLeaves.get());
	}
	
	public Collection<Block> getBlacklistedLogs(){
		return getAsBlocks(blacklistedLogs.get());
	}
	
	public int getLeavesBreakingForceRadius(){
		return this.leavesBreakingForceRadius.get();
	}
	
	public int getMaxSize(){
		return this.maxSize.get();
	}
	
	public int getMinimumLeavesAroundRequired(){
		return this.minimumLeavesAroundRequired.get();
	}
	
	public Collection<Block> getWhitelistedLeaves(){
		return getAsBlocks(whitelistedLeaves.get());
	}
	
	public Collection<Block> getWhitelistedLogs(){
		return getAsBlocks(whitelistedLogs.get());
	}
	
	public boolean isLeavesBreaking(){
		return this.leavesBreaking.get();
	}
	
	public BreakMode getBreakMode(){
		return breakMode.get();
	}
	
	public boolean isAllowMixedLogs(){
		return this.allowMixedLogs.get();
	}
}
