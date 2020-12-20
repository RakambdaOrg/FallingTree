package fr.raksrinana.fallingtree.config;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.StringListListEntry;
import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collection;
import java.util.LinkedList;
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
	
	@OnlyIn(Dist.CLIENT)
	public void fillConfigScreen(ConfigBuilder builder){
		EnumListEntry<BreakMode> breakModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslationTextComponent(getFieldName("breakMode")), BreakMode.class, getBreakMode())
				.setDefaultValue(BreakMode.INSTANTANEOUS)
				.setTooltip(getTooltips("breakMode", 7))
				.setSaveConsumer(breakMode::set)
				.build();
		EnumListEntry<DetectionMode> detectionModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslationTextComponent(getFieldName("detectionMode")), DetectionMode.class, getDetectionMode())
				.setDefaultValue(DetectionMode.WHOLE_TREE)
				.setTooltip(getTooltips("detectionMode", 9))
				.setSaveConsumer(detectionMode::set)
				.build();
		StringListListEntry whitelistedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("whitelistedLogs")), (List<String>) whitelistedLogs.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("whitelistedLogs", 5))
				.setSaveConsumer(whitelistedLogs::set)
				.build();
		StringListListEntry blacklistedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("blacklistedLogs")), (List<String>) blacklistedLogs.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("blacklistedLogs", 3))
				.setSaveConsumer(blacklistedLogs::set)
				.build();
		StringListListEntry whitelistedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("whitelistedLeaves")), (List<String>) whitelistedLeaves.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("whitelistedLeaves", 4))
				.setSaveConsumer(whitelistedLeaves::set)
				.build();
		StringListListEntry blacklistedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("blacklistedLeaves")), (List<String>) blacklistedLeaves.get())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("blacklistedLeaves", 3))
				.setSaveConsumer(blacklistedLeaves::set)
				.build();
		IntegerListEntry maxSizeEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("maxSize")), getMaxSize())
				.setDefaultValue(100)
				.setMin(1)
				.setTooltip(getTooltips("maxSize", 4))
				.setSaveConsumer(maxSize::set)
				.build();
		BooleanListEntry treeBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("treeBreaking")), isTreeBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("treeBreaking", 1))
				.setSaveConsumer(treeBreaking::set)
				.build();
		BooleanListEntry leavesBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("leavesBreaking")), isLeavesBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("leavesBreaking", 2))
				.setSaveConsumer(leavesBreaking::set)
				.build();
		IntegerListEntry leavesBreakingForceRadiusEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("leavesBreakingForceRadius")), getLeavesBreakingForceRadius())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(10)
				.setTooltip(getTooltips("leavesBreakingForceRadius", 8))
				.setSaveConsumer(leavesBreakingForceRadius::set)
				.build();
		IntegerListEntry minimumLeavesAroundRequiredEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("minimumLeavesAroundRequired")), getMinimumLeavesAroundRequired())
				.setDefaultValue(1)
				.setMin(0)
				.setMax(5)
				.setTooltip(getTooltips("minimumLeavesAroundRequired", 4))
				.setSaveConsumer(minimumLeavesAroundRequired::set)
				.build();
		BooleanListEntry allowMixedLogsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("allowMixedLogs")), isAllowMixedLogs())
				.setDefaultValue(false)
				.setTooltip(getTooltips("allowMixedLogs", 4))
				.setSaveConsumer(allowMixedLogs::set)
				.build();
		BooleanListEntry breakNetherTreeWartsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("breakNetherTreeWarts")), isBreakNetherTreeWarts())
				.setDefaultValue(false)
				.setTooltip(getTooltips("breakNetherTreeWarts", 2))
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
	
	private String getFieldName(String fieldName){
		return "text.autoconfig.fallingtree.option.trees." + fieldName;
	}
	
	private ITextComponent[] getTooltips(String fieldName, int count){
		String tooltipKey = getFieldName(fieldName) + ".@Tooltip";
		List<String> keys = new LinkedList<>();
		if(count <= 1){
			keys.add(tooltipKey);
		}
		else{
			for(int i = 0; i < count; i++){
				keys.add(tooltipKey + "[" + i + "]");
			}
		}
		return keys.stream()
				.map(TranslationTextComponent::new)
				.toArray(ITextComponent[]::new);
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
