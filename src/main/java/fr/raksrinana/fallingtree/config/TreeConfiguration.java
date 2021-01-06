package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsBlocks;

public class TreeConfiguration{
	private static final String[] DESC_BREAK_MODE = {
			"How to break the tree.",
			"INSTANTANEOUS will break it in one go.",
			"SHIFT_DOWN will make the tree fall down as you cut it, so you still have to break x blocks but don't have to climb the tree for them."
	};
	private static final String[] DESC_DETECTION_MODE = {
			"What part of the tree should be cut.",
			"WHOLE_TREE tree will break the whole tree.",
			"ABOVE_CUT will break only blocks that are connected from above the cut point.",
			"ABOVE_Y will break only blocks that are above the y value of the cut point."
	};
	private static final String[] DESC_WHITELISTED_LOGS = {
			"Additional list of blocks considered as logs and that will be destroyed by the mod.",
			"INFO: Blocks marked with the log tag will already be whitelisted."
	};
	private static final String[] DESC_BLACKLISTED_LOGS = {
			"List of blocks that should not be considered as logs.",
			"INFO: This wins over the whitelist."
	};
	private static final String[] DESC_WHITELISTED_LEAVES = {
			"Additional list of blocks considered as leaves.",
			"INFO: Blocks marked with the leaves tag will already be whitelisted."
	};
	private static final String[] DESC_BLACKLISTED_LEAVES = {
			"List of blocks that should not be considered as leaves.",
			"INFO: This wins over the whitelist."
	};
	private static final String[] DESC_MAX_SIZE = {
			"The maximum size of a tree. If there's more logs than this value the tree won't be cut.",
			"INFO: Only in INSTANTANEOUS mode."
	};
	private static final String[] DESC_TREE_BREAKING = {
			"When set to true, the mod will cut trees with one cut."
	};
	private static final String[] DESC_LEAVES_BREAKING = {
			"When set to true, leaves that should naturally break will be broken instantly."
	};
	private static final String[] DESC_LEAVES_BREAKING_FORCE_RADIUS = {
			"Radius to force break leaves. If another tree is still holding the leaves they'll still be broken.",
			"If the leaves are persistent (placed by player) they'll also be destroyed.",
			"The radius is applied from one of the top most log blocks.",
			"INFO: break_leaves must be activated for this to take effect.",
			"INFO: Only in INSTANTANEOUS mode."
	};
	private static final String[] DESC_MINIMUM_LEAVES_AROUND_REQUIRED = {
			"The minimum amount of leaves that needs to be around the top most log in order for the mod to consider it a tree.",
			"INFO: Only in INSTANTANEOUS mode."
	};
	private static final String[] DESC_ALLOW_MIXED_LOGS = {
			"When set to true this allow to have any kind of log in a tree trunk.",
			"Otherwise (false) the trunk will be considered as being only one kind of log."
	};
	private static final String[] DESC_BREAK_NETHER_TREE_WARTS = {
			"When set to true nether tree warts (leaves) will be broken along with the trunk."
	};
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedLogs;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedLogs;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistedLeaves;
	private final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedLeaves;
	private final ForgeConfigSpec.ConfigValue<BreakMode> breakMode;
	private final ForgeConfigSpec.ConfigValue<DetectionMode> detectionMode;
	private final ForgeConfigSpec.IntValue maxSize;
	private final ForgeConfigSpec.IntValue minimumLeavesAroundRequired;
	private final ForgeConfigSpec.BooleanValue treeBreaking;
	private final ForgeConfigSpec.BooleanValue leavesBreaking;
	private final ForgeConfigSpec.IntValue leavesBreakingForceRadius;
	private final ForgeConfigSpec.BooleanValue allowMixedLogs;
	private final ForgeConfigSpec.BooleanValue breakNetherTreeWarts;
	
	public TreeConfiguration(ForgeConfigSpec.Builder builder){
		breakMode = builder.comment(DESC_BREAK_MODE)
				.defineEnum("break_mode", BreakMode.INSTANTANEOUS);
		detectionMode = builder.comment(DESC_DETECTION_MODE)
				.defineEnum("detection_mode", DetectionMode.WHOLE_TREE);
		whitelistedLogs = builder.comment(DESC_WHITELISTED_LOGS)
				.defineList("logs_whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklistedLogs = builder.comment(DESC_BLACKLISTED_LOGS)
				.defineList("logs_blacklisted", Lists.newArrayList(), Objects::nonNull);
		whitelistedLeaves = builder.comment(DESC_WHITELISTED_LEAVES)
				.defineList("logs_whitelisted", Lists.newArrayList(), Objects::nonNull);
		blacklistedLeaves = builder.comment(DESC_BLACKLISTED_LEAVES)
				.defineList("logs_blacklisted", Lists.newArrayList(), Objects::nonNull);
		maxSize = builder.comment(DESC_MAX_SIZE)
				.defineInRange("logs_max_count", 100, 1, Integer.MAX_VALUE);
		treeBreaking = builder.comment(DESC_TREE_BREAKING)
				.define("tree_breaking", true);
		leavesBreaking = builder.comment(DESC_LEAVES_BREAKING)
				.define("leaves_breaking", true);
		leavesBreakingForceRadius = builder.comment(DESC_LEAVES_BREAKING_FORCE_RADIUS)
				.defineInRange("leaves_breaking_force_radius", 0, 0, 10);
		minimumLeavesAroundRequired = builder.comment(DESC_MINIMUM_LEAVES_AROUND_REQUIRED)
				.defineInRange("minimum_leaves_around_required", 1, 0, 5);
		allowMixedLogs = builder.comment(DESC_ALLOW_MIXED_LOGS)
				.define("allow_mixed_logs", false);
		breakNetherTreeWarts = builder.comment(DESC_BREAK_NETHER_TREE_WARTS)
				.define("break_nether_tree_warts", true);
	}
	
	public List<String> getBlacklistedLeavesStr(){
		return (List<String>) blacklistedLeaves.get();
	}
	
	public List<String> getBlacklistedLogsStr(){
		return (List<String>) blacklistedLogs.get();
	}
	
	public List<String> getWhitelistedLeavesStr(){
		return (List<String>) whitelistedLeaves.get();
	}
	
	public List<String> getWhitelistedLogsStr(){
		return (List<String>) whitelistedLogs.get();
	}
	
	public void setAllowMixedLogs(Boolean value){
		allowMixedLogs.set(value);
	}
	
	public void setBlacklistedLeaves(List<String> value){
		blacklistedLeaves.set(value);
	}
	
	public void setBlacklistedLogs(List<String> value){
		blacklistedLogs.set(value);
	}
	
	public void setBreakMode(BreakMode value){
		breakMode.set(value);
	}
	
	public void setBreakNetherTreeWarts(Boolean value){
		breakNetherTreeWarts.set(value);
	}
	
	public void setDetectionMode(DetectionMode value){
		detectionMode.set(value);
	}
	
	public void setLeavesBreaking(Boolean value){
		leavesBreaking.set(value);
	}
	
	public void setLeavesBreakingForceRadius(Integer value){
		leavesBreakingForceRadius.set(value);
	}
	
	public void setMaxSize(Integer value){
		maxSize.set(value);
	}
	
	public void setMinimumLeavesAroundRequired(Integer value){
		minimumLeavesAroundRequired.set(value);
	}
	
	public void setTreeBreaking(Boolean value){
		treeBreaking.set(value);
	}
	
	public void setWhitelistedLeaves(List<String> value){
		whitelistedLeaves.set(value);
	}
	
	public void setWhitelistedLogs(List<String> value){
		whitelistedLogs.set(value);
	}
	
	public Collection<Block> getBlacklistedLeaves(){
		return getAsBlocks(blacklistedLeaves.get());
	}
	
	public Collection<Block> getBlacklistedLogs(){
		return getAsBlocks(blacklistedLogs.get());
	}
	
	public boolean isLeavesBreaking(){
		return this.leavesBreaking.get();
	}
	
	public boolean isTreeBreaking(){
		return this.treeBreaking.get();
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
	
	public int getLeavesBreakingForceRadius(){
		return this.leavesBreakingForceRadius.get();
	}
	
	public BreakMode getBreakMode(){
		return breakMode.get();
	}
	
	public DetectionMode getDetectionMode(){
		return detectionMode.get();
	}
	
	public boolean isAllowMixedLogs(){
		return this.allowMixedLogs.get();
	}
	
	public boolean isBreakNetherTreeWarts(){
		return breakNetherTreeWarts.get();
	}
}
