package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.StringListListEntry;
import net.minecraft.block.Block;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import static fr.raksrinana.fallingtree.config.Config.toTooltips;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsBlocks;

public class TreeConfiguration{
	private static final String[] DESC_BREAK_MODE = {
			"How to break the tree.",
			"",
			"Instantaneous will break it in one go.",
			"",
			"Shift down will make the tree fall down as you cut it,",
			"so you still have to break x blocks but don't have to",
			"climb the tree for them."
	};
	private static final String[] DESC_DETECTION_MODE = {
			"What part of the tree should be cut.",
			"",
			"Whole tree will break the whole tree.",
			"",
			"Above cut will break only blocks that are connected",
			"from above the cut point.",
			"",
			"Above y will break only blocks that are above the y",
			"value of the cut point."
	};
	private static final String[] DESC_WHITELISTED_LOGS = {
			"Additional list of blocks considered as logs and that",
			"will be destroyed by the mod.",
			"",
			"INFO: Blocks marked with the log tag will already be",
			"whitelisted."
	};
	private static final String[] DESC_BLACKLISTED_LOGS = {
			"List of blocks that should not be considered as logs.",
			"",
			"INFO: This wins over the whitelist."
	};
	private static final String[] DESC_WHITELISTED_LEAVES = {
			"Additional list of blocks considered as leaves.",
			"",
			"INFO: Blocks marked with the leaves tag will",
			"already be whitelisted."
	};
	private static final String[] DESC_BLACKLISTED_LEAVES = {
			"List of blocks that should not be considered as leaves.",
			"INFO: This wins over the whitelist."
	};
	private static final String[] DESC_MAX_SIZE = {
			"The maximum size of a tree. If there's more logs",
			"than this value the tree won't be cut.",
			"",
			"INFO: Only in INSTANTANEOUS mode."
	};
	private static final String[] DESC_TREE_BREAKING = {
			"When set to true, the mod will cut trees with one cut."
	};
	private static final String[] DESC_LEAVES_BREAKING = {
			"When set to true, leaves that should naturally break",
			"will be broken instantly."
	};
	private static final String[] DESC_LEAVES_BREAKING_FORCE_RADIUS = {
			"Radius to force break leaves. If another tree is",
			"still holding the leaves they'll still be broken.",
			"If the leaves are persistent (placed by player)",
			"they'll also be destroyed.",
			"The radius is applied from one of the top most log blocks.",
			"",
			"INFO: break_leaves must be activated for this to take effect.",
			"INFO: Only in INSTANTANEOUS mode."
	};
	private static final String[] DESC_MINIMUM_LEAVES_AROUND_REQUIRED = {
			"The minimum amount of leaves that needs to be around",
			"the top most log in order for the mod to consider it a tree.",
			"INFO: Only in INSTANTANEOUS mode."
	};
	private static final String[] DESC_ALLOW_MIXED_LOGS = {
			"When set to true this allow to have any kind of log",
			"in a tree trunk.",
			"Otherwise (false) the trunk will be considered as being",
			"only one kind of log."
	};
	private static final String[] DESC_BREAK_NETHER_TREE_WARTS = {
			"When set to true nether tree warts (leaves) will",
			"be broken along with the trunk."
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
	
	@OnlyIn(Dist.CLIENT)
	public void fillConfigScreen(ConfigBuilder builder){
		EnumListEntry<BreakMode> breakModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.breakMode"), BreakMode.class, getBreakMode())
				.setDefaultValue(BreakMode.INSTANTANEOUS)
				.setTooltip(toTooltips(DESC_BREAK_MODE))
				.setSaveConsumer(breakMode::set)
				.build();
		EnumListEntry<DetectionMode> detectionModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.detectionMode"), DetectionMode.class, getDetectionMode())
				.setDefaultValue(DetectionMode.WHOLE_TREE)
				.setTooltip(toTooltips(DESC_DETECTION_MODE))
				.setSaveConsumer(detectionMode::set)
				.build();
		StringListListEntry whitelistedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.whitelistedLogs"), (List<String>) whitelistedLogs.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(toTooltips(DESC_WHITELISTED_LOGS))
				.setSaveConsumer(whitelistedLogs::set)
				.build();
		StringListListEntry blacklistedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.blacklistedLogs"), (List<String>) blacklistedLogs.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(toTooltips(DESC_BLACKLISTED_LOGS))
				.setSaveConsumer(blacklistedLogs::set)
				.build();
		StringListListEntry whitelistedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.whitelistedLeaves"), (List<String>) whitelistedLeaves.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(toTooltips(DESC_WHITELISTED_LEAVES))
				.setSaveConsumer(whitelistedLeaves::set)
				.build();
		StringListListEntry blacklistedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.blacklistedLeaves"), (List<String>) blacklistedLeaves.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(toTooltips(DESC_BLACKLISTED_LEAVES))
				.setSaveConsumer(blacklistedLeaves::set)
				.build();
		IntegerListEntry maxSizeEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.maxSize"), getMaxSize())
				.setDefaultValue(100)
				.setMin(1)
				.setTooltip(toTooltips(DESC_MAX_SIZE))
				.setSaveConsumer(maxSize::set)
				.build();
		BooleanListEntry treeBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.treeBreaking"), isTreeBreaking())
				.setDefaultValue(true)
				.setTooltip(toTooltips(DESC_TREE_BREAKING))
				.setSaveConsumer(treeBreaking::set)
				.build();
		BooleanListEntry leavesBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.leavesBreaking"), isLeavesBreaking())
				.setDefaultValue(true)
				.setTooltip(toTooltips(DESC_LEAVES_BREAKING))
				.setSaveConsumer(leavesBreaking::set)
				.build();
		IntegerListEntry leavesBreakingForceRadiusEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.leavesBreakingForceRadius"), getLeavesBreakingForceRadius())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(10)
				.setTooltip(toTooltips(DESC_LEAVES_BREAKING_FORCE_RADIUS))
				.setSaveConsumer(leavesBreakingForceRadius::set)
				.build();
		IntegerListEntry minimumLeavesAroundRequiredEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.minimumLeavesAroundRequired"), getMinimumLeavesAroundRequired())
				.setDefaultValue(1)
				.setMin(0)
				.setMax(5)
				.setTooltip(toTooltips(DESC_MINIMUM_LEAVES_AROUND_REQUIRED))
				.setSaveConsumer(minimumLeavesAroundRequired::set)
				.build();
		BooleanListEntry allowMixedLogsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.allowMixedLogs"), isAllowMixedLogs())
				.setDefaultValue(false)
				.setTooltip(toTooltips(DESC_ALLOW_MIXED_LOGS))
				.setSaveConsumer(allowMixedLogs::set)
				.build();
		BooleanListEntry breakNetherTreeWartsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent("text.autoconfig.fallingtree.option.trees.breakNetherTreeWarts"), isBreakNetherTreeWarts())
				.setDefaultValue(false)
				.setTooltip(toTooltips(DESC_BREAK_NETHER_TREE_WARTS))
				.setSaveConsumer(breakNetherTreeWarts::set)
				.build();
		
		ConfigCategory tools = builder.getOrCreateCategory(new TranslationTextComponent("text.autoconfig.fallingtree.category.trees"));
		tools.addEntry(breakModeEntry);
		tools.addEntry(detectionModeEntry);
		tools.addEntry(whitelistedLogsEntry);
		tools.addEntry(blacklistedLogsEntry);
		tools.addEntry(whitelistedLeavesEntry);
		tools.addEntry(blacklistedLeavesEntry);
		tools.addEntry(maxSizeEntry);
		tools.addEntry(treeBreakingEntry);
		tools.addEntry(leavesBreakingEntry);
		tools.addEntry(leavesBreakingForceRadiusEntry);
		tools.addEntry(minimumLeavesAroundRequiredEntry);
		tools.addEntry(allowMixedLogsEntry);
		tools.addEntry(breakNetherTreeWartsEntry);
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
