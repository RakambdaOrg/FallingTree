package fr.raksrinana.fallingtree.fabric.config.cloth;

import com.google.common.collect.Lists;
import fr.raksrinana.fallingtree.fabric.config.*;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ClothConfigHook{
	private static final Pattern MINECRAFT_ID_PATTERN = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9/._-]+");
	
	public Function<Screen, Screen> load(){
		return (screen) -> {
			var builder = ConfigBuilder.create()
					.setParentScreen(screen)
					.setTitle(new TextComponent("FallingTree"));
			
			var configuration = Configuration.getInstance();
			builder.setSavingRunnable(configuration::onUpdate);
			
			fillConfigScreen(builder, configuration);
			
			return builder.build();
		};
	}
	
	@Environment(EnvType.CLIENT)
	public void fillConfigScreen(ConfigBuilder builder, Configuration config){
		var reverseSneakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName(null, "reverseSneaking")), config.isReverseSneaking())
				.setDefaultValue(false)
				.setTooltip(getTooltips(null, "reverseSneaking", 2))
				.setSaveConsumer(config::setReverseSneaking)
				.build();
		var breakInCreativeEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName(null, "breakInCreative")), config.isBreakInCreative())
				.setDefaultValue(false)
				.setTooltip(getTooltips(null, "breakInCreative", 2))
				.setSaveConsumer(config::setBreakInCreative)
				.build();
		var notificationModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslatableComponent(getFieldName(null, "notificationMode")), NotificationMode.class, config.getNotificationMode())
				.setDefaultValue(NotificationMode.ACTION_BAR)
				.setTooltip(getTooltips(null, "notificationMode", 5))
				.setSaveConsumer(config::setNotificationMode)
				.build();
		
		var general = builder.getOrCreateCategory(new TranslatableComponent("text.autoconfig.fallingtree.category.default"));
		general.addEntry(reverseSneakingEntry);
		general.addEntry(breakInCreativeEntry);
		general.addEntry(notificationModeEntry);
		
		fillTreesConfigScreen(builder, config.getTrees());
		fillToolsConfigScreen(builder, config.getTools());
	}
	
	@Environment(EnvType.CLIENT)
	private void fillTreesConfigScreen(ConfigBuilder builder, TreeConfiguration config){
		var breakModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslatableComponent(getFieldName("trees", "breakMode")), BreakMode.class, config.getBreakMode())
				.setDefaultValue(BreakMode.INSTANTANEOUS)
				.setTooltip(getTooltips("trees", "breakMode", 7))
				.setSaveConsumer(config::setBreakMode)
				.build();
		var detectionModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslatableComponent(getFieldName("trees", "detectionMode")), DetectionMode.class, config.getDetectionMode())
				.setDefaultValue(DetectionMode.WHOLE_TREE)
				.setTooltip(getTooltips("trees", "detectionMode", 9))
				.setSaveConsumer(config::setDetectionMode)
				.build();
		var allowedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("trees", "allowedLogs")), config.getAllowedLogs())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedLogs", 5))
				.setSaveConsumer(config::setAllowedLogs)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		var deniedLogsEntry = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("trees", "deniedLogs")), config.getDeniedLogs())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "deniedLogs", 3))
				.setSaveConsumer(config::setDeniedLogs)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		var allowedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("trees", "allowedLeaves")), config.getAllowedLeaves())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedLeaves", 5))
				.setSaveConsumer(config::setAllowedLeaves)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		var allowedNonDecayLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("trees", "allowedNonDecayLeaves")), config.getAllowedNonDecayLeaves())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedNonDecayLeaves", 2))
				.setSaveConsumer(config::setAllowedNonDecayLeaves)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		var deniedLeavesEntry = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("trees", "deniedLeaves")), config.getDeniedLeaves())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "deniedLeaves", 3))
				.setSaveConsumer(config::setDeniedLeaves)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		var maxScanSizeEntry = builder.entryBuilder()
				.startIntField(new TranslatableComponent(getFieldName("trees", "maxScanSize")), config.getMaxScanSize())
				.setDefaultValue(500)
				.setMin(1)
				.setTooltip(getTooltips("trees", "maxScanSize", 3))
				.setSaveConsumer(config::setMaxScanSize)
				.build();
		var maxSizeEntry = builder.entryBuilder()
				.startIntField(new TranslatableComponent(getFieldName("trees", "maxSize")), config.getMaxSize())
				.setDefaultValue(100)
				.setMin(1)
				.setTooltip(getTooltips("trees", "maxSize", 2))
				.setSaveConsumer(config::setMaxSize)
				.build();
		var maxSizeActionEntry = builder.entryBuilder()
				.startEnumSelector(new TranslatableComponent(getFieldName("trees", "maxSizeAction")), MaxSizeAction.class, config.getMaxSizeAction())
				.setDefaultValue(MaxSizeAction.ABORT)
				.setTooltip(getTooltips("trees", "maxSizeAction", 4))
				.setSaveConsumer(config::setMaxSizeAction)
				.build();
		var breakOrderEntry = builder.entryBuilder()
				.startEnumSelector(new TranslatableComponent(getFieldName("trees", "breakOrder")), BreakOrder.class, config.getBreakOrder())
				.setDefaultValue(BreakOrder.FURTHEST_FIRST)
				.setTooltip(getTooltips("trees", "breakOrder", 4))
				.setSaveConsumer(config::setBreakOrder)
				.build();
		var treeBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("trees", "treeBreaking")), config.isTreeBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "treeBreaking", 1))
				.setSaveConsumer(config::setTreeBreaking)
				.build();
		var leavesBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("trees", "leavesBreaking")), config.isLeavesBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "leavesBreaking", 2))
				.setSaveConsumer(config::setLeavesBreaking)
				.build();
		var leavesBreakingForceRadiusEntry = builder.entryBuilder()
				.startIntField(new TranslatableComponent(getFieldName("trees", "leavesBreakingForceRadius")), config.getLeavesBreakingForceRadius())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(10)
				.setTooltip(getTooltips("trees", "leavesBreakingForceRadius", 8))
				.setSaveConsumer(config::setLeavesBreakingForceRadius)
				.build();
		var minimumLeavesAroundRequiredEntry = builder.entryBuilder()
				.startIntField(new TranslatableComponent(getFieldName("trees", "minimumLeavesAroundRequired")), config.getMinimumLeavesAroundRequired())
				.setDefaultValue(1)
				.setMin(0)
				.setMax(5)
				.setTooltip(getTooltips("trees", "minimumLeavesAroundRequired", 4))
				.setSaveConsumer(config::setMinimumLeavesAroundRequired)
				.build();
		var includePersistentLeavesInRequiredCountEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("trees", "includePersistentLeavesInRequiredCount")), config.isIncludePersistentLeavesInRequiredCount())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "includePersistentLeavesInRequiredCount", 5))
				.setSaveConsumer(config::setIncludePersistentLeavesInRequiredCount)
				.build();
		var allowMixedLogsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("trees", "allowMixedLogs")), config.isAllowMixedLogs())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "allowMixedLogs", 4))
				.setSaveConsumer(config::setAllowMixedLogs)
				.build();
		var breakNetherTreeWartsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("trees", "breakNetherTreeWarts")), config.isBreakNetherTreeWarts())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "breakNetherTreeWarts", 2))
				.setSaveConsumer(config::setBreakNetherTreeWarts)
				.build();
		var instantlyBreakWartsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("trees", "instantlyBreakWarts")), config.isInstantlyBreakWarts())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "instantlyBreakWarts", 2))
				.setSaveConsumer(config::setInstantlyBreakWarts)
				.build();
		var searchAreaRadiusEntry = builder.entryBuilder()
				.startIntField(new TranslatableComponent(getFieldName("trees", "searchAreaRadius")), config.getSearchAreaRadius())
				.setDefaultValue(-1)
				.setTooltip(getTooltips("trees", "searchAreaRadius", 5))
				.setSaveConsumer(config::setSearchAreaRadius)
				.build();
		var allowedAdjacentBlocks = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("trees", "allowedAdjacentBlocks")), config.getAllowedAdjacentBlocks())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedAdjacentBlocks", 9))
				.setSaveConsumer(config::setAllowedAdjacentBlocks)
				.setCellErrorSupplier(getMinecraftBlockIdCellError())
				.build();
		var adjacentStopModeEntry = builder.entryBuilder()
				.startEnumSelector(new TranslatableComponent(getFieldName("trees", "adjacentStopMode")), AdjacentStopMode.class, config.getAdjacentStopMode())
				.setDefaultValue(AdjacentStopMode.STOP_ALL)
				.setTooltip(getTooltips("trees", "adjacentStopMode", 9))
				.setSaveConsumer(config::setAdjacentStopMode)
				.build();
		
		var trees = builder.getOrCreateCategory(new TranslatableComponent("text.autoconfig.fallingtree.category.trees"));
		trees.addEntry(breakModeEntry);
		trees.addEntry(detectionModeEntry);
		trees.addEntry(allowedLogsEntry);
		trees.addEntry(deniedLogsEntry);
		trees.addEntry(allowedLeavesEntry);
		trees.addEntry(allowedNonDecayLeavesEntry);
		trees.addEntry(deniedLeavesEntry);
		trees.addEntry(maxScanSizeEntry);
		trees.addEntry(maxSizeEntry);
		trees.addEntry(maxSizeActionEntry);
		trees.addEntry(breakOrderEntry);
		trees.addEntry(treeBreakingEntry);
		trees.addEntry(leavesBreakingEntry);
		trees.addEntry(leavesBreakingForceRadiusEntry);
		trees.addEntry(minimumLeavesAroundRequiredEntry);
		trees.addEntry(includePersistentLeavesInRequiredCountEntry);
		trees.addEntry(allowMixedLogsEntry);
		trees.addEntry(breakNetherTreeWartsEntry);
		trees.addEntry(instantlyBreakWartsEntry);
		trees.addEntry(searchAreaRadiusEntry);
		trees.addEntry(allowedAdjacentBlocks);
		trees.addEntry(adjacentStopModeEntry);
	}
	
	@Environment(EnvType.CLIENT)
	private void fillToolsConfigScreen(ConfigBuilder builder, ToolConfiguration config){
		var ignoreToolsEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("tools", "ignoreTools")), config.isIgnoreTools())
				.setDefaultValue(false)
				.setTooltip(getTooltips("tools", "ignoreTools", 4))
				.setSaveConsumer(config::setIgnoreTools)
				.build();
		var requireEnchantEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("tools", "requireEnchant")), config.isRequireEnchant())
				.setDefaultValue(true)
				.setTooltip(getTooltips("tools", "requireEnchant", 2))
				.setSaveConsumer(config::setRequireEnchant)
				.build();
		var allowedEntry = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("tools", "allowed")), config.getAllowed())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("tools", "allowed", 3))
				.setSaveConsumer(config::setAllowed)
				.setCellErrorSupplier(getMinecraftItemIdCellError())
				.build();
		var deniedEntry = builder.entryBuilder()
				.startStrList(new TranslatableComponent(getFieldName("tools", "denied")), config.getDenied())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("tools", "denied", 3))
				.setSaveConsumer(config::setDenied)
				.setCellErrorSupplier(getMinecraftItemIdCellError())
				.build();
		var damageMultiplicandEntry = builder.entryBuilder()
				.startDoubleField(new TranslatableComponent(getFieldName("tools", "damageMultiplicand")), config.getDamageMultiplicand())
				.setDefaultValue(1)
				.setMin(0)
				.setMax(100)
				.setTooltip(getTooltips("tools", "damageMultiplicand", 7))
				.setSaveConsumer(config::setDamageMultiplicand)
				.build();
		var damageRoundingEntry = builder.entryBuilder()
				.startEnumSelector(new TranslatableComponent(getFieldName("tools", "damageRounding")), DamageRounding.class, config.getDamageRounding())
				.setDefaultValue(DamageRounding.ROUND_DOWN)
				.setTooltip(getTooltips("tools", "damageRounding", 8))
				.setSaveConsumer(config::setDamageRounding)
				.build();
		var speedMultiplicandEntry = builder.entryBuilder()
				.startDoubleField(new TranslatableComponent(getFieldName("tools", "speedMultiplicand")), config.getSpeedMultiplicand())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(50)
				.setTooltip(getTooltips("tools", "speedMultiplicand", 15))
				.setSaveConsumer(config::setSpeedMultiplicand)
				.build();
		var preserveEntry = builder.entryBuilder()
				.startBooleanToggle(new TranslatableComponent(getFieldName("tools", "preserve")), config.isPreserve())
				.setDefaultValue(false)
				.setTooltip(getTooltips("tools", "preserve", 3))
				.setSaveConsumer(config::setPreserve)
				.build();
		
		var tools = builder.getOrCreateCategory(new TranslatableComponent("text.autoconfig.fallingtree.category.tools"));
		tools.addEntry(ignoreToolsEntry);
		tools.addEntry(requireEnchantEntry);
		tools.addEntry(allowedEntry);
		tools.addEntry(deniedEntry);
		tools.addEntry(damageMultiplicandEntry);
		tools.addEntry(damageRoundingEntry);
		tools.addEntry(speedMultiplicandEntry);
		tools.addEntry(preserveEntry);
	}
	
	private String getFieldName(String category, String fieldName){
		return Optional.ofNullable(category)
				.filter(c -> !c.isBlank())
				.map(c -> "text.autoconfig.fallingtree.option." + c + "." + fieldName)
				.orElseGet(() -> "text.autoconfig.fallingtree.option." + fieldName);
	}
	
	private Component[] getTooltips(String category, String fieldName, int count){
		var tooltipKey = getFieldName(category, fieldName) + ".@Tooltip";
		var keys = new LinkedList<String>();
		if(count <= 1){
			keys.add(tooltipKey);
		}
		else{
			for(int i = 0; i < count; i++){
				keys.add(tooltipKey + "[" + i + "]");
			}
		}
		return keys.stream()
				.map(TranslatableComponent::new)
				.toArray(Component[]::new);
	}
	
	public static Function<String, Optional<Component>> getMinecraftBlockIdCellError(){
		return value -> Optional.ofNullable(value)
				.map(v -> MINECRAFT_ID_PATTERN.matcher(v).matches())
				.filter(v -> !v)
				.map(v -> new TranslatableComponent("text.autoconfig.fallingtree.error.invalidBlockResourceLocation"));
	}
	
	public static Function<String, Optional<Component>> getMinecraftItemIdCellError(){
		return value -> Optional.ofNullable(value)
				.map(v -> MINECRAFT_ID_PATTERN.matcher(v).matches())
				.filter(v -> !v)
				.map(v -> new TranslatableComponent("text.autoconfig.fallingtree.error.invalidItemResourceLocation"));
	}
}
