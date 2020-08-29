package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.config.validator.Max;
import fr.raksrinana.fallingtree.config.validator.MaxRunner;
import fr.raksrinana.fallingtree.config.validator.Min;
import fr.raksrinana.fallingtree.config.validator.MinRunner;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.gui.registry.GuiRegistry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Collectors;

@Config(name = "fallingtree")
public class Configuration implements ConfigData{
	@Comment("When set to true, a tree will only be chopped down if the player is sneaking.")
	public final boolean reverseSneaking = false;
	@Comment("When set to true, the mod will cut down trees in creative too.")
	public final boolean breakInCreative = false;
	@ConfigEntry.Category("trees")
	@ConfigEntry.Gui.TransitiveObject
	public TreeConfiguration trees = new TreeConfiguration();
	@ConfigEntry.Category("tools")
	@ConfigEntry.Gui.TransitiveObject
	public ToolConfiguration tools = new ToolConfiguration();
	
	public static Configuration register(){
		Configuration configuration = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new).getConfig();
		
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
			registerGui();
		}
		return configuration;
	}
	
	@Environment(EnvType.CLIENT)
	private static void registerGui(){
		GuiRegistry registry = AutoConfig.getGuiRegistry(Configuration.class);
		MinRunner minRunner = new MinRunner();
		MaxRunner maxRunner = new MaxRunner();
		
		// Validators
		registry.registerAnnotationTransformer((guis, i13n, field, config, defaults, guiProvider) -> guis.stream().peek(gui -> {
			gui.setErrorSupplier(() -> minRunner.apply(gui.getValue(), field.getAnnotation(Min.class)));
		}).collect(Collectors.toList()), Min.class);
		registry.registerAnnotationTransformer((guis, i13n, field, config, defaults, guiProvider) -> guis.stream().peek(gui -> {
			gui.setErrorSupplier(() -> maxRunner.apply(gui.getValue(), field.getAnnotation(Max.class)));
		}).collect(Collectors.toList()), Max.class);
	}
	
	@Override
	public void validatePostLoad() throws ValidationException{
		runValidators(ToolConfiguration.class, this.tools, "tools");
		runValidators(TreeConfiguration.class, this.trees, "trees");
	}
	
	private static <T> void runValidators(Class<T> categoryClass, T category, String categoryName) throws ValidationException{
		try{
			MinRunner minRunner = new MinRunner();
			MaxRunner maxRunner = new MaxRunner();
			
			for(Field field : categoryClass.getDeclaredFields()){
				Min min = field.getAnnotation(Min.class);
				Max max = field.getAnnotation(Max.class);
				
				if(min != null){
					Optional<String> errorMsg = minRunner.apply(field.get(category), min);
					if(errorMsg.isPresent()){
						throw new ValidationException("FallingTree config field " + categoryName + "." + field.getName() + " is invalid: " + errorMsg.get());
					}
				}
				if(max != null){
					Optional<String> errorMsg = maxRunner.apply(field.get(category), max);
					if(errorMsg.isPresent()){
						throw new ValidationException("FallingTree config field " + categoryName + "." + field.getName() + " is invalid: " + errorMsg.get());
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
