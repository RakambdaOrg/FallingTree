package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.config.validator.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Config(name = "fallingtree")
public class Configuration implements ConfigData{
	@Excluded
	private static final MinRunner MIN_RUNNER = new MinRunner();
	@Excluded
	private static final MaxRunner MAX_RUNNER = new MaxRunner();
	@Excluded
	private static final MinMaxRunner MIN_MAX_RUNNER = new MinMaxRunner();
	@Excluded
	private static final BlockIdRunner BLOCK_ID_RUNNER = new BlockIdRunner();
	@Excluded
	private static final ItemIdRunner ITEM_ID_RUNNER = new ItemIdRunner();
	@Excluded
	private static final List<ValidatorRunner<?>> RUNNERS = Arrays.asList(MIN_RUNNER, MAX_RUNNER, MIN_MAX_RUNNER, BLOCK_ID_RUNNER, ITEM_ID_RUNNER);
	@Tooltip(count = 2)
	@Comment("When set to true, a tree will only be chopped down if the player is sneaking.")
	public boolean reverseSneaking = false;
	@Tooltip(count = 2)
	@Comment("When set to true, the mod will cut down trees in creative too.")
	public boolean breakInCreative = false;
	@Category("trees")
	@TransitiveObject
	public TreeConfiguration trees = new TreeConfiguration();
	@Category("tools")
	@TransitiveObject
	public ToolConfiguration tools = new ToolConfiguration();
	
	public static Configuration register(){
		ConfigHolder<Configuration> configHolder = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
		configHolder.registerSaveListener((configHolder1, configuration) -> {
			ConfigCache.getInstance().invalidate();
			return ActionResult.PASS;
		});
		configHolder.registerLoadListener((configHolder1, configuration) -> {
			ConfigCache.getInstance().invalidate();
			return ActionResult.PASS;
		});
		
		Configuration configuration = configHolder.getConfig();
		
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
			registerGui();
		}
		return configuration;
	}
	
	@Environment(EnvType.CLIENT)
	private static void registerGui(){
		GuiRegistry registry = AutoConfig.getGuiRegistry(Configuration.class);
		
		RUNNERS.forEach(runner -> registry.registerAnnotationTransformer((guis, i13n, field, config, defaults, guiProvider) -> guis.stream()
				.peek(gui -> gui.setErrorSupplier(() -> runner.apply(gui.getValue(), field)))
				.collect(toList()), runner.getAnnotationClass()));
	}
	
	@Override
	public void validatePostLoad() throws ValidationException{
		runValidators(Configuration.class, this, "general");
		runValidators(ToolConfiguration.class, this.tools, "tools");
		runValidators(TreeConfiguration.class, this.trees, "trees");
	}
	
	private static <T> void runValidators(Class<T> categoryClass, T category, String categoryName) throws ValidationException{
		try{
			for(Field field : categoryClass.getDeclaredFields()){
				for(ValidatorRunner<?> validator : RUNNERS){
					if(!validator.validateIfAnnotated(field, category)){
						throw new ValidationException("FallingTree config field " + categoryName + "." + field.getName() + " is invalid");
					}
				}
			}
		}
		catch(ReflectiveOperationException | RuntimeException e){
			throw new ValidationException(e);
		}
	}
	
	public ToolConfiguration getToolsConfiguration(){
		return this.tools;
	}
	
	public TreeConfiguration getTreesConfiguration(){
		return this.trees;
	}
	
	public boolean isBreakInCreative(){
		return this.breakInCreative;
	}
	
	public boolean isReverseSneaking(){
		return this.reverseSneaking;
	}
}
