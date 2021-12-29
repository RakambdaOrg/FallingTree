package fr.raksrinana.fallingtree.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.raksrinana.fallingtree.fabric.config.cloth.ClothConfigHook;
import me.shedaniel.autoconfig.gui.DefaultGuiProviders;
import me.shedaniel.autoconfig.gui.DefaultGuiTransformers;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public class ModMenuImpl implements ModMenuApi{
	private static final GuiRegistry defaultGuiRegistry = DefaultGuiTransformers.apply(DefaultGuiProviders.apply(new GuiRegistry()));
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		if(FabricLoader.getInstance().isModLoaded("cloth-config")){
			return (screen) -> {
				try{
					return Class.forName("fr.raksrinana.fallingtree.fabric.config.cloth.ClothConfigHook")
							.asSubclass(ClothConfigHook.class)
							.getConstructor()
							.newInstance()
							.load()
							.apply(screen);
				}
				catch(ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e){
					FallingTree.logger.error("Failed to hook into ClothConfig", e);
				}
				return null;
			};
		}
		return screen -> null;
	}
}
