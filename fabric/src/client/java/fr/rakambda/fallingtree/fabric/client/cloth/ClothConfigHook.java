package fr.rakambda.fallingtree.fabric.client.cloth;

import com.google.common.collect.Lists;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.AdjacentStopMode;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.config.enums.BreakOrder;
import fr.rakambda.fallingtree.common.config.enums.DamageRounding;
import fr.rakambda.fallingtree.common.config.enums.DetectionMode;
import fr.rakambda.fallingtree.common.config.enums.MaxSizeAction;
import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import fr.rakambda.fallingtree.common.config.enums.SneakMode;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import fr.rakambda.fallingtree.common.config.real.EnchantmentConfiguration;
import fr.rakambda.fallingtree.common.config.real.PlayerConfiguration;
import fr.rakambda.fallingtree.common.config.real.ToolConfiguration;
import fr.rakambda.fallingtree.common.config.real.TreeConfiguration;
import fr.rakambda.fallingtree.common.config.real.cloth.ClothHookBase;
import fr.rakambda.fallingtree.common.wrapper.IComponent;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class ClothConfigHook extends ClothHookBase{
	public ClothConfigHook(@NotNull FallingTreeCommon<?> mod){
		super(mod);
	}
	
	@NotNull
	public Function<Screen, Screen> load(){
		return (screen) -> {
			var builder = ConfigBuilder.create()
					.setParentScreen(screen)
					.setTitle(Component.literal("FallingTree"));
			
			var configuration = getMod().getOwnConfiguration();
			builder.setSavingRunnable(configuration::onUpdate);
			
			fillConfigScreen(builder, configuration);
			
			return builder.build();
		};
	}
	
	@Environment(EnvType.CLIENT)
	public void fillConfigScreen(@NotNull ConfigBuilder builder, @NotNull Configuration config){
		var reverseSneakingEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName(null, "sneakMode")), SneakMode.class, config.getSneakMode())
				.setDefaultValue(SneakMode.SNEAK_DISABLE)
				.setTooltip(getTooltips(null, "sneakMode", 5))
				.setSaveConsumer(config::setSneakMode)
				.build();
		var breakInCreativeEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName(null, "breakInCreative")), config.isBreakInCreative())
				.setDefaultValue(false)
				.setTooltip(getTooltips(null, "breakInCreative", 2))
				.setSaveConsumer(config::setBreakInCreative)
				.build();
		var lootInCreativeEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName(null, "lootInCreative")), config.isLootInCreative())
				.setDefaultValue(true)
				.setTooltip(getTooltips(null, "lootInCreative", 2))
				.setSaveConsumer(config::setLootInCreative)
				.build();
		var notificationModeEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName(null, "notificationMode")), NotificationMode.class, config.getNotificationMode())
				.setDefaultValue(NotificationMode.ACTION_BAR)
				.setTooltip(getTooltips(null, "notificationMode", 5))
				.setSaveConsumer(config::setNotificationMode)
				.build();
		
		var general = builder.getOrCreateCategory(translatable("text.autoconfig.fallingtree.category.default"));
		general.addEntry(reverseSneakingEntry);
		general.addEntry(breakInCreativeEntry);
		general.addEntry(lootInCreativeEntry);
		general.addEntry(notificationModeEntry);
		
		fillTreesConfigScreen(builder, config.getTrees());
		fillToolsConfigScreen(builder, config.getTools());
		fillPlayerConfigScreen(builder, config.getPlayer());
		fillEnchantmentConfigScreen(builder, config.getEnchantment());
	}
	
	@Environment(EnvType.CLIENT)
	private void fillTreesConfigScreen(@NotNull ConfigBuilder builder, @NotNull TreeConfiguration config){
		var breakModeEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName("trees", "breakMode")), BreakMode.class, config.getBreakMode())
				.setDefaultValue(BreakMode.INSTANTANEOUS)
				.setTooltip(getTooltips("trees", "breakMode", 11))
				.setSaveConsumer(config::setBreakMode)
				.build();
		var detectionModeEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName("trees", "detectionMode")), DetectionMode.class, config.getDetectionMode())
				.setDefaultValue(DetectionMode.WHOLE_TREE)
				.setTooltip(getTooltips("trees", "detectionMode", 9))
				.setSaveConsumer(config::setDetectionMode)
				.build();
		var allowedLogsEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("trees", "allowedLogs")), config.getAllowedLogs())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedLogs", 5))
				.setSaveConsumer(config::setAllowedLogs)
				.setCellErrorSupplier(map(getMinecraftBlockIdCellError()))
				.build();
		var deniedLogsEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("trees", "deniedLogs")), config.getDeniedLogs())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "deniedLogs", 3))
				.setSaveConsumer(config::setDeniedLogs)
				.setCellErrorSupplier(map(getMinecraftBlockIdCellError()))
				.build();
		var allowedLeavesEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("trees", "allowedLeaves")), config.getAllowedLeaves())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedLeaves", 5))
				.setSaveConsumer(config::setAllowedLeaves)
				.setCellErrorSupplier(map(getMinecraftBlockIdCellError()))
				.build();
		var allowedNonDecayLeavesEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("trees", "allowedNonDecayLeaves")), config.getAllowedNonDecayLeaves())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedNonDecayLeaves", 2))
				.setSaveConsumer(config::setAllowedNonDecayLeaves)
				.setCellErrorSupplier(map(getMinecraftBlockIdCellError()))
				.build();
		var deniedLeavesEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("trees", "deniedLeaves")), config.getDeniedLeaves())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "deniedLeaves", 3))
				.setSaveConsumer(config::setDeniedLeaves)
				.setCellErrorSupplier(map(getMinecraftBlockIdCellError()))
				.build();
		var maxScanSizeEntry = builder.entryBuilder()
				.startIntField(translatable(getFieldName("trees", "maxScanSize")), config.getMaxScanSize())
				.setDefaultValue(500)
				.setMin(1)
				.setTooltip(getTooltips("trees", "maxScanSize", 3))
				.setSaveConsumer(config::setMaxScanSize)
				.build();
		var maxSizeEntry = builder.entryBuilder()
				.startIntField(translatable(getFieldName("trees", "maxSize")), config.getMaxSize())
				.setDefaultValue(100)
				.setMin(1)
				.setTooltip(getTooltips("trees", "maxSize", 2))
				.setSaveConsumer(config::setMaxSize)
				.build();
		var maxSizeActionEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName("trees", "maxSizeAction")), MaxSizeAction.class, config.getMaxSizeAction())
				.setDefaultValue(MaxSizeAction.ABORT)
				.setTooltip(getTooltips("trees", "maxSizeAction", 4))
				.setSaveConsumer(config::setMaxSizeAction)
				.build();
		var breakOrderEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName("trees", "breakOrder")), BreakOrder.class, config.getBreakOrder())
				.setDefaultValue(BreakOrder.FURTHEST_FIRST)
				.setTooltip(getTooltips("trees", "breakOrder", 7))
				.setSaveConsumer(config::setBreakOrder)
				.build();
		var treeBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("trees", "treeBreaking")), config.isTreeBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "treeBreaking", 1))
				.setSaveConsumer(config::setTreeBreaking)
				.build();
		var leavesBreakingEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("trees", "leavesBreaking")), config.isLeavesBreaking())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "leavesBreaking", 2))
				.setSaveConsumer(config::setLeavesBreaking)
				.build();
		var leavesBreakingForceRadiusEntry = builder.entryBuilder()
				.startIntField(translatable(getFieldName("trees", "leavesBreakingForceRadius")), config.getLeavesBreakingForceRadius())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(10)
				.setTooltip(getTooltips("trees", "leavesBreakingForceRadius", 8))
				.setSaveConsumer(config::setLeavesBreakingForceRadius)
				.build();
		var minimumLeavesAroundRequiredEntry = builder.entryBuilder()
				.startIntField(translatable(getFieldName("trees", "minimumLeavesAroundRequired")), config.getMinimumLeavesAroundRequired())
				.setDefaultValue(1)
				.setMin(0)
				.setMax(5)
				.setTooltip(getTooltips("trees", "minimumLeavesAroundRequired", 4))
				.setSaveConsumer(config::setMinimumLeavesAroundRequired)
				.build();
		var includePersistentLeavesInRequiredCountEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("trees", "includePersistentLeavesInRequiredCount")), config.isIncludePersistentLeavesInRequiredCount())
				.setDefaultValue(true)
				.setTooltip(getTooltips("trees", "includePersistentLeavesInRequiredCount", 5))
				.setSaveConsumer(config::setIncludePersistentLeavesInRequiredCount)
				.build();
		var allowMixedLogsEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("trees", "allowMixedLogs")), config.isAllowMixedLogs())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "allowMixedLogs", 4))
				.setSaveConsumer(config::setAllowMixedLogs)
				.build();
		var breakNetherTreeWartsEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("trees", "breakNetherTreeWarts")), config.isBreakNetherTreeWarts())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "breakNetherTreeWarts", 2))
				.setSaveConsumer(config::setBreakNetherTreeWarts)
				.build();
		var breakMangroveRootsEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("trees", "breakMangroveRoots")), config.isBreakMangroveRoots())
				.setDefaultValue(false)
				.setTooltip(getTooltips("trees", "breakMangroveRoots", 2))
				.setSaveConsumer(config::setBreakMangroveRoots)
				.build();
		var searchAreaRadiusEntry = builder.entryBuilder()
				.startIntField(translatable(getFieldName("trees", "searchAreaRadius")), config.getSearchAreaRadius())
				.setDefaultValue(-1)
				.setTooltip(getTooltips("trees", "searchAreaRadius", 5))
				.setSaveConsumer(config::setSearchAreaRadius)
				.build();
		var allowedAdjacentBlocks = builder.entryBuilder()
				.startStrList(translatable(getFieldName("trees", "allowedAdjacentBlocks")), config.getAllowedAdjacentBlocks())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("trees", "allowedAdjacentBlocks", 9))
				.setSaveConsumer(config::setAllowedAdjacentBlocks)
				.setCellErrorSupplier(map(getMinecraftBlockIdCellError()))
				.build();
		var adjacentStopModeEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName("trees", "adjacentStopMode")), AdjacentStopMode.class, config.getAdjacentStopMode())
				.setDefaultValue(AdjacentStopMode.STOP_ALL)
				.setTooltip(getTooltips("trees", "adjacentStopMode", 9))
				.setSaveConsumer(config::setAdjacentStopMode)
				.build();
		
		var trees = builder.getOrCreateCategory(translatable("text.autoconfig.fallingtree.category.trees"));
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
		trees.addEntry(breakMangroveRootsEntry);
		trees.addEntry(searchAreaRadiusEntry);
		trees.addEntry(allowedAdjacentBlocks);
		trees.addEntry(adjacentStopModeEntry);
	}
	
	@Environment(EnvType.CLIENT)
	private void fillToolsConfigScreen(@NotNull ConfigBuilder builder, @NotNull ToolConfiguration config){
		var ignoreToolsEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("tools", "ignoreTools")), config.isIgnoreTools())
				.setDefaultValue(false)
				.setTooltip(getTooltips("tools", "ignoreTools", 4))
				.setSaveConsumer(config::setIgnoreTools)
				.build();
		var allowedEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("tools", "allowed")), config.getAllowed())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("tools", "allowed", 3))
				.setSaveConsumer(config::setAllowed)
				.setCellErrorSupplier(map(getMinecraftItemIdCellError()))
				.build();
		var deniedEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("tools", "denied")), config.getDenied())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("tools", "denied", 3))
				.setSaveConsumer(config::setDenied)
				.setCellErrorSupplier(map(getMinecraftItemIdCellError()))
				.build();
		var damageMultiplicandEntry = builder.entryBuilder()
				.startDoubleField(translatable(getFieldName("tools", "damageMultiplicand")), config.getDamageMultiplicand())
				.setDefaultValue(1)
				.setMin(0)
				.setMax(100)
				.setTooltip(getTooltips("tools", "damageMultiplicand", 7))
				.setSaveConsumer(config::setDamageMultiplicand)
				.build();
		var damageRoundingEntry = builder.entryBuilder()
				.startEnumSelector(translatable(getFieldName("tools", "damageRounding")), DamageRounding.class, config.getDamageRounding())
				.setDefaultValue(DamageRounding.ROUND_DOWN)
				.setTooltip(getTooltips("tools", "damageRounding", 8))
				.setSaveConsumer(config::setDamageRounding)
				.build();
		var speedMultiplicandEntry = builder.entryBuilder()
				.startDoubleField(translatable(getFieldName("tools", "speedMultiplicand")), config.getSpeedMultiplicand())
				.setDefaultValue(0)
				.setMin(0)
				.setMax(50)
				.setTooltip(getTooltips("tools", "speedMultiplicand", 15))
				.setSaveConsumer(config::setSpeedMultiplicand)
				.build();
		var preserveEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("tools", "preserve")), config.isPreserve())
				.setDefaultValue(false)
				.setTooltip(getTooltips("tools", "preserve", 3))
				.setSaveConsumer(config::setPreserve)
				.build();
		var forceToolUsageEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("tools", "forceToolUsage")), config.isForceToolUsage())
				.setDefaultValue(false)
				.setTooltip(getTooltips("tools", "forceToolUsage", 2))
				.setSaveConsumer(config::setForceToolUsage)
				.build();
		
		var tools = builder.getOrCreateCategory(translatable("text.autoconfig.fallingtree.category.tools"));
		tools.addEntry(ignoreToolsEntry);
		tools.addEntry(allowedEntry);
		tools.addEntry(deniedEntry);
		tools.addEntry(damageMultiplicandEntry);
		tools.addEntry(damageRoundingEntry);
		tools.addEntry(speedMultiplicandEntry);
		tools.addEntry(preserveEntry);
		tools.addEntry(forceToolUsageEntry);
	}
	
	@Environment(EnvType.CLIENT)
	private void fillPlayerConfigScreen(@NotNull ConfigBuilder builder, @NotNull PlayerConfiguration config){
		var allowedTagsEntry = builder.entryBuilder()
				.startStrList(translatable(getFieldName("player", "allowedTags")), config.getAllowedTags())
				.setDefaultValue(Lists.newArrayList())
				.setTooltip(getTooltips("player", "allowedTags", 4))
				.setSaveConsumer(config::setAllowedTags)
				.build();
		
		var tools = builder.getOrCreateCategory(translatable("text.autoconfig.fallingtree.category.player"));
		tools.addEntry(allowedTagsEntry);
	}
	
	@Environment(EnvType.CLIENT)
	private void fillEnchantmentConfigScreen(@NotNull ConfigBuilder builder, @NotNull EnchantmentConfiguration config){
		var registerEnchantEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("enchantment", "registerEnchant")), config.isRegisterEnchant())
				.setDefaultValue(false)
				.setTooltip(getTooltips("enchantment", "registerEnchant", 8))
				.setSaveConsumer(config::setRegisterEnchant)
				.build();
		var registerSpecificEnchantEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("enchantment", "registerSpecificEnchant")), config.isRegisterSpecificEnchant())
				.setDefaultValue(false)
				.setTooltip(getTooltips("enchantment", "registerSpecificEnchant", 9))
				.setSaveConsumer(config::setRegisterSpecificEnchant)
				.build();
		var hideEnchantEntry = builder.entryBuilder()
				.startBooleanToggle(translatable(getFieldName("enchantment", "hideEnchant")), config.isHideEnchant())
				.setDefaultValue(false)
				.setTooltip(getTooltips("enchantment", "hideEnchant", 2))
				.setSaveConsumer(config::setHideEnchant)
				.build();
		
		var enchantment = builder.getOrCreateCategory(translatable("text.autoconfig.fallingtree.category.enchantment"));
		enchantment.addEntry(registerEnchantEntry);
		enchantment.addEntry(registerSpecificEnchantEntry);
		enchantment.addEntry(hideEnchantEntry);
	}
	
	@NotNull
	private Component translatable(@NotNull String key){
		return (Component) getMod().translate(key).getRaw();
	}
	
	@NotNull
	private Function<String, Optional<Component>> map(@NotNull Function<String, Optional<IComponent>> fct){
		return str -> fct.apply(str).map(IComponent::getRaw).map(Component.class::cast);
	}
	
	@NotNull
	protected Component[] getTooltips(@Nullable String category, @NotNull String fieldName, int count){
		return getTooltipsInternal(category, fieldName, count)
				.map(IComponent::getRaw)
				.map(Component.class::cast)
				.toArray(Component[]::new);
	}
}
