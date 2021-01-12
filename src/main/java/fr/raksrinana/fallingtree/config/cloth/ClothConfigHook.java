package fr.raksrinana.fallingtree.config.cloth;

import com.google.common.collect.Lists;
import fr.raksrinana.fallingtree.config.*;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.gui.entries.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ClothConfigHook{
	private static final Pattern MINECRAFT_ID_PATTERN = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9/._-]+");
	
	public void load(){
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, parent) -> {
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(parent)
					.setTitle(new StringTextComponent("FallingTree"));
			
			fillConfigScreen(builder);
			
			builder.setSavingRunnable(() -> ConfigCache.getInstance().invalidate());
			
			return builder.build();
		});
	}
	
	@OnlyIn(Dist.CLIENT)
	public void fillConfigScreen(ConfigBuilder builder){
		CommonConfig config = Config.COMMON;
		
		BooleanListEntry reverseSneakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName(null, "reverseSneaking")), config.isReverseSneaking())
				.setDefaultValue(false)
				.setTooltip(getTooltips(null, "reverseSneaking", 2))
				.setSaveConsumer(config::setReverseSneaking)
				.build();
		BooleanListEntry breakInCreativeEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName(null, "breakInCreative")), config.isBreakInCreative())
				.setDefaultValue(false)
				.setTooltip(getTooltips(null, "breakInCreative", 2))
				.setSaveConsumer(config::setBreakInCreative)
				.build();
		
		ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("text.autoconfig.fallingtree.category.default"));
		general.addEntry(reverseSneakingEntry);
		general.addEntry(breakInCreativeEntry);
		
		fillTreesConfigScreen(builder);
		fillToolsConfigScreen(builder);
	}
	
	@OnlyIn(Dist.CLIENT)
	private void fillTreesConfigScreen(ConfigBuilder builder){
		TreeConfiguration config = Config.COMMON.getTreesConfiguration();
		
		EnumListEntry<BreakMode> breakModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslationTextComponent(getFieldName("trees", "breakMode")), BreakMode.class, config.getBreakMode())
				.setDefaultValue(BreakMode.INSTANTANEOUS)
				.setTooltip(getTooltips("trees", "breakMode", 7))
				.setSaveConsumer(config::setBreakMode)
				.build();
		EnumListEntry<DetectionMode> detectionModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslationTextComponent(getFieldName("trees", "detectionMode")), DetectionMode.class, config.getDetectionMode())
				.setDefaultValue(DetectionMode.WHOLE_TREE)
				.setTooltip(getTooltips("trees", "detectionMode", 9))
				.setSaveConsumer(config::setDetectionMode)
				.build();
		StringListListEntry whitelistedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("trees", "whitelistedLogs")), config.getWhitelistedLogsStr())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "whitelistedLogs", 5))
				.setSaveConsumer(config::setWhitelistedLogs)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		StringListListEntry blacklistedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("trees", "blacklistedLogs")), config.getBlacklistedLogsStr())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "blacklistedLogs", 3))
				.setSaveConsumer(config::setBlacklistedLogs)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		StringListListEntry whitelistedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("trees", "whitelistedLeaves")), config.getWhitelistedLeavesStr())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "whitelistedLeaves", 4))
				.setSaveConsumer(config::setWhitelistedLeaves)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		StringListListEntry blacklistedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("trees", "blacklistedLeaves")), config.getBlacklistedLeavesStr())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "blacklistedLeaves", 3))
				.setSaveConsumer(config::setBlacklistedLeaves)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		IntegerListEntry maxSizeEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("trees", "maxSize")), config.getMaxSize())
				.setDefaultValue(100)
				.setMin(1)
				.setTooltip(getTooltips("trees", "maxSize", 4))
				.setSaveConsumer(config::setMaxSize)
				.build();
		BooleanListEntry treeBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("trees", "treeBreaking")), config.isTreeBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "treeBreaking", 1))
				.setSaveConsumer(config::setTreeBreaking)
				.build();
		BooleanListEntry leavesBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("trees", "leavesBreaking")), config.isLeavesBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "leavesBreaking", 2))
				.setSaveConsumer(config::setLeavesBreaking)
				.build();
		IntegerListEntry leavesBreakingForceRadiusEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("trees", "leavesBreakingForceRadius")), config.getLeavesBreakingForceRadius())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(10)
				.setTooltip(getTooltips("trees", "leavesBreakingForceRadius", 8))
				.setSaveConsumer(config::setLeavesBreakingForceRadius)
				.build();
		IntegerListEntry minimumLeavesAroundRequiredEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("trees", "minimumLeavesAroundRequired")), config.getMinimumLeavesAroundRequired())
				.setDefaultValue(1)
				.setMin(0)
				.setMax(5)
				.setTooltip(getTooltips("trees", "minimumLeavesAroundRequired", 4))
				.setSaveConsumer(config::setMinimumLeavesAroundRequired)
				.build();
		BooleanListEntry allowMixedLogsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("trees", "allowMixedLogs")), config.isAllowMixedLogs())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "allowMixedLogs", 4))
				.setSaveConsumer(config::setAllowMixedLogs)
				.build();
		BooleanListEntry breakNetherTreeWartsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("trees", "breakNetherTreeWarts")), config.isBreakNetherTreeWarts())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "breakNetherTreeWarts", 2))
				.setSaveConsumer(config::setBreakNetherTreeWarts)
				.build();
		IntegerListEntry searchAreaRadiusEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("trees", "searchAreaRadius")), config.getSearchAreaRadius())
				.setDefaultValue(-1)
				.setTooltip(getTooltips("trees", "searchAreaRadius", 5))
				.setSaveConsumer(config::setSearchAreaRadius)
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
		tools.addEntry(searchAreaRadiusEntry);
	}
	
	@OnlyIn(Dist.CLIENT)
	private void fillToolsConfigScreen(ConfigBuilder builder){
		ToolConfiguration config = Config.COMMON.getToolsConfiguration();
		
		BooleanListEntry ignoreToolsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("tools", "ignoreTools")), config.isIgnoreTools())
				.setDefaultValue(false)
				.setTooltip(getTooltips("tools", "ignoreTools", 4))
				.setSaveConsumer(config::setIgnoreTools)
				.build();
		StringListListEntry whitelistedEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("tools", "whitelisted")), config.getWhitelistedStr())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("tools", "whitelisted", 3))
				.setSaveConsumer(config::setWhitelisted)
				.setCellErrorSupplier(getMinecraftItemIdCellError())
				.build();
		StringListListEntry blacklistedEntry = builder.entryBuilder()
				.startStrList(new TranslationTextComponent(getFieldName("tools", "blacklisted")), config.getBlacklistedStr())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("tools", "blacklisted", 3))
				.setSaveConsumer(config::setBlacklisted)
				.setCellErrorSupplier(getMinecraftItemIdCellError())
				.build();
		IntegerListEntry damageMultiplicandEntry = builder.entryBuilder()
				.startIntField(new TranslationTextComponent(getFieldName("tools", "damageMultiplicand")), config.getDamageMultiplicand())
				.setDefaultValue(1)
				.setMin(0)
				.setTooltip(getTooltips("tools", "damageMultiplicand", 7))
				.setSaveConsumer(config::setDamageMultiplicand)
				.build();
		DoubleListEntry speedMultiplicandEntry = builder.entryBuilder()
				.startDoubleField(new TranslationTextComponent(getFieldName("tools", "speedMultiplicand")), config.getSpeedMultiplicand())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(50)
				.setTooltip(getTooltips("tools", "speedMultiplicand", 15))
				.setSaveConsumer(config::setSpeedMultiplicand)
				.build();
		BooleanListEntry preserveEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslationTextComponent(getFieldName("tools", "preserve")), config.isPreserve())
				.setDefaultValue(false)
				.setTooltip(getTooltips("tools", "preserve", 3))
				.setSaveConsumer(config::setPreserve)
				.build();
		
		ConfigCategory tools = builder.getOrCreateCategory(new TranslationTextComponent("text.autoconfig.fallingtree.category.tools"));
		tools.addEntry(ignoreToolsEntry);
		tools.addEntry(whitelistedEntry);
		tools.addEntry(blacklistedEntry);
		tools.addEntry(damageMultiplicandEntry);
		tools.addEntry(speedMultiplicandEntry);
		tools.addEntry(preserveEntry);
	}
	
	private String getFieldName(String category, String fieldName){
		if(category == null || category.isEmpty()){
			return "text.autoconfig.fallingtree.option." + fieldName;
		}
		return "text.autoconfig.fallingtree.option." + category + "." + fieldName;
	}
	
	private ITextComponent[] getTooltips(String category, String fieldName, int count){
		String tooltipKey = getFieldName(category, fieldName) + ".@Tooltip";
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
	
	public static Function<String, Optional<ITextComponent>> getMinecraftBlockIdCellError(){
		return value -> {
			boolean valid = false;
			if(value != null){
				valid = MINECRAFT_ID_PATTERN.matcher(value).matches();
			}
			if(!valid){
				return Optional.of(new TranslationTextComponent("text.autoconfig.fallingtree.error.invalidBlockResourceLocation"));
			}
			return Optional.empty();
		};
	}
	
	public static Function<String, Optional<ITextComponent>> getMinecraftItemIdCellError(){
		return value -> {
			boolean valid = false;
			if(value != null){
				valid = MINECRAFT_ID_PATTERN.matcher(value).matches();
			}
			if(!valid){
				return Optional.of(new TranslationTextComponent("text.autoconfig.fallingtree.error.invalidItemResourceLocation"));
			}
			return Optional.empty();
		};
	}
}
